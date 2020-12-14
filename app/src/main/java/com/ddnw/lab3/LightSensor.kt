package com.ddnw.lab3

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class LightSensor(private val context: Context) : SensorEventListener {
    companion object {
        const val LOG_KEY = "LightSensor"
        const val DARK_MODE_BORDER = 20.0

        const val LIGHT_MODE = 0
        const val DARK_MODE = 1
    }
    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val lightSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    private var lightMode: Int = LIGHT_MODE

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            Log.d(LOG_KEY, "Mesuured: ${event.values[0]}")
            val newMode = if (event.values[0] < DARK_MODE_BORDER) DARK_MODE else LIGHT_MODE
            if (newMode != lightMode) {
                when (newMode) {
                    LIGHT_MODE -> context.setTheme(R.style.AppTheme_Light)
                    DARK_MODE -> context.setTheme(R.style.AppTheme_Dark)
                }
                context.s
                lightMode = newMode
                Log.d(LOG_KEY, "Changed mode to: $lightMode")
            }
        }
    }

    fun watch() {
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stopWatching() {
        sensorManager.unregisterListener(this)
    }
}