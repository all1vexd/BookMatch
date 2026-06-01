package ru.itis.bookmatch.domain.repository

import ru.itis.bookmatch.domain.AuthUser

interface AuthRepository {

    suspend fun login(email: String, password: String): AuthUser

    suspend fun register(email: String, password: String, nickname: String): AuthUser

    fun getCurrentUser(): AuthUser?

    fun logout()
}