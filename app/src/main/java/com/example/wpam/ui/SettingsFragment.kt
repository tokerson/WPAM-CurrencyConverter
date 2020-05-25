package com.example.wpam.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.wpam.R
import com.example.wpam.data.model.Currency
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SettingsFragment : Fragment() {

    private val currencyViewModel: CurrencyViewModel by sharedViewModel()
    private lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_settings, container, false)

        currencyViewModel.symbols.observe(this@SettingsFragment, Observer {
            val adapter = ArrayAdapter(this.context!!, android.R.layout.simple_spinner_item, it)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            original_symbol_dropdown.adapter = adapter
            desired_symbol_dropdown.adapter = adapter
        })

        root.original_symbol_dropdown.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                currencyViewModel.baseCurrency.value = original_symbol_dropdown.selectedItem as Currency?
                currencyViewModel.updateConversionRate()
            }
        }

        root.desired_symbol_dropdown.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                currencyViewModel.wantedCurrency.value = desired_symbol_dropdown.selectedItem as Currency?
                currencyViewModel.updateConversionRate()
            }
        }

        return root
    }
}
