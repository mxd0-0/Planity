package com.mohammed.planity.domain.model

import java.util.Date

data class Task(
    val id: String,
    val title: String,
    val date: String,
    val category: String,
    val isHighPriority: Boolean,

    // --- ADD THIS FIELD ---
    val completedAt: Date? = null
)