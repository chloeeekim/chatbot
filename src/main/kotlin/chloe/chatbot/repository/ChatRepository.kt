package chloe.chatbot.repository

import chloe.chatbot.domain.Chat
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ChatRepository : JpaRepository<Chat, UUID>, ChatQueryRepository {

    fun findAllByThreadIdOrderByCreatedAtAsc(threadId: UUID): List<Chat>


}