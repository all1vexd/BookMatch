package ru.itis.bookmatch.data.repository

interface ProfileRepository {

    suspend fun getUserNickname(userId: String): String

    suspend fun updateNickname(userId: String, nickname: String)
}