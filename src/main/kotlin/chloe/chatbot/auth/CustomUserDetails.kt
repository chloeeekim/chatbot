package chloe.chatbot.auth

import chloe.chatbot.domain.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

class CustomUserDetails(
    private val user: User
) : UserDetails {

    override fun getAuthorities(): List<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))
    }

    override fun getPassword(): String? = user.password
    override fun getUsername(): String = user.email
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
    fun getUserId(): UUID = user.id!!
}