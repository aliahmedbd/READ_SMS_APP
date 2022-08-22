package com.aliahmed.smsapp.model

data class SMSModel(
    var number: String? = null,
    var lastMessage: String? = null,
    var lastMessageDate: String? = null,
    var lastMessageType: MessageType? = null,
    var message: List<MessagesModel>? = null
)
