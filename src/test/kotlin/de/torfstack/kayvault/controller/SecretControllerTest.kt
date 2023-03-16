package de.torfstack.kayvault.controller

import assertk.assertThat
import assertk.assertions.hasSize
import de.torfstack.kayvault.persistence.SecretService
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class SecretControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var secretService: SecretService

    @Test
    fun `getting secret returns nothing`() {
        mockMvc.perform(get("/secret"))
            .andExpect(status().isOk)
            .andExpect(content().string(""))
    }

    @Test
    fun `secret for one user can be retrieved`() {
        mockMvc.perform(post("/secret")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "key": "system",
                    "value": "abc-def-ghij"
                }
            """.trimIndent()))
            .andExpect(status().isOk)

        val secrets = secretService.secretsForUser("test")
        assertThat(secrets).hasSize(1)
    }
}