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
import mozilla.components.concept.engine.EngineView
import mozilla.components.concept.engine.Engine

class MainActivity : AppCompatActivity() {

    private val engine : Engine by lazy{
        SystemEngine(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val session = engine.createSession()
        engineView.render(session)
        session.loadUrl("https://mozilla.org")
    }

    override fun onCreateView(parent: View?, name: String?, context: Context, attrs: AttributeSet?): View? =
        when (name) {
            EngineView::class.java.name -> engine.createView(context, attrs).asView()
            else -> super.onCreateView(parent, name, context, attrs)
        }
}
