package ru.itis.bookmatch.domain

import ru.itis.bookmatch.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String, nickname: String): AuthUser {
        return repository.register(email, password, nickname)
    }
}
