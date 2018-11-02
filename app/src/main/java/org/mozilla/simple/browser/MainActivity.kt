/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.simple.browser

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import mozilla.components.browser.engine.gecko.GeckoEngine
import mozilla.components.browser.menu.BrowserMenuBuilder
import mozilla.components.browser.menu.item.BrowserMenuItemToolbar
import mozilla.components.browser.session.Session
import mozilla.components.browser.session.SessionManager
import mozilla.components.concept.engine.Engine
import mozilla.components.concept.engine.EngineView
import mozilla.components.feature.session.SessionFeature
import mozilla.components.feature.session.SessionUseCases
import mozilla.components.feature.toolbar.ToolbarFeature

class MainActivity : AppCompatActivity() {

    private val engine: Engine by lazy {
        GeckoEngine(this)
    }

    private lateinit var toolbarFeature: ToolbarFeature
    private lateinit var sessionFeature: SessionFeature
    private lateinit var sessionManager: SessionManager
    private lateinit var sessionUseCases: SessionUseCases

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = SessionManager(engine)
        sessionUseCases = SessionUseCases(sessionManager)
        toolbar.setMenuBuilder(getBrowserMenuBuilder())

        sessionFeature = SessionFeature(
            sessionManager,
            sessionUseCases,
            engineView
        )

        toolbarFeature = ToolbarFeature(
            toolbar,
            sessionManager,
            sessionUseCases.loadUrl
        )

        //Adding a default session
        sessionManager.add(Session("https://www.mozilla.org"))

    }

    override fun onStart() {
        super.onStart()
        toolbarFeature.start()
        sessionFeature.start()
    }

    override fun onStop() {
        super.onStop()
        toolbarFeature.stop()
        sessionFeature.stop()
    }

    override fun onCreateView(parent: View?, name: String?, context: Context, attrs: AttributeSet?): View? =
        when (name) {
            EngineView::class.java.name -> engine.createView(context, attrs).asView()
            else -> super.onCreateView(parent, name, context, attrs)
        }

    private fun getBrowserMenuBuilder(): BrowserMenuBuilder {
        return BrowserMenuBuilder(listOf(getBrowserMenuItemToolbar()))
    }

    private fun getBrowserMenuItemToolbar(): BrowserMenuItemToolbar {

        val back = BrowserMenuItemToolbar.Button(
            mozilla.components.ui.icons.R.drawable.mozac_ic_back,
            iconTintColorResource = R.color.photonBlue90,
            contentDescription = "Back"
        ) {
            sessionUseCases.goBack.invoke()
        }

        val forward = BrowserMenuItemToolbar.Button(
            mozilla.components.ui.icons.R.drawable.mozac_ic_forward,
            iconTintColorResource = R.color.photonBlue90,
            contentDescription = "Forward"
        ) {
            sessionUseCases.goForward.invoke()
        }

        val refresh = BrowserMenuItemToolbar.Button(
            mozilla.components.ui.icons.R.drawable.mozac_ic_refresh,
            iconTintColorResource = R.color.photonBlue90,
            contentDescription = "Refresh"
        ) {
            sessionUseCases.reload.invoke()
        }

        val stop = BrowserMenuItemToolbar.Button(
            mozilla.components.ui.icons.R.drawable.mozac_ic_stop,
            iconTintColorResource = R.color.photonBlue90,
            contentDescription = "Stop"
        ) {
            sessionUseCases.stopLoading.invoke()
        }

        return BrowserMenuItemToolbar(listOf(back, forward, refresh, stop))
    }
}
