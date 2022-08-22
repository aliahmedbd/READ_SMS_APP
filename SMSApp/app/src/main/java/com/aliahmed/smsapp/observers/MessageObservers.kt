package com.aliahmed.smsapp.observers

import android.content.Context
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.provider.Telephony
import android.util.Log
import com.aliahmed.smsapp.model.MessageType
import com.aliahmed.smsapp.model.SMSModel
import com.aliahmed.smsapp.utils.AppUtils
import org.koin.java.KoinJavaComponent.inject
import java.util.*

private const val KNOX_PROVIDER = "com.samsung.android.knox.deployment.app.comproxy.provider"

class MessageObservers(handler: Handler?) : ContentObserver(handler) {

    private val context: Context by inject(Context::class.java)

    override fun onChange(selfChange: Boolean, uri: Uri?) {
         handleOnChange(uri)
        super.onChange(selfChange, uri)
    }

    override fun onChange(selfChange: Boolean, uris: MutableCollection<Uri>, flags: Int) {
        super.onChange(selfChange, uris, flags)
    }


    private fun handleOnChange(uri: Uri?) {
        val concreteURI = uri?.let { wrapUri(it) }
        concreteURI?.let { buildWithTypeSMS(it) }
    }


    private fun wrapUri(uri: Uri): Uri? {
        if (!AppUtils.isSamsung()) {
            return uri
        }
        return if ("sms" != uri.authority && "mms" != uri.authority) {
            uri
        } else Uri.Builder()
            .scheme("content")
            .authority(KNOX_PROVIDER)
            .appendPath(uri.authority)
            .appendPath(uri.path)
            .build()
    }

    private fun buildWithTypeSMS(uri: Uri) {
        val smsModel: SMSModel = SMSModel()
        val contentResolver = context.contentResolver
        try {
            val cur: Cursor? =
                contentResolver.query(uri, null, null, null, Telephony.Sms.DEFAULT_SORT_ORDER)
            if (cur == null || !cur.moveToFirst()) {
                return
            }
            when (cur.getInt(cur.getColumnIndex(Telephony.Sms.TYPE))) {
                Telephony.Sms.MESSAGE_TYPE_INBOX -> smsModel.lastMessageType = MessageType.INBOX
                Telephony.Sms.MESSAGE_TYPE_SENT -> smsModel.lastMessageType = MessageType.SENT
            }
            smsModel.lastMessageDate =
                Date(cur.getLong(cur.getColumnIndex(Telephony.Sms.DATE))).toString()
            smsModel.lastMessage = cur.getString(cur.getColumnIndex(Telephony.Sms.BODY))
            smsModel.number = cur.getString(cur.getColumnIndex(Telephony.Sms.ADDRESS))
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }

    }
}