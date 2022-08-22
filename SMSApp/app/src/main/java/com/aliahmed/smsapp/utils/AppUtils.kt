package com.aliahmed.smsapp.utils

import android.os.Build

object AppUtils {

    fun isSamsung(): Boolean {
        return "samsung".equals(Build.MANUFACTURER, ignoreCase = true)
    }

}