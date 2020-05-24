package com.example.wpam.data

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import com.example.wpam.R
import com.example.wpam.data.model.Currency
import com.example.wpam.service.CurrencyService
import java.net.SocketTimeoutException

class CurrencyRepository(
    private val currencyService: CurrencyService,
    private val context: Context
) {
    private val FIXER_API_KEY = context.getString(R.string.fixer_api_key)

    val symbols = MutableLiveData<List<Currency>>()
    val conversionRate = MutableLiveData<Float>()

    fun getAllSymbols(): MutableLiveData<List<Currency>> {
        class GetAllSymbolsTask : AsyncTask<Void, Void, List<Currency>>() {
            override fun doInBackground(vararg params: Void?): List<Currency> {
                return try {
                    val currencyServiceResponse = currencyService.getSymbols(
                        key = FIXER_API_KEY
                    ).execute().body()
                    val fetchedSymbols = mutableListOf<Currency>()

                    val fetchedSymbols2 =
                        currencyServiceResponse!!.asJsonObject.get("symbols").asJsonObject

                    for (symbol in fetchedSymbols2.entrySet()) {
                        fetchedSymbols.add(
                            Currency(
                                shortName = symbol.key,
                                name = symbol.value.asString
                            )
                        )
                    }
                    fetchedSymbols
                } catch (exception: SocketTimeoutException) {
                    arrayListOf()
                }
            }

            override fun onPostExecute(result: List<Currency>?) {
                super.onPostExecute(result)
                symbols.value = result
            }
        }

        GetAllSymbolsTask().execute()
        return symbols
    }

    fun getConversionRate(from: Currency, to: Currency): MutableLiveData<Float> {
        class GetConversionRateTask : AsyncTask<Void, Void, Float>() {
            override fun doInBackground(vararg params: Void?): Float {
                val currencyServiceResponse = currencyService.getLatestRates(
                    key = FIXER_API_KEY,
                    symbols = from.shortName + "," + to.shortName
                ).execute().body()

                val rates = currencyServiceResponse!!.asJsonObject.get("rates").asJsonObject

                val baseRate = rates.get(from.shortName).asFloat
                val toConvertRate = rates.get(to.shortName).asFloat

                return toConvertRate / baseRate
            }

            override fun onPostExecute(result: Float?) {
                super.onPostExecute(result)
                conversionRate.value = result
            }
        }

        GetConversionRateTask().execute()
        return conversionRate
    }
}