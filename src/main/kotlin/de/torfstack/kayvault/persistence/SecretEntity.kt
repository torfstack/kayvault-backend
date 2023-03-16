package de.torfstack.kayvault.persistence

import jakarta.persistence.*

@Entity(name = "secrets")
class SecretEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var secretId: Long = 0

    @Column(name = "actualValue")
    var actualValue: String = ""

    @Column(name = "forUser")
    var forUser: String = ""
}