package com.example.wpam.service

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CurrencyService {
    @GET("symbols")
    fun getSymbols(
        @Query("access_key") key: String
    ): Call<JsonElement>

    @GET("latest")
    fun getLatestRates(
        @Query("symbols") symbols: String,
        @Query("access_key") key: String
    ): Call<JsonElement>

}