package chloe.chatbot.dto

import chloe.chatbot.domain.Chat
import java.time.LocalDateTime
import java.util.*

data class ChatRequest(
    val question: String,
    val isStreaming: Boolean = false,
    val model: String? = "gpt-4o"
)

data class ChatResponse(
    val question: String,
    val answer: String,
    val createdAt: LocalDateTime
) {
    companion object {
        @JvmStatic
        fun fromEntity(chat: Chat): ChatResponse {
            return ChatResponse(
                question = chat.question,
                answer = chat.answer,
                createdAt = chat.createdAt
            )
        }
    }
}