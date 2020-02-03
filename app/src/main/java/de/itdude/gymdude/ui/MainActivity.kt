/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.itdude.gymdude.ui

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import dagger.android.support.DaggerAppCompatActivity
import de.itdude.gymdude.R
import io.realm.Realm
import javax.inject.Inject

class MainActivity: DaggerAppCompatActivity() {

    @Suppress("ProtectedInFinal")
    @Inject
    protected lateinit var db: Realm
    val clearFocusViews = ArrayList<View>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setContentView(R.layout.activity_main)
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFocusViews.clear()
        db.close()
    }

    override fun dispatchTouchEvent(ev: MotionEvent) = super.dispatchTouchEvent(ev).also {
        clearFocusViews.removeAll { view ->
            if (view.isAttachedToWindow) {
                val rect = Rect()
                view.getGlobalVisibleRect(rect)
                if (!rect.contains(ev.x.toInt(), ev.y.toInt())) {
                    view.clearFocus()
                }
                false
            } else {
                true
            }
        }
    }

}
