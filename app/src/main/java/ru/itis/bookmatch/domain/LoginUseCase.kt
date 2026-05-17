package ru.itis.bookmatch.domain

import ru.itis.bookmatch.data.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String): AuthUser {
        return repository.login(email, password)
    }
}
