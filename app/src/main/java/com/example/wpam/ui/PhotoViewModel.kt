package com.example.wpam.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.ml.vision.common.FirebaseVisionImage

class PhotoViewModel() : ViewModel() {
    val image: MutableLiveData<FirebaseVisionImage> = MutableLiveData()
}
