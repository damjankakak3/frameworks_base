/*
 * Copyright (C) 2023-2024 The risingOS Android Project
 * Copyright (C) 2025 The AxionAOSP Android Project
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
 * limitations under the License
 */
package com.android.systemui.statusbar

import android.content.Context
import android.net.wifi.ScanResult
import android.util.AttributeSet
import android.view.ViewGroup.MarginLayoutParams
import android.widget.ImageView

import com.android.systemui.res.R

class WifiStandardImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        WifiStandardController.INSTANCE(context).attachView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        WifiStandardController.INSTANCE(context).detachView()
    }

    fun updateWifiStatus(wifiStandard: Int, wifiStandardEnabled: Boolean) {
        post {
            // Note the ScanResult values: 11AD (7) is 60 GHz WiGig, NOT Wi-Fi 7.
            // Wi-Fi 7 is 11BE (8). The previous mapping labelled WiGig as Wi-Fi 7
            // and never showed anything for real Wi-Fi 7 hardware.
            val drawableId = if (!wifiStandardEnabled) 0 else when (wifiStandard) {
                ScanResult.WIFI_STANDARD_11N -> R.drawable.ic_wifi_standard_4
                ScanResult.WIFI_STANDARD_11AC -> R.drawable.ic_wifi_standard_5
                ScanResult.WIFI_STANDARD_11AX -> R.drawable.ic_wifi_standard_6
                WifiStandardController.WIFI_STANDARD_11AX_6GHZ -> R.drawable.ic_wifi_standard_6e
                ScanResult.WIFI_STANDARD_11BE -> R.drawable.ic_wifi_standard_7
                else -> 0
            }

            if (drawableId == 0) {
                visibility = GONE
                layoutParams = (layoutParams as MarginLayoutParams).apply { marginEnd = 0 }
            } else {
                setImageResource(drawableId)
                visibility = VISIBLE
                layoutParams = (layoutParams as MarginLayoutParams).apply {
                    marginEnd = resources.getDimensionPixelSize(R.dimen.status_bar_airplane_spacer_width)
                }
            }
        }
    }
}
