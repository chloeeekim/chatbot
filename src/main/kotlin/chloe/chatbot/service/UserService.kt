package chloe.chatbot.service

import chloe.chatbot.auth.JwtProvider
import chloe.chatbot.domain.User
import chloe.chatbot.dto.LoginRequest
import chloe.chatbot.dto.LoginResponse
import chloe.chatbot.dto.SignupRequest
import chloe.chatbot.repository.UserRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtProvider: JwtProvider
) {
    fun signUp(request: SignupRequest): UUID {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email already taken")
        }

        val user = User(
            email = request.email,
            password = passwordEncoder.encode(request.password),
            name = request.name
        )

        return requireNotNull(userRepository.save(user).id) { "User ID must not be null" }
    }

    fun login(loginRequest: LoginRequest, request: HttpServletRequest, response: HttpServletResponse): LoginResponse {
        val user = userRepository.findByEmail(loginRequest.email)
            ?: throw IllegalArgumentException("User not found")

        require(passwordEncoder.matches(loginRequest.password, user.password)) {
            throw IllegalArgumentException("Invalid password")
        }

        val accessToken = jwtProvider.generateAccessToken(user)
        response.setHeader("Authorization", "Bearer $accessToken")

        return LoginResponse(user.name)
    }
}