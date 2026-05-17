package ru.itis.bookmatch.domain

import ru.itis.bookmatch.data.repository.AuthRepository

class RegisterUseCase(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String): AuthUser {
        return repository.register(email, password)
    }
}
