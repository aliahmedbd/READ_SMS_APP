package com.aliahmed.smsapp.di

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    single { androidApplication().applicationContext }
}