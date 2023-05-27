package de.torfstack.kayvault.controller

import de.torfstack.kayvault.persistence.SecretService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
class SecretController(val secretService: SecretService) {

    @GetMapping("secret")
    fun getSecret(): List<String> {
        return secretsForUser("test")
    }

    @PostMapping("secret")
    fun postSecret(@RequestBody entity: PostSecretRequestEntity): List<String> {
        secretService.addSecretForUser("test", entity.value)
        return secretsForUser("test")
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
