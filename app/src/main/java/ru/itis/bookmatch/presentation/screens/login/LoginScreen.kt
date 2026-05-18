package ru.itis.bookmatch.presentation.screens.login

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.collectLatest
import ru.itis.bookmatch.BookMatchApplication

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    login: (String) -> Unit,
    moveToRegister: () -> Unit
) {
    val context = LocalContext.current
    val appComponent = (context.applicationContext as BookMatchApplication).appComponent
    val viewModel: LoginScreenViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return appComponent.loginScreenViewModel() as T
            }
        }
    )

    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.state.collectLatest { currentState ->
            if (currentState is LoginScreenState.Error) {
                snackbarHostState.showSnackbar(currentState.message)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.state.collectLatest { currentState ->
            if (currentState is LoginScreenState.Success) {
                login(currentState.user.uid)
                Log.d("LoginScreen", "userId = ${currentState.user.uid}")
            }
        }
    }

    Scaffold(

    ) { paddingValues ->
        when (state) {
            is LoginScreenState.Content -> {
                val contentState = state as LoginScreenState.Content

                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Вход",
                        fontSize = 28.sp,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    OutlinedTextField(
                        value = contentState.email,
                        onValueChange = {
                            viewModel.processCommand(LoginScreenCommand.EmailInput(it))
                        },
                        label = {
                            Text(
                                text = "Email"
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = contentState.password,
                        onValueChange = {
                            viewModel.processCommand(LoginScreenCommand.PasswordInput(it))
                        },
                        label = { Text("Пароль") }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            viewModel.processCommand(LoginScreenCommand.LoginClicked)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Войти")
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Еще нет аккаунта? Зарегестрируйтесь!",
                        modifier = Modifier.clickable(
                            onClick = {
                                moveToRegister()
                            }
                        )
                    )
                }
            }
            is LoginScreenState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Ошибка",
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = (state as LoginScreenState.Error).message)
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(
                            onClick = {
                                viewModel.processCommand(LoginScreenCommand.EmailInput(""))
                            }
                        ) {
                            Text("Попробовать снова")
                        }
                    }
                }
            }
            LoginScreenState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Вход...")
                    }
                }
            }

            is LoginScreenState.Success -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}