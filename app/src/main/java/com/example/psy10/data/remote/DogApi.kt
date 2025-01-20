package com.example.psy10.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface DogApi {
    @GET("breeds/list/all") // Pobiera listę wszystkich ras psów
    suspend fun getAllBreeds(): BreedListResponse

    @GET("breed/{breed}/images/random")     // Pobiera losowe zdjęcie psa konkretnej rasy
    suspend fun getRandomDogImageByBreed(
        @Path("breed") breed: String
    ): DogApiResponse

    @GET("breeds/image/random")      // Pobiera losowe zdjęcie dowolnego psa
    suspend fun getRandomDogImage(): DogApiResponse

    companion object {
        const val BASE_URL = "https://dog.ceo/api/"         // Bazowy URL API
    }
}