package com.ddnw.lab3

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ddnw.lab3.databinding.ActivityHistoryBinding
import com.ddnw.lab3.history.BmiHistoryEntry
import java.lang.NumberFormatException

class HistoryActivity : AppCompatActivity() {
    companion object {
        val LOG_KEY = "HISTORY"
        val SHARED_NAME = "bmi-history"
    }

    lateinit var binding: ActivityHistoryBinding
    lateinit var history: ArrayList<BmiHistoryEntry>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        history = readHistory()
        val rvHistory = findViewById<View>(R.id.rvHistory) as RecyclerView
        rvHistory.adapter = HistoryAdapter(history, getString(R.string.history_bmi), getString(R.string.history_params))
        rvHistory.layoutManager = LinearLayoutManager(this)
    }

    private fun readHistory(): ArrayList<BmiHistoryEntry> {
        val sharedPref: SharedPreferences = getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)
        val history = ArrayList<BmiHistoryEntry>()
        Log.d(LOG_KEY, "Starting")
        for (i in 0 until 10) {
            var raw = sharedPref.getString("history_$i", "")
            Log.d(LOG_KEY, "$i value = $raw" )
            if (raw != null) {
                val entry = parseHistoryEntry(raw)
                if (entry != null) {
                    history.add(entry)
                    Log.d(LOG_KEY, "$i: ${entry.bmi}, ${entry.mass}, ${entry.height}, ${entry.data}")
                }
            }

        }
        return history
    }

    private fun parseHistoryEntry(raw: String) :BmiHistoryEntry? {
        val params = raw.split(";")
        Log.d(LOG_KEY, "Params: ${params.size}" )
        if (params.size != 4 ) return null
        return BmiHistoryEntry(params[0], params[1], params[2], params[3])
    }
}

class HistoryAdapter (private val history: List<BmiHistoryEntry>, private val bmi_text: String, private val params_text: String): RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    inner class ViewHolder(lintItemView: View) : RecyclerView.ViewHolder(lintItemView) {
        val bmiTV = itemView.findViewById<TextView>(R.id.EntryBmiTV)
        val paramsTV = itemView.findViewById<TextView>(R.id.EntryParamsTV)
        val dataTV = itemView.findViewById<TextView>(R.id.EntryDataTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val historyView = inflater.inflate(R.layout.history_row, parent, false)
        return ViewHolder(historyView)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
        val entry = history[position]
        holder.bmiTV.text = String.format(bmi_text, entry.bmi)
        holder.dataTV.text = entry.data
        holder.paramsTV.text = String.format(params_text, entry.mass, entry.height)
    }

    override fun getItemCount(): Int {
        return history.size
    }
}