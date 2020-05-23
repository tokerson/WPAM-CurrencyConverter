package com.example.wpam.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wpam.data.CurrencyRepository
import com.example.wpam.data.model.Currency

class CurrencyViewModel(private val currencyRepository: CurrencyRepository) : ViewModel() {
    val symbols: MutableLiveData<List<Currency>> = currencyRepository.getAllSymbols()

    val baseCurrency: MutableLiveData<Currency> = MutableLiveData()
    val wantedCurrency: MutableLiveData<Currency> = MutableLiveData()
    val conversionResult: MutableLiveData<Float> = currencyRepository.conversionResult

    fun convertCurrencies(from: Currency, to: Currency, amount: Float) {
        currencyRepository.convertCurrencies(from, to, amount)
    }
}