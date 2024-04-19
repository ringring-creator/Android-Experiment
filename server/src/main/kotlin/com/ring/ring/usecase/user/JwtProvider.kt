package com.ring.ring.usecase.user

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.ring.ring.data.User
import java.util.Date

object JwtProvider {
    private const val validityInMs = 36_000_00 * 10 // 10 hours
    private const val ISSUER = "android-experiment"
    const val AUDIENCE = "android-experiment"
    const val SECRET = "secret"
    val ALGORITHM = Algorithm.HMAC256(SECRET)

    val verifier: JWTVerifier = JWT
        .require(ALGORITHM)
        .withAudience(AUDIENCE)
        .withIssuer(ISSUER)
        .build()

    fun createJWT(user: User): String =
        JWT.create()
            .withIssuedAt(Date())
            .withSubject("Authentication")
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("email", user.email)
            .withExpiresAt(Date(System.currentTimeMillis() + validityInMs))
            .sign(ALGORITHM)
}
