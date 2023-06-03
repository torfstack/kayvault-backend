package de.torfstack.kayvault.persistence

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class SecretService @Autowired constructor(val repo: SecretRepository) {
    fun secretsForUser(user: String): List<SecretEntity> {
        return repo.findByForUser(user)
    }

    fun addSecretForUser(user: String, secret: String) {
        repo.save(
            SecretEntity().also {
                it.actualValue = secret
                it.forUser = user
            }
        )
    }
}
