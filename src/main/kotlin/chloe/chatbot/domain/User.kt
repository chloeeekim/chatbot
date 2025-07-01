package chloe.chatbot.domain

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "users")
class User(
    var email: String,

    var password: String,

    var name: String,

    @Enumerated(EnumType.STRING)
    var role: Role = Role.MEMBER
) {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    val id: UUID? = null

    @CreationTimestamp
    val createdAt: LocalDateTime = LocalDateTime.now()
}

enum class Role {
    MEMBER, ADMIN
}