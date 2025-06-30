package com.mohammed.planity.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date
data class TaskDto(
    val id: String? = null,
    val title: String? = null,
    val date: String? = null,
    val category: String? = null,
    val isHighPriority: Boolean? = null,

    // --- ADD THIS FIELD ---
    // @ServerTimestamp tells Firestore to populate this field with the server's time
    // when the document is written or updated.
    @ServerTimestamp
    val completedAt: Date? = null
)