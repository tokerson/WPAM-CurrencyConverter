package com.example.wpam.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.wpam.R
import com.example.wpam.data.model.Currency
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_settings, container, false)



        mainViewModel.symbols.observe(this@MainFragment, Observer {
            val adapter = ArrayAdapter(this.context!!, android.R.layout.simple_spinner_item, it)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            symbols_dropdown_1.adapter = adapter
            symbols_dropdown_2.adapter = adapter
        })

        mainViewModel.conversionResult.observe(this@MainFragment, Observer {
            conversion_result.text = it.toString()
        })

        root.calculate_button.setOnClickListener {
            mainViewModel.convertCurrencies(
                symbols_dropdown_1.selectedItem as Currency,
                symbols_dropdown_2.selectedItem as Currency,
                price.text.toString().toFloat()
            )
        }
        return root
    }
}
