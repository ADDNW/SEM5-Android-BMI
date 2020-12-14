package com.ddnw.lab3

import com.ddnw.lab3.bmi.BmiInLb
import org.junit.Assert.assertEquals
import org.junit.Test

class BmiInLbTest {
    @Test
    fun call_count_with_0_mass() {
        val counter = BmiInLb(0.0, 10.0)
        assertEquals(0.0, counter.count(), 0.01)
    }

    @Test
    fun call_count_with_0_height() {
        val counter = BmiInLb(10.0, 0.0)
        assertEquals(0.0, counter.count(), 0.01)
    }

    @Test
    fun call_count_with_negative_mass() {
        val counter = BmiInLb(-10.0, 10.0)
        assertEquals(0.0, counter.count(), 0.01)
    }

    @Test
    fun call_count_with_negative_height() {
        val counter = BmiInLb(10.0, -10.0)
        assertEquals(0.0, counter.count(), 0.01)
    }

    @Test
    fun call_count_with_positive_parameters() {
        val counter = BmiInLb(80.0, 2.0)
        assertEquals(14060.0, counter.count(), 0.01)
    }
}
