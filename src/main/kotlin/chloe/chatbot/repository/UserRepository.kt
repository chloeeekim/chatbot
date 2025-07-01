package chloe.chatbot.repository

import chloe.chatbot.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository: JpaRepository<User, UUID> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): User?
}