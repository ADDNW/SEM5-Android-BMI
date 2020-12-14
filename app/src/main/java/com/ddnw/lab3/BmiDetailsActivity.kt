package com.ddnw.lab3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ddnw.lab3.databinding.ActivityBmiDetailsBinding
import kotlinx.android.synthetic.main.activity_bmi_details.*

class BmiDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityBmiDetailsBinding
    var bmi: Double = 0.0
    var color: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bmi = intent.getDoubleExtra("BMI", 0.0);
        color = intent.getIntExtra("COLOR", 0)
        setMonsterData(bmi)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putDouble("BMI", bmi)
        outState.putInt("COLOR", color)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        bmi = savedInstanceState.getDouble("BMI")
        color = savedInstanceState.getInt("COLOR")
        setMonsterData(bmi)
    }

    private fun setMonsterData(bmi: Double) {
        binding.apply {
            bmiDetailsTV.text = String.format("%.2f", bmi)
            bmiDetailsTV.setTextColor(color)
            when (true) {
                (bmi < 16) -> {
                    monsterNameTV.text = getString(R.string.skeleton_name)
                    monsterDescriptionTV.text = getString(R.string.skeleton_description)
                }
                bmi < 17 -> {
                    monsterNameTV.text = getString(R.string.ghoul_name)
                    monsterDescriptionTV.text = getString(R.string.ghoul_description)
                }
                bmi < 18.5 -> {
                    monsterNameTV.text = getString(R.string.zombie_name)
                    monsterDescriptionTV.text = getString(R.string.zombie_description)
                }
                bmi < 25 -> {
                    monsterNameTV.text = getString(R.string.necromancer_name)
                    monsterDescriptionTV.text = getString(R.string.necromancer_description)
                }
                bmi < 30 -> {
                    monsterNameTV.text = getString(R.string.death_warrior_name)
                    monsterDescriptionTV.text = getString(R.string.death_warrior_description)
                }
                bmi < 35 -> {
                    monsterNameTV.text = getString(R.string.werewolf_name)
                    monsterDescriptionTV.text = getString(R.string.werewolf_description)
                }
                bmi < 40 -> {
                    monsterNameTV.text = getString(R.string.frankenstein_name)
                    monsterDescriptionTV.text = getString(R.string.frankenstein_description)
                }
                else -> {
                    monsterNameTV.text = getString(R.string.stitches_name)
                    monsterDescriptionTV.text = getString(R.string.stitches_description)
                }
            }
        }
    }
}