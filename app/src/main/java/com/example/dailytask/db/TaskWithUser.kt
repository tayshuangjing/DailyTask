package com.example.dailytask.db

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithUser (
    @Embedded val task: Task,
    @Relation(
        parentColumn = "username",
        entityColumn = "username"
    )
    val user: User
)