package ru.itis.bookmatch.domain

import javax.inject.Inject
import ru.itis.bookmatch.domain.repository.AuthRepository

class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    operator fun invoke() {
        authRepository.logout()
    }
}