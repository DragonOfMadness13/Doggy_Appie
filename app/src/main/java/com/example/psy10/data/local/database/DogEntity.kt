package com.example.psy10.data.local.database

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Encja bazodanowa
@Entity(tableName = "dogs")
data class DogEntity(
    val name: String,
    val breed: String,
    val imageUrl: String,
    val ownerName: String,

    @ColumnInfo(defaultValue = "0")
    val isFav: Boolean = false
) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}

// Data Access Object (DAO) dla operacji bazodanowych
@Dao
interface DogEntityDao {
    @Query("SELECT * FROM dogs")
    fun getAllDogs(): Flow<List<DogEntity>>

    @Query("SELECT * FROM dogs ORDER BY uid DESC LIMIT 10")
    fun getSortedDogs(): Flow<List<DogEntity>>

    @Query("SELECT * FROM dogs WHERE isFav = 1")
    fun getAllFavDogs(): Flow<List<DogEntity>>

    @Query("SELECT * FROM dogs WHERE uid = :id")
    suspend fun getDogById(id: Int): DogEntity?

    @Query("UPDATE dogs SET isFav = CASE WHEN isFav = 1 THEN 0 ELSE 1 END WHERE uid = :id")
    suspend fun triggerFavDog(id: Int)

    @Insert
    suspend fun insertDog(dog: DogEntity)

    @Query("DELETE FROM dogs WHERE uid = :id")
    suspend fun removeDog(id: Int)
}