package com.mohammed.planity.data.mapper

import com.mohammed.planity.data.model.CategoryDto
import com.mohammed.planity.domain.model.Category

fun CategoryDto.toDomain(): Category {
    return Category(
        id = this.id ?: "",
        name = this.name ?: "Unnamed",
        orderIndex = this.orderIndex ?: 0
    )
}