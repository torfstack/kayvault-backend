package de.torfstack.kayvault.controller

import com.nimbusds.jwt.SignedJWT
import de.torfstack.kayvault.persistence.SecretService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
class SecretController(val secretService: SecretService) {

    @GetMapping("secret")
    fun getSecret(@RequestHeader authorization: String): List<String> {
        val jwt = jwtFromHeader(authorization)
        val user = jwt.jwtClaimsSet.subject
        return secretsForUser(user)
    }

    @PostMapping("secret")
    fun postSecret(@RequestHeader authorization: String, @RequestBody entity: PostSecretRequestEntity): List<String> {
        val jwt = jwtFromHeader(authorization)
        val user = jwt.jwtClaimsSet.subject
        secretService.addSecretForUser(user, entity.value)
        return secretsForUser(user)
    }

    private fun jwtFromHeader(header: String): SignedJWT {
        val authorization = header.removePrefix("Bearer")
        return SignedJWT.parse(authorization)
    }

    private fun secretsForUser(user: String): List<String> {
        return secretService.secretsForUser(user)
            .map { it.actualValue }
            .ifEmpty { listOf("") }
    }

    data class PostSecretRequestEntity(
        val key: String,
        val value: String,
        val notes: String?,
        val url: String?
    )
}
