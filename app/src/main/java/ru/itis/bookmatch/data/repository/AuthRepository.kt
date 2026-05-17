package ru.itis.bookmatch.data.repository

import ru.itis.bookmatch.domain.AuthUser

interface AuthRepository {

    suspend fun login(email: String, password: String): AuthUser

    suspend fun register(email: String, password: String): AuthUser
}
