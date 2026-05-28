package ru.itis.bookmatch.domain.profileUseCase

import ru.itis.bookmatch.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateNickNameUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {

    suspend operator fun invoke(userId: String, newNickName: String) {
        profileRepository.updateNickname(userId, newNickName)
    }
}