package chloe.chatbot.controller

import chloe.chatbot.auth.CustomUserDetails
import chloe.chatbot.dto.ChatRequest
import chloe.chatbot.dto.ChatResponse
import chloe.chatbot.service.ChatService
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/chats")
class ChatController(
    private val chatService: ChatService
) {
    @GetMapping("/{id}")
    fun getChatsByThreadId(
        @PathVariable id: UUID,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "desc") sort: String,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ): ResponseEntity<Page<ChatResponse>> {
        val userId = userDetails.getUserId()
        val result = chatService.getChatsByThread(id, userId, page, size, sort)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/{id}")
    fun makeChat(
        @PathVariable id: UUID,
        @RequestBody request: ChatRequest,
        @AuthenticationPrincipal userDetails: CustomUserDetails
    ): ResponseEntity<ChatResponse?> {
        val userId = userDetails.getUserId()
        val result = chatService.makeChat(id, request, userId)

        result?.let {
            return ResponseEntity.ok(result)
        } ?: run {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build()
        }
    }
}