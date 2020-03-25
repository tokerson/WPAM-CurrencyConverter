package com.example.wpam.data

import android.content.Context
import com.example.wpam.R
import com.example.wpam.service.CurrencyService

class CurrencyRepository(
    private val currencyService: CurrencyService,
    private val context: Context
) {
    private val FIXER_API_KEY = context.getString(R.string.fixer_api_key)

    fun getAllSymbols() {

    }
}