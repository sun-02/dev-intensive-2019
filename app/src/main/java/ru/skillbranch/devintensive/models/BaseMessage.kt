package ru.skillbranch.devintensive.models

import java.lang.IllegalArgumentException
import java.util.*

abstract class BaseMessage(
    val id: String,
    val from: User?,
    val chat: Chat,
    val isIncoming: Boolean = false,
    val date: Date = Date()
) {
    abstract fun formatMessage(): String

    companion object AbstractFactory {
        var lastId = -1
        fun makeMessage(
            from: User?,
            chat: Chat,
            date: Date = Date(),
            type: String = "text",
            payload: Any?,
            isIncoming: Boolean = false
        ): BaseMessage {
            lastId++
            return when (type) {
                "text" -> TextMessage(
                    "$lastId",
                    from,
                    chat,
                    date = date,
                    text = payload as String,
                    isIncoming = isIncoming
                )
                "image" -> ImageMessage(
                    "$lastId",
                    from,
                    chat,
                    date = date,
                    image = payload as String,
                    isIncoming = isIncoming
                )
                else -> throw IllegalArgumentException("Type must be \"text\" or \"image\"")
            }
        }
    }
}

//enum class MessageType {
//    IMAGE,
//    TEXT
//}
