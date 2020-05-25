package com.example.wpam.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wpam.data.CurrencyRepository
import com.example.wpam.data.model.Currency

class CurrencyViewModel(private val currencyRepository: CurrencyRepository) : ViewModel() {
    val symbols: MutableLiveData<List<Currency>> = currencyRepository.getAllSymbols()
    val baseCurrency: MutableLiveData<Currency> = MutableLiveData()
    val wantedCurrency: MutableLiveData<Currency> = MutableLiveData()
    val conversionRate: MutableLiveData<Float> = currencyRepository.conversionRate
    val loading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun updateConversionRate() {
        if(baseCurrency.value != null && wantedCurrency.value != null){
            currencyRepository.getConversionRate(baseCurrency.value!!, wantedCurrency.value!!)
        }
    }

    fun startLoading() {
        loading.value = true
    }

    fun stopLoading() {
        loading.value = false
    }
}