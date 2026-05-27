package ru.itis.bookmatch.presentation.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.bookmatch.data.repository.AuthRepositoryImpl
import ru.itis.bookmatch.domain.AuthUser
import ru.itis.bookmatch.domain.RegisterUseCase
import javax.inject.Inject

class RegistrationScreenViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
): ViewModel() {

    private val _state = MutableStateFlow<RegistrationScreenState>(RegistrationScreenState.Content(
        email = "",
        password = "",
        nickname = ""
    ))
    val state = _state.asStateFlow()

    fun processCommand(command: RegistrationScreenCommand) {
        when (command) {
            is RegistrationScreenCommand.EmailInput -> {
                updateState(email = command.email.trim())
            }

            is RegistrationScreenCommand.PasswordInput -> {
                updateState(password = command.password.trim())
            }

            RegistrationScreenCommand.RegisterClicked -> {
                registerUser()
            }

            is RegistrationScreenCommand.NicknameInput -> {
                updateState(nickname = command.nick.trim())
            }
        }
    }

    private fun registerUser() {

        val currentState = _state.value as? RegistrationScreenState.Content ?: return
        val email = currentState.email
        val password = currentState.password
        val nickname = currentState.nickname
        when {
            email.isEmpty() -> {
                _state.update {
                    RegistrationScreenState.Error("Введите email")
                }
                return
            }

            password.isEmpty() -> {
                _state.update {
                    RegistrationScreenState.Error("Введите пароль")
                }
                return
            }

            password.length < 6 -> {
                _state.update {
                    RegistrationScreenState.Error("Пароль должен содержать минимум 6 символов")
                }
                return
            }

            nickname.isEmpty() -> {
                _state.update {
                    RegistrationScreenState.Error("Введите имя пользователя")
                }
                return
            }

            nickname.length < 6 -> {
                _state.update {
                    RegistrationScreenState.Error("Имя должно содержать минимум 6 символов")
                }
                return
            }

            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _state.update {
                    RegistrationScreenState.Error("Введите корректный email")
                }
                return
            }
        }



        viewModelScope.launch {
            _state.update {
                RegistrationScreenState.Loading
            }
            try {
                val user = registerUseCase(email, password, nickname)
                _state.update {
                    RegistrationScreenState.Success(user)
                }
            } catch (e: Exception) {
                _state.update {
                    RegistrationScreenState.Error(
                        message = e.message ?: "Ошибка при регистрации"
                    )
                }
            }
        }
    }

    private fun updateState(email: String? = null, password: String? = null, nickname: String? = null) {

        val currentState = _state.value as? RegistrationScreenState.Content ?: return

        _state.update {
            RegistrationScreenState.Content(
                email = email ?: currentState.email,
                password = password ?: currentState.password,
                nickname = nickname ?: currentState.nickname
            )
        }
    }

}

sealed interface RegistrationScreenCommand {

    data class PasswordInput(val password: String): RegistrationScreenCommand

    data class EmailInput(val email: String): RegistrationScreenCommand


    data class NicknameInput(val nick: String): RegistrationScreenCommand

    data object RegisterClicked : RegistrationScreenCommand
}

sealed interface RegistrationScreenState{

    data class Error(val message: String): RegistrationScreenState

    data class Content(
        val email: String,
        val password: String,
        val nickname: String
    ): RegistrationScreenState

    data object Loading: RegistrationScreenState

    data class Success(val user: AuthUser): RegistrationScreenState
}













