package com.example.bionime.network

import retrofit2.http.GET

interface MaskApi {
    @GET("points.json")
    suspend fun getMasks(): MaskResponse
}

data class MaskResponse(val features: List<Feature>)
data class Feature(val properties: Properties)
data class Properties(
    val id: String,
    val name: String,
    val address: String,
    val mask_adult: Int,
    val mask_child: Int,
    val town: String,
    val county: String
)