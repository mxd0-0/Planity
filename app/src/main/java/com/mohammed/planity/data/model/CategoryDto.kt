package com.mohammed.planity.data.model

// Represents a category document in Firestore
data class CategoryDto(
    val id: String? = null,
    val name: String? = null,
    val orderIndex: Int? = null // To store the position
)