package com.example.bionime.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MaskApiService {
    private const val BASE_URL = "https://raw.githubusercontent.com/kiang/pharmacies/master/json/"

    private val retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()

    val api: MaskApi = retrofit.create(MaskApi::class.java)
}