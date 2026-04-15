package ru.itis.bookmatch.presentation.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Discover : Screen("discover", "Discover", Icons.Default.MenuBook)
    data object Saved : Screen("saved", "Saved", Icons.Default.Bookmark)
    data object Library : Screen("library", "Library", Icons.Default.AutoStories)
    data object Profile : Screen("profile", "Profile", Icons.Default.Person)
}