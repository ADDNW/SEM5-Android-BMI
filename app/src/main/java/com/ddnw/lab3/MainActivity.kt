package com.ddnw.lab3

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText
import com.ddnw.lab3.bmi.Bmi
import com.ddnw.lab3.bmi.BmiCmKg
import com.ddnw.lab3.bmi.BmiInLb
import com.ddnw.lab3.databinding.ActivityMainBinding
import com.ddnw.lab3.detalis.BmiDetailsActivity
import com.ddnw.lab3.history.BmiHistoryDatabase
import com.ddnw.lab3.history.BmiHistoryEntry
import com.ddnw.lab3.history.HistoryActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val LOG_KEY = "Main"
        const val EU_UNITS = "eu"
    }

    var mode: String = EU_UNITS
    lateinit var binding: ActivityMainBinding
    var bmi: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_units -> {
                if (item.title == getString(R.string.units_eu)) {
                    modeChange("eu")
                    item.title = getString(R.string.units_us)
                } else {
                    modeChange("us")
                    item.title = getString(R.string.units_eu)
                }
                return true;
            }
            R.id.menu_history -> {
                showHistory()
                return true
            }

            else -> return false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble("BMI", bmi)
        outState.putString("MODE", mode)
        binding.apply {
            outState.putString("HEIGHT", heightET.text.toString())
            outState.putString("MASS", massET.text.toString())
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mode = savedInstanceState.getString("MODE", "en")
        bmi = savedInstanceState.getDouble("BMI")
        modeChange(mode)
        showBmi(bmi)
        binding.apply {
            heightET.setText(savedInstanceState.getString("HEIGHT", ""))
            massET.setText(savedInstanceState.getString("MASS", ""))
        }
    }

    fun count(view: View) {
        binding.apply {
            var dataValid = true
            val massMax: Int
            val massMin: Int
            val heightMax: Int
            val heightMin: Int

            if (mode == "eu") {
                massMax = 200
                massMin = 40
                heightMax = 200
                heightMin = 80
            } else {
                massMax = 440
                massMin = 90
                heightMax = 80
                heightMin = 30
            }

            if (massET.text.isBlank()) {
                dataValid = false
                showError(massET, getString(R.string.mass_is_empty))
            }

            if (heightET.text.isBlank()) {
                dataValid = false
                showError(heightET, getString(R.string.height_is_empty))
            }

            if (dataValid) {
                val mass = massET.text.toString().toDouble()
                val height = heightET.text.toString().toDouble()

                if (mass < massMin) {
                    dataValid = false
                    showError(massET, getString(R.string.mass_too_low))
                }

                if (mass > massMax) {
                    dataValid = false
                    showError(massET, getString(R.string.mass_too_high))
                }

                if (height < heightMin) {
                    dataValid = false
                    showError(heightET, getString(R.string.height_too_low))
                }

                if (height > heightMax) {
                    dataValid = false
                    showError(heightET, getString(R.string.height_too_high))
                }

                if (dataValid) {
                    val counter: Bmi = if (mode == "eu") {
                        BmiCmKg(mass, height)
                    } else {
                        BmiInLb(mass, height)
                    }
                    bmi = counter.count()
                    showBmi(bmi)
                    storeResultInHistory(bmi, mass, height)
                }
            }
        }
    }

    fun showDetails(view: View) {
        val intent = Intent(this, BmiDetailsActivity::class.java )
        intent.putExtra("BMI", bmi)
        intent.putExtra("COLOR", view.bmiTV.currentTextColor)
        startActivityForResult(intent, 0)
    }

    private fun showHistory() {
        val intent = Intent(this, HistoryActivity::class.java )
        startActivityForResult(intent, 0)
    }

    private fun modeChange(newMode: String) {
        mode = newMode;
        binding.apply {
            if (newMode == "eu") {
                massTV.text = getString(R.string.mass_kg)
                heightTV.text = getString(R.string.height_cm)
            } else {
                massTV.text = getString(R.string.mass_lb)
                heightTV.text = getString(R.string.height_in)
            }
        }
    }

    private fun showError(et: EditText, text: String) {
        et.error = text
    }

    private fun showBmi(value: Double){
        bmiTV.text = String.format("%.2f",value)
        when (true) {
            (value < 16) -> bmiTV.setTextColor(getColor(R.color.bmiDarkBlue))
            value < 17 -> bmiTV.setTextColor(getColor(R.color.bmiBlue))
            value < 18.5 -> bmiTV.setTextColor(getColor(R.color.bmiLightGreen))
            value < 25 -> bmiTV.setTextColor(getColor(R.color.bmiGreen))
            value < 30 -> bmiTV.setTextColor(getColor(R.color.bmiYellow))
            value < 35 -> bmiTV.setTextColor(getColor(R.color.bmiOrange))
            value < 40 -> bmiTV.setTextColor(getColor(R.color.bmiRed))
            else -> bmiTV.setTextColor(getColor(R.color.bmiDarkRed))
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun storeResultInHistory(result: Double, mass: Double, height: Double ) {
        val context = this
        runBlocking {
            val database = BmiHistoryDatabase.getInstance(context)
            val heightUnit: String
            val massUnit: String
            if (mode == EU_UNITS) {
                heightUnit = getString(R.string.unit_cm)
                massUnit = getString(R.string.unit_kg)
            } else {
                heightUnit = getString(R.string.unit_lb)
                massUnit = getString(R.string.unit_in)
            }

            val list = database.historyDao().getAllSortByDate()
            if (list.size == 10) {
                database.historyDao().delete(list.last())
            }

            val newEntry = BmiHistoryEntry(
                bmi = String.format("%.2f", result),
                mass =  String.format("%.2f %s", mass, massUnit),
                height =  String.format("%.2f %s", height, heightUnit),
                date =  SimpleDateFormat("HH:mm dd.MM.yyyy").format(Date())
            )

            Log.d(LOG_KEY, "Created newEntry with id: ${newEntry.id}")
            database.historyDao().insert(newEntry)
        }
    }


}
