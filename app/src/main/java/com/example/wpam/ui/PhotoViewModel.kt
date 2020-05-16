package com.example.wpam.ui

import androidx.camera.core.ImageCapture
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wpam.data.CurrencyRepository
import com.example.wpam.data.model.Currency
import com.google.firebase.ml.vision.common.FirebaseVisionImage

class PhotoViewModel() : ViewModel() {
    val image: MutableLiveData<FirebaseVisionImage> = MutableLiveData()
}