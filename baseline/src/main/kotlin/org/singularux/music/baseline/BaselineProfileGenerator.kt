package org.singularux.music.baseline

import androidx.benchmark.macro.junit4.BaselineProfileRule
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
            /*  TODO: Fix this not working
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
            */
        }
    }
}