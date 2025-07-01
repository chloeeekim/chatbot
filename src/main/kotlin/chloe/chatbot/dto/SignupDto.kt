package chloe.chatbot.dto

data class SignupRequest(
    val email: String,
    val password: String,
    val name: String
)