package ru.itis.bookmatch.presentation.screens.registration

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RegistrationScreenViewModel(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
): ViewModel() {

    private val _state = MutableStateFlow<RegistrationScreenState>(RegistrationScreenState.Content(
        email = "",
        password = ""
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
        }
    }

    fun registerUser() {

        val currentState = _state.value
        if (currentState is RegistrationScreenState.Content) {
            val email = currentState.email
            val password = currentState.password

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

                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    _state.update {
                        RegistrationScreenState.Error("Введите корректный email")
                    }
                    return
                }
            }

            viewModelScope.launch {
                _state.update { RegistrationScreenState.Loading }

                try {
                    val result = auth.createUserWithEmailAndPassword(email, password).await()
                    val user = result.user

                    if (user != null) {
                        _state.update {
                            RegistrationScreenState.Success(user)
                        }
                    } else {
                        _state.update {
                            RegistrationScreenState.Error("Ошибка при регистрации")
                        }
                    }
                } catch (e: Exception) {
                    _state.update {
                        RegistrationScreenState.Error(e.message ?: "")
                    }
                }
            }
        }
    }

    fun updateState(email: String? = null, password: String? = null) {

        val currentState = _state.value
        val currentEmail =  if (currentState is RegistrationScreenState.Content) {
            currentState.email
        } else ""
        val currentPassword =  if (currentState is RegistrationScreenState.Content) {
            currentState.password
        } else ""

        _state.update {
            RegistrationScreenState.Content(
                email = email ?: currentEmail,
                password = password ?: currentPassword
            )
        }
    }

}

sealed interface RegistrationScreenCommand {

    data class PasswordInput(val password: String): RegistrationScreenCommand

    data class EmailInput(val email: String): RegistrationScreenCommand

    data object RegisterClicked : RegistrationScreenCommand
}

sealed interface RegistrationScreenState{

    data class Error(val message: String): RegistrationScreenState

    data class Content(
        val email: String,
        val password: String
    ): RegistrationScreenState

    data object Loading: RegistrationScreenState

    data class Success(val user: FirebaseUser): RegistrationScreenState
}













