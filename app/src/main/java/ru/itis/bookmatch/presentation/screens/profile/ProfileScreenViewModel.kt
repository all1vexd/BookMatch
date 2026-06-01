package ru.itis.bookmatch.presentation.screens.profile

import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.bookmatch.domain.LogoutUseCase
import ru.itis.bookmatch.domain.ReadBook
import ru.itis.bookmatch.domain.profileUseCase.GetUserNicknameUseCase
import ru.itis.bookmatch.domain.likedUseCase.GetLikedBooksCountUseCase
import ru.itis.bookmatch.domain.profileUseCase.GetLastReadBooksUseCase
import ru.itis.bookmatch.domain.profileUseCase.UpdateNickNameUseCase
import ru.itis.bookmatch.domain.readUseCase.GetReadBooksCountUseCase
import ru.itis.bookmatch.presentation.screens.profile.ProfileScreenState.*

class ProfileScreenViewModel @AssistedInject constructor(
    @Assisted("userId") private val userId: String,
    private val getUserNicknameUseCase: GetUserNicknameUseCase,
    private val updateNickNameUseCase: UpdateNickNameUseCase,
    private val getReadBooksCountUseCase: GetReadBooksCountUseCase,
    private val getLikedBooksCountUseCase: GetLikedBooksCountUseCase,
    private val getLastReadBooksUseCase: GetLastReadBooksUseCase,
    private val logoutUseCase: LogoutUseCase
): ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("userId") userId: String
        ) : ProfileScreenViewModel
    }

    private val _state = MutableStateFlow<ProfileScreenState>(ProfileScreenState.Loading)
    val state = _state.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            try {
                _state.value = ProfileScreenState.Loading
                val nickname = getUserNicknameUseCase(userId = userId)
                val countLikedBooks = getLikedBooksCountUseCase(userId = userId)
                val countReadBooks = getReadBooksCountUseCase(userId = userId)
                getLastReadBooksUseCase(userId).collect {
                    _state.value = ProfileScreenState.Content(
                        nickname = nickname,
                        countLikedBooks = countLikedBooks,
                        countReadBooks = countReadBooks,
                        recentReadBooks = it
                    )
                }

            } catch (e: Exception) {
                _state.value = ProfileScreenState.Error(errorMessage = e.message ?: "")
            }
        }
    }

    fun processCommand(command: ProfileScreenCommand) {
        when (command) {
            is ProfileScreenCommand.EditNickname -> {
                viewModelScope.launch {
                    try {
                        updateNickNameUseCase(userId, command.nickname)
                        _state.update {
                            (it as ProfileScreenState.Content).copy(nickname = command.nickname, dialogUserId = null)
                        }
                    } catch (e: Exception) {
                        _state.value = Error(
                            errorMessage = e.message ?: ""
                        )
                    }

                }

            }

            ProfileScreenCommand.CloseEditingDialog -> {
                _state.update {
                    (it as ProfileScreenState.Content).copy(dialogUserId = null)
                }
            }

            is ProfileScreenCommand.OpenEditingDialog -> {
                _state.update {
                    (it as ProfileScreenState.Content).copy(dialogUserId = userId)
                }
            }

            ProfileScreenCommand.Logout -> {
                logoutUseCase()
            }

        }
    }

}

sealed interface ProfileScreenCommand {

    data class OpenEditingDialog(val userId: String): ProfileScreenCommand

    data class EditNickname(val nickname: String): ProfileScreenCommand

    data object CloseEditingDialog: ProfileScreenCommand

    data object Logout: ProfileScreenCommand
}

sealed interface ProfileScreenState {
    data object Loading: ProfileScreenState

    data class Error(val errorMessage: String): ProfileScreenState

    data class Content(
        val nickname: String,
        val countReadBooks: Int,
        val countLikedBooks: Int,
        val dialogUserId: String? = null,
        val recentReadBooks: List<ReadBook> = emptyList()
    ) : ProfileScreenState
}