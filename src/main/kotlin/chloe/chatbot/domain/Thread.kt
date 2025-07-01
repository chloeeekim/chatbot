package chloe.chatbot.domain

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "threads")
class Thread(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User
) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private val id: UUID? = null

    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.now()
}