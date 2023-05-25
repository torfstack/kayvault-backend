package de.torfstack.kayvault.controller

import de.torfstack.kayvault.persistence.SecretService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SecretController(val secretService: SecretService) {

    @GetMapping("secret")
    fun getSecret(): String {
        return secretService.secretsForUser("test")
            .map { it.actualValue }
            .ifEmpty { listOf("") }
            .first()
    }

    @PostMapping("secret")
    fun postSecret(@RequestBody entity: PostSecretRequestEntity) {
        secretService.addSecretForUser("test", entity.value)
    }

    data class PostSecretRequestEntity(
        val key: String,
        val value: String,
        val notes: String?,
        val url: String?
    )
}
