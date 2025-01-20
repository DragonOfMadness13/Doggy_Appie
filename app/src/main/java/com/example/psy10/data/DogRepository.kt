package com.example.psy10.data

import com.example.psy10.data.local.database.DogEntity
import com.example.psy10.data.local.database.DogEntityDao
import com.example.psy10.data.models.Dog
import com.example.psy10.data.remote.DogApi
import com.example.psy10.data.remote.UserApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.random.Random
import android.util.Log

interface DogRepository {
    val dogs: Flow<List<Dog>>
    suspend fun add(name: String)
    suspend fun remove(id: Int)
    suspend fun triggerFav(id: Int)
    suspend fun getDogById(id: Int): Dog?
}

class DefaultDogRepository @Inject constructor(
    private val dogDao: DogEntityDao,
    private val dogApi: DogApi,
    private val userApi: UserApi
) : DogRepository {

    override val dogs: Flow<List<Dog>> = dogDao.getSortedDogs().map { items ->
        items.map {
            Dog(
                id = it.uid,
                name = it.name,
                breed = it.breed,
                imageUrl = it.imageUrl,
                ownerName = it.ownerName,
                isFav = it.isFav
            )
        }
    }

    override suspend fun add(name: String) {
        try {
            // Get random breed
            val breeds = dogApi.getAllBreeds().message.keys.toList()
            val randomBreed = breeds[Random.nextInt(breeds.size)]

            // Get random image
            val imageUrl = dogApi.getRandomDogImageByBreed(randomBreed).message

            // Get random owner
            val users = userApi.getUsers()
            val randomUser = users[Random.nextInt(users.size)]

            Log.d("DogRepository", "Adding new dog: $name, breed: $randomBreed")

            dogDao.insertDog(
                DogEntity(
                    name = name,
                    breed = randomBreed,
                    imageUrl = imageUrl,
                    ownerName = randomUser.name,
                    isFav = false
                )
            )
        } catch (e: Exception) {
            Log.e("DogRepository", "Error adding dog", e)
            throw e
        }
    }

    override suspend fun remove(id: Int) {
        dogDao.removeDog(id)
    }

    override suspend fun triggerFav(id: Int) {
        dogDao.triggerFavDog(id)
    }

    override suspend fun getDogById(id: Int): Dog? {
        return dogDao.getDogById(id)?.let {
            Dog(
                id = it.uid,
                name = it.name,
                breed = it.breed,
                imageUrl = it.imageUrl,
                ownerName = it.ownerName,
                isFav = it.isFav
            )
        }
    }
}