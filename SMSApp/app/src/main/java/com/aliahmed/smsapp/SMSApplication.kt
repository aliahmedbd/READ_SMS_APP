package com.aliahmed.smsapp

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.provider.Telephony
import com.aliahmed.smsapp.di.appModule
import com.aliahmed.smsapp.observers.MessageObservers
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class SMSApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        initialiseMessageObserver()
    }

    private fun initKoin(){
        startKoin {
            androidContext(this@SMSApplication)
            androidLogger(Level.INFO)
            modules(listOf(appModule))
        }
    }

    private fun initialiseMessageObserver(){
        val contentResolver = contentResolver
        val messageObservers = MessageObservers(Handler(Looper.myLooper()!!))
        contentResolver.registerContentObserver(Telephony.Sms.CONTENT_URI, true, messageObservers)
    }
}