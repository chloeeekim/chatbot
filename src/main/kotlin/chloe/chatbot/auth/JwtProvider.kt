package chloe.chatbot.auth

import chloe.chatbot.domain.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtProvider(
    @Value("\${jwt.secret}") secretKey: String,
    @Value("\${jwt.access_expiration_time}") accessTokenExpTime: Long,
    private val userDetailService: CustomUserDetailService
) {

    private val secretKey: Key
    private val accessTokenExpTime: Long

    init {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        this.secretKey = Keys.hmacShaKeyFor(keyBytes)
        this.accessTokenExpTime = accessTokenExpTime
    }

    fun generateAccessToken(user: User): String {
        val now = System.currentTimeMillis()
        val claims = Jwts.claims().setSubject(user.email)
        claims["role"] = user.role.name

        return Jwts.builder().apply {
            setHeader(createHeader())
            setClaims(claims)
            setIssuedAt(Date(now))
            setExpiration(Date(now + accessTokenExpTime))
            signWith(secretKey, SignatureAlgorithm.HS256)
        }.compact()
    }

    fun getUsername(token: String): String {
        return parseClaims(token).subject
    }

    fun parseClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }

    fun isTokenExpired(token: String): Boolean {
        try {
            return parseClaims(token).expiration?.before(Date())
                ?: return true
        } catch (e: ExpiredJwtException) {
            return true
        }
    }

    fun isValidToken(token: String): Boolean {
        return try {
            parseClaims(token)
            true
        } catch(e: Exception) {
            false
        }
    }

    private fun createHeader(): MutableMap<String, Any> {
        val header: MutableMap<String, Any> = HashMap<String, Any>()
        header["typ"] = "JWT"
        header["alg"] = "HS256"
        return header
    }
}