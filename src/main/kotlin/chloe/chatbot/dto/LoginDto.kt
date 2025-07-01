package chloe.chatbot.dto

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val name: String
)