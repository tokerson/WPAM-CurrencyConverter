package com.example.wpam

import android.app.Application
import com.example.wpam.service.koinModule
import org.koin.android.ext.android.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(koinModule))
    }
}