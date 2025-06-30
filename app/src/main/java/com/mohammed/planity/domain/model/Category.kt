package com.mohammed.planity.domain.model

/**
 * A more detailed domain model for a Category, including its ID.
 */
data class Category(
    val id: String,
    val name: String,
    val orderIndex: Int,
    val taskCount: Int = 0
)