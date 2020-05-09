package com.example.wpam.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wpam.R
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.text = mainViewModel.xd
    }
}
