package ru.itis.bookmatch.data.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import ru.itis.bookmatch.domain.AuthUser

class AuthRepositoryImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : AuthRepository {

    override suspend fun login(email: String, password: String): AuthUser {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val user = result.user ?: throw Exception("Ошибка при входе")
        return AuthUser(uid = user.uid)
    }

    override suspend fun register(email: String, password: String): AuthUser {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: throw Exception("Ошибка при регистрации")
        return AuthUser(uid = user.uid)
    }
}
