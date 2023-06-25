package de.torfstack.kayvault.controller

import com.google.api.gax.rpc.InvalidArgumentException
import com.nimbusds.jwt.SignedJWT
import de.torfstack.kayvault.persistence.SecretEntity
import de.torfstack.kayvault.persistence.SecretModel
import de.torfstack.kayvault.persistence.SecretService
import de.torfstack.kayvault.validation.TokenValidator
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.lang.IllegalArgumentException

@RestController
@CrossOrigin
class SecretController(val secretService: SecretService, val tokenVerifier: TokenValidator) {

    @GetMapping("secret")
    fun getSecret(@RequestHeader authorization: String): List<SecretRequestEntity> {
        val user = userFromHeader(authorization)
        return secretsForUser(user)
    }

    @PostMapping("secret")
    fun postSecret(@RequestHeader authorization: String, @RequestBody entity: SecretRequestEntity): List<SecretRequestEntity> {
        val user = userFromHeader(authorization)
        secretService.addSecretForUser(user, entity.toModel())
        return secretsForUser(user)
    }

    private fun userFromHeader(header: String): String {
        val authorization = header.removePrefix("Bearer")
        when (val result = tokenVerifier.validate(authorization)) {
            is TokenValidator.InvalidVerification -> throw IllegalArgumentException("token could not be verified", result.ex)
            is TokenValidator.ValidVerification -> return result.user
        }
    }

    private fun secretsForUser(user: String): List<SecretRequestEntity> {
        return secretService.secretsForUser(user)
            .map {
                SecretRequestEntity(
                    key = it.secretKey,
                    value = it.secretValue,
                    url = it.secretUrl,
                    notes = ""
                )
            }
            .ifEmpty { emptyList() }
    }

    data class SecretRequestEntity(
        val key: String,
        val value: String,
        val notes: String?,
        val url: String?
    ) {
        fun toModel(): SecretModel {
            return SecretModel(
                secretValue = value,
                secretUrl = url,
                secretKey = key
            )
        }
    }
}
