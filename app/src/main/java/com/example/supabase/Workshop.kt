package com.example.supabase

import kotlinx.serialization.Serializable

@Serializable
data class Workshop(
    val id: Int? = null,
    val workshopText: String
)
