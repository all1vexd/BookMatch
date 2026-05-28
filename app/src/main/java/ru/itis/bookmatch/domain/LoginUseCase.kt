package ru.itis.bookmatch.domain

import ru.itis.bookmatch.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String): AuthUser {
        return repository.login(email, password)
    }
}
