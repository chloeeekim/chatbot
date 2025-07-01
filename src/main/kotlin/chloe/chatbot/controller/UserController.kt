package chloe.chatbot.controller

import chloe.chatbot.dto.LoginRequest
import chloe.chatbot.dto.LoginResponse
import chloe.chatbot.dto.SignupRequest
import chloe.chatbot.service.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/signup")
    fun signUp(@RequestBody request: SignupRequest) : ResponseEntity<String> {
        userService.signUp(request)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body("Signup Success")
    }

    @PostMapping("login")
    fun login(@RequestBody loginRequest: LoginRequest, request: HttpServletRequest, response: HttpServletResponse) : ResponseEntity<LoginResponse> {
        val loginResponse = userService.login(loginRequest, request, response)
        return ResponseEntity.ok(loginResponse)
    }
}