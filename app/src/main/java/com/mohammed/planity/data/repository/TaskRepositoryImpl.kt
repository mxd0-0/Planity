package com.mohammed.planity.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.mohammed.planity.data.mapper.toDomain
import com.mohammed.planity.data.mapper.toDto
import com.mohammed.planity.data.model.CategoryDto
import com.mohammed.planity.data.model.TaskDto
import com.mohammed.planity.domain.model.Category
import com.mohammed.planity.domain.model.Task
import com.mohammed.planity.domain.repository.TaskRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : TaskRepository {

    companion object {
        private const val TASKS_COLLECTION = "tasks"
        private const val CATEGORIES_COLLECTION = "categories"
    }
    override suspend fun deleteTask(taskId: String) {
        try {
            firestore.collection(TASKS_COLLECTION).document(taskId).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    // --- Task Operations ---

    override fun getTasks(): Flow<List<Task>> = callbackFlow {
        val listener =
            firestore.collection(TASKS_COLLECTION).addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val tasks = snapshot.documents.mapNotNull { doc ->
                        val taskDto = doc.toObject<TaskDto>()?.copy(id = doc.id)
                        taskDto?.toDomain()
                    }
                    trySend(tasks)
                }
            }
        awaitClose { listener.remove() }
    }

    override fun getTaskById(taskId: String): Flow<Task?> = callbackFlow {
        val listener = firestore.collection(TASKS_COLLECTION).document(taskId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val taskDto = snapshot.toObject<TaskDto>()?.copy(id = snapshot.id)
                    trySend(taskDto?.toDomain())
                } else {
                    trySend(null)
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun createTask(task: Task) {
        try {
            // --- FIX: Removed reference to the non-existent isCompleted property ---
            val taskDto = TaskDto(
                title = task.title,
                date = task.date,
                category = task.category,
                isHighPriority = task.isHighPriority
            )
            firestore.collection(TASKS_COLLECTION).add(taskDto).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun updateTask(task: Task) {
        try {
            val taskDto = task.toDto()
            firestore.collection(TASKS_COLLECTION).document(task.id).set(taskDto).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun moveTaskToCategory(taskId: String, newCategory: String) {
        try {
            // We use a map to build our update request.
            val updates = mutableMapOf<String, Any>()

            // The primary update is always the category.
            updates["category"] = newCategory

            // Conditionally add the timestamp logic.
            if (newCategory == "Completed") {
                // If the task is being marked as complete, tell Firestore to add the server time.
                updates["completedAt"] = FieldValue.serverTimestamp()
            } else {
                // If the task is being moved OUT of "Completed" (e.g., "un-checked"),
                // we tell Firestore to completely remove the completedAt field.
                updates["completedAt"] = FieldValue.delete()
            }

            // Perform the update with all our changes.
            firestore.collection(TASKS_COLLECTION)
                .document(taskId)
                .update(updates)
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // --- REMOVED: The old deleteTask is replaced by moveTaskToCategory(id, "Trash") ---
    // override suspend fun deleteTask(taskId: String) { ... }


    // --- Category Operations ---

    override fun getCategories(): Flow<List<Category>> = callbackFlow {
        val listener = firestore.collection(CATEGORIES_COLLECTION)
            .orderBy("orderIndex", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error); return@addSnapshotListener
                }
                if (snapshot != null) {
                    val categories = snapshot.documents.mapNotNull {
                        it.toObject<CategoryDto>()?.copy(id = it.id)?.toDomain()
                    }
                    trySend(categories)
                }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun createCategory(categoryName: String) {
        try {
            val newIndex = firestore.collection(CATEGORIES_COLLECTION).get().await().size()
            val categoryData = mapOf(
                "name" to categoryName,
                "orderIndex" to newIndex
            )
            firestore.collection(CATEGORIES_COLLECTION).add(categoryData).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteCategory(categoryId: String) {
        try {
            firestore.collection(CATEGORIES_COLLECTION).document(categoryId).delete().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun updateCategoryOrder(categories: List<Category>) {
        try {
            val batch = firestore.batch()
            categories.forEachIndexed { index, category ->
                val docRef = firestore.collection(CATEGORIES_COLLECTION).document(category.id)
                batch.update(docRef, "orderIndex", index)
            }
            batch.commit().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}