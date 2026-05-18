package ru.itis.bookmatch.domain

import ru.itis.bookmatch.data.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String): AuthUser {
        return repository.register(email, password)
    }
}
