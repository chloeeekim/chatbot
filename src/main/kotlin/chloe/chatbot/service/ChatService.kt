package chloe.chatbot.service

import chloe.chatbot.domain.Chat
import chloe.chatbot.dto.ChatRequest
import chloe.chatbot.dto.ChatResponse
import chloe.chatbot.repository.ChatRepository
import chloe.chatbot.repository.ThreadRepository
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.ChatOptions
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.openai.api.OpenAiApi
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.AccessDeniedException
import org.springframework.stereotype.Service
import java.util.*

@Service
class ChatService(
    private val chatRepository: ChatRepository,
    private val threadRepository: ThreadRepository,
    private val openAiApi: OpenAiApi
) {

    fun getChatsByThread(threadId: UUID, userId: UUID, page: Int, size: Int, sort: String): Page<ChatResponse> {
        if (!threadRepository.existsByIdAndUserId(threadId, userId)) {
            throw AccessDeniedException("해당 스레드에 접근할 권한이 없습니다.")
        }

        val pageable = PageRequest.of(page, size)
        val sortDir = if (sort.lowercase() == "asc") Sort.Direction.ASC else Sort.Direction.DESC
        return chatRepository.findChatsByThreadId(threadId, pageable, sortDir).map { ChatResponse.fromEntity(it) }
    }

    fun makeChat(threadId: UUID, request: ChatRequest, userId: UUID): ChatResponse? {
        if (!threadRepository.existsByIdAndUserId(threadId, userId)) {
            throw AccessDeniedException("해당 스레드에 접근할 권한이 없습니다.")
        }
        val thread = threadRepository.findByIdOrNull(threadId)
            ?: throw IllegalArgumentException("Thread not found.")

        try {
            val message = listOf(
                UserMessage(request.question)
            )
            val chatOptions = ChatOptions.builder()
                .model(request.model)
                .build()
            val prompt = Prompt(message, chatOptions)

            val chatModel = OpenAiChatModel.builder()
                .openAiApi(openAiApi)
                .build()

            val answer = chatModel.call(prompt).toString()

            val save = Chat(request.question, answer, thread)

            chatRepository.save(save)
            return ChatResponse.fromEntity(save)
        } catch (e: Exception) {
            return null
        }
    }
}