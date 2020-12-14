package com.ddnw.lab3

import com.ddnw.lab3.bmi.BmiCmKg
import org.junit.Assert.assertEquals
import org.junit.Test

class BmiCmKgTest {
    @Test
    fun call_count_with_0_mass() {
        val counter = BmiCmKg(0.0, 10.0)
        assertEquals(0.0, counter.count(), 0.01)
    }

    @Test
    fun call_count_with_0_height() {
        val counter = BmiCmKg(10.0, 0.0)
        assertEquals(0.0, counter.count(), 0.01)
    }

    @Test
    fun call_count_with_negative_mass() {
        val counter = BmiCmKg(-10.0, 10.0)
        assertEquals(0.0, counter.count(), 0.01)
    }

    @Test
    fun call_count_with_negative_height() {
        val counter = BmiCmKg(10.0, -10.0)
        assertEquals(0.0, counter.count(), 0.01)
    }

    @Test
    fun call_count_with_positive_parameters() {
        val counter = BmiCmKg(80.0, 200.0)
        assertEquals(20.0, counter.count(), 0.01)
    }
}
