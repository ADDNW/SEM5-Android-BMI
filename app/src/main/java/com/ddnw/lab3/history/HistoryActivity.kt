package com.ddnw.lab3.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ddnw.lab3.R
import com.ddnw.lab3.databinding.ActivityHistoryBinding
import kotlinx.coroutines.runBlocking

class HistoryActivity : AppCompatActivity() {
    companion object {
        const val LOG_KEY = "HISTORY"
    }

    lateinit var binding: ActivityHistoryBinding
    lateinit var history: List<BmiHistoryEntry>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        history = readHistory()
        val rvHistory = findViewById<View>(R.id.rvHistory) as RecyclerView
        rvHistory.adapter = HistoryAdapter(
            history,
            getString(R.string.history_bmi),
            getString(R.string.history_params)
        )
        rvHistory.layoutManager = LinearLayoutManager(this)
    }

    private fun readHistory(): List<BmiHistoryEntry> {
        val context = this
        return runBlocking<List<BmiHistoryEntry>> {
            BmiHistoryDatabase.getInstance(context).historyDao().getAllSortByDate()
        }
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = history[position]
        holder.bmiTV.text = String.format(bmi_text, entry.bmi)
        holder.dataTV.text = entry.date
        holder.paramsTV.text = String.format(params_text, entry.mass, entry.height)
    }

    override fun getItemCount(): Int {
        return history.size
    }
}