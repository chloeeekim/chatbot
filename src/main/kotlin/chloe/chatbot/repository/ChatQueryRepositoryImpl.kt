package chloe.chatbot.repository

import chloe.chatbot.domain.Chat
import chloe.chatbot.domain.QChat
import com.querydsl.jpa.impl.JPAQueryFactory
import org.hibernate.query.SortDirection
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import java.util.*

class ChatQueryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : ChatQueryRepository {
    override fun findChatsByThreadId(threadId: UUID, pageable: Pageable, sortDirection: Sort.Direction): Page<Chat> {
        val qChat = QChat.chat

        val chats = queryFactory
            .selectFrom(qChat)
            .where(
                qChat.thread.id.eq(threadId)
            )
            .orderBy(
                if (sortDirection == Sort.Direction.ASC) qChat.createdAt.asc()
                else qChat.createdAt.desc()
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(qChat.count())
            .from(qChat)
            .where(qChat.thread.id.eq(threadId))
            .fetchOne() ?: 0L

        return PageImpl(chats, pageable, total)
    }
}