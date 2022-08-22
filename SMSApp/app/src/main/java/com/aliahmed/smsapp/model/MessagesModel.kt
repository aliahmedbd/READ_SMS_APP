package com.aliahmed.smsapp.model

data class MessagesModel(
    val messageBody: String,
    val date: String,
    val isSuccess: String,
    val type: MessageType
)
