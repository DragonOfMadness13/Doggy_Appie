package com.example.psy10.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [DogEntity::class],
    version = 2,  // Zwiększamy wersję z 1 na 2
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun dogDao(): DogEntityDao
}