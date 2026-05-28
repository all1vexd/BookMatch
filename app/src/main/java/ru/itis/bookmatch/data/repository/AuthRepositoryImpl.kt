package ru.itis.bookmatch.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import ru.itis.bookmatch.domain.AuthUser
import ru.itis.bookmatch.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun login(email: String, password: String): AuthUser {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val user = result.user ?: throw Exception("Ошибка при входе")
        return AuthUser(uid = user.uid)
    }

    override suspend fun register(email: String, password: String, nickname: String): AuthUser {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: throw Exception("Ошибка при регистрации")
        try {
            firestore.collection("users")
                .document(user.uid)
                .set(mapOf("nickname" to nickname))
                .await()
        } catch (e: Exception) {

        }
        return AuthUser(uid = user.uid)
    }
}
