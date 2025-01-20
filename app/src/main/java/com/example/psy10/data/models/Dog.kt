package com.example.psy10.data.models

// To jest model danych używany w UI
data class Dog(
    val id: Int,
    val name: String,
    val breed: String,
    val imageUrl: String,
    val ownerName: String,
    val isFav: Boolean
)