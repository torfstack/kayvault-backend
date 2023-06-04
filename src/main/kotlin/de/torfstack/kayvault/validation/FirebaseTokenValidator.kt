package de.torfstack.kayvault.validation

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import org.springframework.stereotype.Component
import java.io.FileInputStream

@Component
class FirebaseTokenValidator : TokenValidator {

    private val app = FirebaseApp.initializeApp(
        FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(FileInputStream("kayvault-c8416-d1b77965b6ee.json")))
            .build()
    )

    override fun validate(token: String): TokenValidator.VerificationResult {
        val auth = FirebaseAuth.getInstance(app)
        return  try {
            val parsedJwt = auth.verifyIdToken(token)
            TokenValidator.ValidVerification(parsedJwt.uid)
        } catch (e: Exception) {
            TokenValidator.InvalidVerification(e)
        }
    }
}