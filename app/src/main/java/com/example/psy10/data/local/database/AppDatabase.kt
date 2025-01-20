package com.example.psy10.data.local.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

// Baza danych RoomDatabes
@Database(entities = [DogEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun dogDao(): DogEntityDao
}