package org.singularux.music.baseline

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class BaselineProfileGenerator {

    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() {
        rule.collect(
            packageName = "org.singularux.music",
            includeInStartupProfile = false
        ) {
            pressHome()
            startActivityAndWait()
            // TODO Write more interactions to optimize advanced journeys of your app.
        }
    }
}