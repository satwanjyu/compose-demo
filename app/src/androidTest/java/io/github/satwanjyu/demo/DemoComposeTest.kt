package io.github.satwanjyu.demo

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DemoComposeTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun runsWithoutCrashing() {
        val client = buildClient()
        composeTestRule.setContent {
            App(client = client)
        }
    }
}