package ru.itis.bookmatch.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.itis.bookmatch.domain.AuthUser
import ru.itis.bookmatch.domain.LoginUseCase
import ru.itis.bookmatch.domain.likedUseCase.SyncLikedUseCase
import javax.inject.Inject

class LoginScreenViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val syncUseCase: SyncLikedUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<LoginScreenState>(LoginScreenState.Content(
        email = "",
        password = ""
    ))
    val state = _state.asStateFlow()

    fun processCommand(command: LoginScreenCommand) {
        when (command) {
            is LoginScreenCommand.EmailInput -> {
                updateState(email = command.email.trim())
            }
            is LoginScreenCommand.PasswordInput -> {
                updateState(password = command.password.trim())
            }
            LoginScreenCommand.LoginClicked -> {
                loginUser()
            }
        }
    }

    private fun loginUser() {
        val currentState = _state.value as? LoginScreenState.Content ?: return
        val email = currentState.email
        val password = currentState.password

        when {
            email.isEmpty() -> {
                _state.update { LoginScreenState.Error("Введите email") }
                return
            }
            password.isEmpty() -> {
                _state.update { LoginScreenState.Error("Введите пароль") }
                return
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                _state.update { LoginScreenState.Error("Введите корректный email") }
                return
            }
        }

        viewModelScope.launch {
            _state.update { LoginScreenState.Loading }
            try {
                val user = loginUseCase(email, password)
                syncUseCase(userId = user.uid)
                _state.update { LoginScreenState.Success(user) }
            } catch (e: Exception) {
                _state.update { LoginScreenState.Error(e.message ?: "Ошибка при входе") }
            }
        }

    }

    private fun updateState(email: String? = null, password: String? = null) {

        val currentState = _state.value
        val currentEmail = if (currentState is LoginScreenState.Content) {
            currentState.email
        } else ""
        val currentPassword = if (currentState is LoginScreenState.Content) {
            currentState.password
        } else ""

        _state.update {
            LoginScreenState.Content(
                email = email ?: currentEmail,
                password = password ?: currentPassword
            )
        }
    }
}

sealed interface LoginScreenCommand {

    data class EmailInput(val email: String) : LoginScreenCommand

    data class PasswordInput(val password: String) : LoginScreenCommand

    data object LoginClicked : LoginScreenCommand
}

sealed interface LoginScreenState {

    data class Error(val message: String) : LoginScreenState

    data class Content(val email: String, val password: String) : LoginScreenState

    data object Loading : LoginScreenState

    data class Success(val user: AuthUser) : LoginScreenState
}