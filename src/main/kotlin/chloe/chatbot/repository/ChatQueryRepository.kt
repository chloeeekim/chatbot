package chloe.chatbot.repository

import chloe.chatbot.domain.Chat
import org.hibernate.query.SortDirection
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.util.UUID

interface ChatQueryRepository {
    fun findChatsByThreadId(
        threadId: UUID,
        pageable: Pageable,
        sortDirection: Sort.Direction
    ): Page<Chat>
}