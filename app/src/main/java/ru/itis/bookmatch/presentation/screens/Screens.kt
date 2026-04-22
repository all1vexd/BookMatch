package ru.itis.bookmatch.presentation.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    open val route: String,
    open val title: String = "",
    open val icon: ImageVector? = null
) {
    data class Discover(
        override val route: String = "discover",
        override val title: String = "Discover",
        override val icon: ImageVector = Icons.Default.MenuBook
    ) : Screen(route = route, title, icon)

    data class Saved(
        override val route: String = "saved",
        override val title: String = "Saved",
        override val icon: ImageVector = Icons.Default.Bookmark
    ) : Screen(route, title, icon)

    data class Library(
        override val route: String = "library",
        override val title: String = "Library",
        override val icon: ImageVector = Icons.Default.AutoStories
    ) : Screen(route, title, icon)

    data class Profile(
        override val route: String = "profile",
        override val title: String = "Profile",
        override val icon: ImageVector = Icons.Default.Person
    ) : Screen(route, title, icon)

    object Registration : Screen(
        route = "registration",
        title = "Registration"
    )

    object Login : Screen(
        route = "login",
        title = "Login"
    )
}