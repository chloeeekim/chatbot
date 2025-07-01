package chloe.chatbot.domain

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "chats")
class Chat(
    var question: String,
    var answer: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id", nullable = false)
    var thread: Thread
) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private val id: UUID? = null

    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.now()
}