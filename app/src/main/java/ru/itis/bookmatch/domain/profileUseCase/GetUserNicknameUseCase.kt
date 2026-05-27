package ru.itis.bookmatch.domain.profileUseCase

import ru.itis.bookmatch.data.repository.ProfileRepository
import javax.inject.Inject

class GetUserNicknameUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {

    suspend operator fun invoke(userId: String): String {
        return profileRepository.getUserNickname(userId = userId)
    }
}