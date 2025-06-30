package com.mohammed.planity.domain.use_case

import com.mohammed.planity.domain.model.Task
import com.mohammed.planity.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class TaskStats(
    val completedTasks: Int,
    val pendingTasks: Int,
    // The Map key is the 3-letter day name (e.g., "Sun"), value is the count
    val weeklyCompletion: Map<String, Int>
)

class GetWeeklyTaskStatsUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    // Define the date format your tasks are saved in (e.g., "d,MMMM,yyyy")
    private val firestoreDateFormat = SimpleDateFormat("d,MMMM,yyyy", Locale.getDefault())

    operator fun invoke(): Flow<TaskStats> {
        return repository.getTasks().map { tasks ->
            // --- START OF CORRECTED LOGIC ---

            // 1. Calculate overall stats based on the "category" field
            val completedTasks = tasks.filter { it.category == "Completed" }
            val pendingTasks = tasks.filter { it.category != "Completed" && it.category != "Trash" }

            // 2. Prepare data for the last 7 days
            val calendar = Calendar.getInstance()
            val today = calendar.time
            calendar.add(Calendar.DAY_OF_YEAR, -6) // Go back 6 days to get a 7-day window
            val sevenDaysAgo = calendar.time

            // 3. Filter tasks to only include those in the "Completed" category from the last 7 days
            val recentCompletedTasks = completedTasks.filter { task ->
                isWithinDateRange(parseDate(task.date), sevenDaysAgo, today)
            }

            // 4. Group these recent completed tasks by day of the week
            val weeklyMap = groupTasksByDayOfWeek(recentCompletedTasks)

            // --- END OF CORRECTED LOGIC ---

            TaskStats(
                completedTasks = completedTasks.size,
                pendingTasks = pendingTasks.size,
                weeklyCompletion = weeklyMap
            )
        }
    }

    /**
     * Parses the date string from Firestore into a Date object.
     * Returns null if parsing fails.
     */
    private fun parseDate(dateString: String): Date? {
        return try {
            firestoreDateFormat.parse(dateString)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Checks if a given date is within the last 7 days.
     */
    private fun isWithinDateRange(date: Date?, startDate: Date, endDate: Date): Boolean {
        return date != null && !date.before(startDate) && !date.after(endDate)
    }

    /**
     * Takes a list of tasks and groups them into a map where the key is the
     * day of the week ("Sun", "Mon", etc.) and the value is the count of tasks.
     * It also ensures all 7 days are present in the map, even with a count of 0.
     */
    private fun groupTasksByDayOfWeek(tasks: List<Task>): Map<String, Int> {
        val calendar = Calendar.getInstance()
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault()) // "EEE" gives "Sun", "Mon", etc.

        // Initialize a map with all 7 days of the week, ordered correctly, with a count of 0
        val weeklyCompletionMap = mutableMapOf<String, Int>()
        val dayLabels = mutableListOf<String>()
        val tempCal = Calendar.getInstance()
        tempCal.add(Calendar.DAY_OF_YEAR, -6)
        repeat(7) {
            val dayName = dayFormat.format(tempCal.time)
            dayLabels.add(dayName)
            weeklyCompletionMap[dayName] = 0
            tempCal.add(Calendar.DAY_OF_YEAR, 1)
        }

        // Count the tasks for each day
        tasks.forEach { task ->
            parseDate(task.date)?.let { date ->
                val dayOfWeek = dayFormat.format(date)
                weeklyCompletionMap[dayOfWeek] = (weeklyCompletionMap[dayOfWeek] ?: 0) + 1
            }
        }

        // Return a map that maintains the correct order of days
        return dayLabels.associateWith { weeklyCompletionMap[it] ?: 0 }
    }
}