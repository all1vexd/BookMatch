package ru.itis.bookmatch.domain.repository

interface ProfileRepository {

    suspend fun getUserNickname(userId: String): String

    suspend fun updateNickname(userId: String, nickname: String)
}