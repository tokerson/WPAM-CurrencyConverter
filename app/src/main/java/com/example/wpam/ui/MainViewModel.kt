package com.example.wpam.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wpam.data.CurrencyRepository
import com.example.wpam.data.model.Currency

class MainViewModel(private val currencyRepository: CurrencyRepository) : ViewModel() {
    val xd = "xd"
    val symbols : MutableLiveData<List<Currency>> = currencyRepository.getAllSymbols()
}