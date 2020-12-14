package com.ddnw.lab3.bmi

import kotlin.math.pow

class BmiInLb(var mass: Double, var height: Double): Bmi {
    override fun count(): Double {
        if (height <= 0 || mass <= 0 ) return -0.0
        return (mass/height.pow(2)) * 703;
    }
}