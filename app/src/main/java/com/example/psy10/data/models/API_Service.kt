package com.example.psy10.data.remote

import retrofit2.http.GET

data class User(
    val id: Int,
    val name: String
)

interface UserApi {
    @GET("users")
    suspend fun getUsers(): List<User>

    companion object {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    }
}