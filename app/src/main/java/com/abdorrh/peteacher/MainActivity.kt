package com.abdorrh.peteacher

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent

/** Opens the site in Chrome's browser context so Google sign-in shares its session. */
class MainActivity : Activity() {
    private val startUrl = "https://rhpeman-z8yc.whacka.app/#/calendar?tab=schedule"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val customTabs = CustomTabsIntent.Builder()
            .setShowTitle(true)
            .build()
        customTabs.launchUrl(this, Uri.parse(startUrl))
    }
}
