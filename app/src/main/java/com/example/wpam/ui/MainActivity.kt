package com.example.wpam.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.example.wpam.R
import com.example.wpam.data.model.Currency
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        takePictureButton.setOnClickListener{
            val intent = Intent(this, PhotoActivity::class.java)
            startActivity(intent)
        }

        mainViewModel.symbols.observe(this@MainActivity, Observer {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, it)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            symbols_dropdown_1.adapter = adapter
            symbols_dropdown_2.adapter = adapter
        })

        mainViewModel.conversionResult.observe(this@MainActivity, Observer {
            conversion_result.text = it.toString()
        })

        calculate_button.setOnClickListener {
            mainViewModel.convertCurrencies(
                symbols_dropdown_1.selectedItem as Currency,
                symbols_dropdown_2.selectedItem as Currency,
                price.text.toString().toFloat()
            )
        }

    }
}
