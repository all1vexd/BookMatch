package ru.itis.bookmatch.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.itis.bookmatch.data.BookRepositoryImpl
import ru.itis.bookmatch.domain.Book
import ru.itis.bookmatch.domain.GetBooksForSwipeUseCase
import ru.itis.bookmatch.presentation.screens.BottomBar
import ru.itis.bookmatch.presentation.screens.Screen
import ru.itis.bookmatch.presentation.screens.library.LibraryScreen
import ru.itis.bookmatch.presentation.screens.mainScreen.MainScreen
import ru.itis.bookmatch.presentation.screens.profile.ProfileScreen
import ru.itis.bookmatch.presentation.screens.saved.SavedScreen

@Composable
fun BookMatchApp() {
    val navController = rememberNavController()

    var currentRoute by remember { mutableStateOf(Screen.Discover.route) }
    var likedBooks by remember { mutableStateOf<List<Book>>(emptyList()) }

    navController.addOnDestinationChangedListener { _, destination, _ ->
        currentRoute = destination.route ?: Screen.Discover.route
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.Discover.route,
            modifier = Modifier.weight(1f)
        ) {
            composable(Screen.Discover.route) {
                MainScreen(
                    getBooksForSwipeUseCase = GetBooksForSwipeUseCase(BookRepositoryImpl()),
                    onBookLiked = {
                        if (!likedBooks.contains(it)) {
                            likedBooks = likedBooks + it
                        }
                    }
                )
            }
            composable(Screen.Saved.route) {
                SavedScreen(
                    likedBooks = likedBooks,
                    onRemoveBook = { book ->
                        likedBooks = likedBooks - book
                    },
                    onBookClick = { bookId ->
                        TODO("Сделать")
                    }
                )
            }
            composable(Screen.Library.route) {
                LibraryScreen()
            }
            composable(Screen.Profile.route) {
                ProfileScreen()
            }
        }

        BottomBar(
            selected = when (currentRoute) {
                Screen.Discover.route -> Screen.Discover
                Screen.Saved.route -> Screen.Saved
                Screen.Library.route -> Screen.Library
                Screen.Profile.route -> Screen.Profile
                else -> Screen.Discover
            },
            onTabSelected = { screen ->
                navController.navigate(screen.route) {
                    popUpTo(Screen.Discover.route) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}