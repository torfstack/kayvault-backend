package de.torfstack.kayvault.controller

import de.torfstack.kayvault.persistence.SecretService
import org.springframework.util.DigestUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
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
    @ResponseBody
    fun postSecret(@RequestBody entity: PostSecretRequestEntity): PostSecretResponseEntity {
        val digest = DigestUtils.md5DigestAsHex(entity.value.byteInputStream())
        secretService.addSecretForUser("test", digest)
        return PostSecretResponseEntity(
            key = entity.key,
            hash = digest
        )
    }

    data class PostSecretRequestEntity(
        val key: String,
        val value: String,
        val notes: String?,
        val url: String?
    )

    data class PostSecretResponseEntity(
        val key: String,
        val hash: String
    )
}
