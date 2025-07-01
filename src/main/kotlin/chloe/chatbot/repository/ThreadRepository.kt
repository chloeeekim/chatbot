package chloe.chatbot.repository

import chloe.chatbot.domain.Thread
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ThreadRepository: JpaRepository<Thread, UUID> {
    fun existsByIdAndUserId(threadId: UUID, userId: UUID): Boolean
}