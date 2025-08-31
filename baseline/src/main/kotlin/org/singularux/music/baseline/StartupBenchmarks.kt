package org.singularux.music.baseline

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.uiAutomator
import androidx.test.uiautomator.watcher.PermissionDialog
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class StartupBenchmarks {

    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun startupCompilationNone() =
        benchmark(CompilationMode.None())

    @Test
    fun startupCompilationBaselineProfiles() =
        benchmark(CompilationMode.Partial(BaselineProfileMode.Require))

    private fun benchmark(compilationMode: CompilationMode) {
        rule.measureRepeated(
            packageName = "org.singularux.music",
            metrics = listOf(StartupTimingMetric()),
            compilationMode = compilationMode,
            startupMode = StartupMode.COLD,
            iterations = 10,
            setupBlock = { pressHome() },
            measureBlock = {
                startActivityAndWait()
                uiAutomator {
                    // Accept the permissions
                    watchFor(PermissionDialog) { clickAllow() }
                    // Click on first track
                    onElementOrNull { viewIdResourceName == "track_item_0" }?.click()
                    // Click on search bar and return back
                    onElement { viewIdResourceName == "track_list_search" }.click()
                    pressBack()
                    // Scroll list of tracks
                    onElement { viewIdResourceName == "track_list_content" }
                        .scroll(Direction.DOWN, 1F)
                    // Click on bottom bar and go to playback screen
                    onElement { viewIdResourceName == "track_list_bottom_bar" }
                        .click()
                }
            }
        )
    }
}