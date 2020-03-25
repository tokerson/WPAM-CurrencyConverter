package com.example.wpam.service

import retrofit2.Call
import retrofit2.http.GET

interface CurrencyService {
    @GET("symbols")
    fun getSymbols(): Call<String>
}