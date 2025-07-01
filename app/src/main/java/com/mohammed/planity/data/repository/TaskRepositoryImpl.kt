package com.mohammed.planity.data.repository

import com.google.firebase.auth.FirebaseAuth
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


// NO @Inject constructor for Koin
class TaskRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth // <-- Add the FirebaseAuth dependency
) : TaskRepository {

    companion object {
        private const val USERS_COLLECTION = "users"
        private const val TASKS_SUBCOLLECTION = "tasks"
        private const val CATEGORIES_SUBCOLLECTION = "categories"
    }

    // A helper to get the current user's document path. This is the key to multi-user support.
    private val currentUserDocRef
        get() = auth.currentUser?.uid?.let { uid ->
            firestore.collection(USERS_COLLECTION).document(uid)
        }

    // --- Task Operations (Now User-Specific) ---

    override fun getTasks(): Flow<List<Task>> = callbackFlow {
        val userDoc = currentUserDocRef
        if (userDoc == null) {
            trySend(emptyList()); close(); return@callbackFlow
        }
        val listener = userDoc.collection(TASKS_SUBCOLLECTION).addSnapshotListener { snapshot, error ->
            if (error != null) { close(error); return@addSnapshotListener }
            if (snapshot != null) {
                val tasks = snapshot.documents.mapNotNull { doc ->
                    doc.toObject<TaskDto>()?.copy(id = doc.id)?.toDomain()
                }
                trySend(tasks)
            }
        }
        awaitClose { listener.remove() }
    }

    override suspend fun createTask(task: Task) {
        currentUserDocRef?.collection(TASKS_SUBCOLLECTION)?.add(task.toDto())?.await()
    }

    override suspend fun updateTask(task: Task) {
        currentUserDocRef?.collection(TASKS_SUBCOLLECTION)?.document(task.id)?.set(task.toDto())?.await()
    }

    override suspend fun moveTaskToCategory(taskId: String, newCategory: String) {
        val userDoc = currentUserDocRef ?: return
        try {
            val updates = mutableMapOf<String, Any>("category" to newCategory)
            if (newCategory == "Completed") {
                updates["completedAt"] = FieldValue.serverTimestamp()
            } else {
                updates["completedAt"] = FieldValue.delete()
            }
            userDoc.collection(TASKS_SUBCOLLECTION).document(taskId).update(updates).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteTask(taskId: String) {
        currentUserDocRef?.collection(TASKS_SUBCOLLECTION)?.document(taskId)?.delete()?.await()
    }

    override fun getTaskById(taskId: String): Flow<Task?> = callbackFlow {
        val userDoc = currentUserDocRef
        if (userDoc == null) {
            trySend(null); close(); return@callbackFlow
        }
        val listener = userDoc.collection(TASKS_SUBCOLLECTION).document(taskId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
                if (snapshot != null && snapshot.exists()) {
                    trySend(snapshot.toObject<TaskDto>()?.copy(id = snapshot.id)?.toDomain())
                } else {
                    trySend(null)
                }
            }
        awaitClose { listener.remove() }
    }

    // --- Category Operations (Now User-Specific) ---

    override fun getCategories(): Flow<List<Category>> = callbackFlow {
        val userDoc = currentUserDocRef
        if (userDoc == null) {
            trySend(emptyList()); close(); return@callbackFlow
        }
        val listener = userDoc.collection(CATEGORIES_SUBCOLLECTION)
            .orderBy("orderIndex", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) { close(error); return@addSnapshotListener }
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
        val userDoc = currentUserDocRef ?: return
        try {
            val newIndex = userDoc.collection(CATEGORIES_SUBCOLLECTION).get().await().size()
            val categoryData = mapOf("name" to categoryName, "orderIndex" to newIndex)
            userDoc.collection(CATEGORIES_SUBCOLLECTION).add(categoryData).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteCategory(categoryId: String) {
        currentUserDocRef?.collection(CATEGORIES_SUBCOLLECTION)?.document(categoryId)?.delete()?.await()
    }

    override suspend fun updateCategoryOrder(categories: List<Category>) {
        val userDoc = currentUserDocRef ?: return
        try {
            val batch = firestore.batch()
            categories.forEachIndexed { index, category ->
                val docRef = userDoc.collection(CATEGORIES_SUBCOLLECTION).document(category.id)
                batch.update(docRef, "orderIndex", index)
            }
            batch.commit().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}