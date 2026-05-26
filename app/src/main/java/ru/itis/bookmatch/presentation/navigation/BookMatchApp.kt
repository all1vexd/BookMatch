package ru.itis.bookmatch.presentation.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.itis.bookmatch.presentation.screens.BottomBar
import ru.itis.bookmatch.presentation.screens.Screen
import ru.itis.bookmatch.presentation.screens.library.LibraryScreen
import ru.itis.bookmatch.presentation.screens.login.LoginScreen
import ru.itis.bookmatch.presentation.screens.mainScreen.MainScreen
import ru.itis.bookmatch.presentation.screens.profile.ProfileScreen
import ru.itis.bookmatch.presentation.screens.registration.RegistrationScreen
import ru.itis.bookmatch.presentation.screens.saved.SavedScreen

@Composable
fun BookMatchApp() {
    val navController = rememberNavController()

    var currentUserId by remember { mutableStateOf("") }
    var currentRoute by remember {
        mutableStateOf(Screen.Discover.route)
    }

    navController.addOnDestinationChangedListener { _, destination, _ ->
        currentRoute = destination.route ?: Screen.Discover.route
    }

    val shouldShowBottomBar = currentRoute.startsWith("discover/") ||
            currentRoute.startsWith("saved/") ||
            currentRoute.startsWith("library/") ||
            currentRoute.startsWith("profile/")

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.weight(1f)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    login = {
                        currentUserId = it
                        navController.navigate(Screen.Discover.createRoute(it)) {
                            popUpTo(Screen.Login.route) {
                                inclusive = true
                            }
                        }
                    },
                    moveToRegister = {
                        navController.navigate(Screen.Registration.route)
                    }
                )
            }
            composable(Screen.Registration.route) {
                RegistrationScreen(
                    register = {
                        currentUserId = it
                        navController.navigate(Screen.Discover.createRoute(it)) {
                            popUpTo(Screen.Registration.route) {
                                inclusive = true
                            }
                        }
                    },
                    moveToLogin = {
                        navController.navigate(Screen.Login.route)
                    }
                )
            }
            composable(Screen.Discover.route) {
                MainScreen(
                    userId = currentUserId
                )
            }
            composable(Screen.Saved.route) {
                SavedScreen(
                    userId = currentUserId,
                    onBookClick = { bookId ->
                        TODO("Сделать")
                    }
                )
            }
            composable(Screen.Library.route) {
                LibraryScreen(
                    userId = currentUserId
                )
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
        }

        if (shouldShowBottomBar) {
            BottomBar(
                selected = when {
                    currentRoute.startsWith("discover/") -> Screen.Discover
                    currentRoute.startsWith("saved/") -> Screen.Saved
                    currentRoute.startsWith("library/") -> Screen.Library
                    currentRoute.startsWith("profile/") -> Screen.Profile
                    else -> Screen.Discover
                },
                onTabSelected = { screen ->
                    val route = when (screen) {
                        is Screen.Discover -> {
                            screen.createRoute(currentUserId)
                        }
                        is Screen.Library -> {
                            screen.createRoute(currentUserId)
                        }
                        is Screen.Profile -> {
                            screen.createRoute(currentUserId)
                        }
                        is Screen.Saved -> {
                            screen.createRoute(currentUserId)
                        }
                        else -> {
                            screen.route
                        }
                    }
                    navigateOnBottomBar(navController, route)
                }
            )
        }
    }
}

fun navigateOnBottomBar(navController: NavHostController, route: String) {
    navController.navigate(route) {
        popUpTo(Screen.Discover.route) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}