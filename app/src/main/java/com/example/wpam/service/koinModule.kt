package com.example.wpam.service

import com.example.wpam.data.CurrencyRepository
import com.example.wpam.ui.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val FIXER_API_URL = "http://data.fixer.io/api/"

val koinModule = module {

    single<CurrencyService> {
        Retrofit.Builder()
            .baseUrl(FIXER_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyService::class.java)
    }

    single { CurrencyRepository(get(), androidContext()) }

    single { MainViewModel(get()) }
}