/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.simple.browser

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import mozilla.components.browser.engine.system.SystemEngine
import mozilla.components.browser.session.Session
import mozilla.components.browser.session.SessionManager
import mozilla.components.concept.engine.EngineView
import mozilla.components.feature.session.SessionFeature
import mozilla.components.feature.session.SessionUseCases
import mozilla.components.feature.toolbar.ToolbarFeature

class MainActivity : AppCompatActivity() {

    private val engine = SystemEngine(this)

    private lateinit var toolbarFeature: ToolbarFeature
    private lateinit var sessionFeature: SessionFeature
    private val sessionManager = SessionManager(engine)
    private val sessionUseCases = SessionUseCases(sessionManager)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}
