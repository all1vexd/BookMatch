package ru.itis.bookmatch.presentation.screens

import android.net.Uri
import android.os.Bundle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.gson.Gson
import ru.itis.bookmatch.domain.Book

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector? = null
) {

    data object Discover: Screen(
        route = "discover/{user_id}",
        title = "Discover",
        icon = Icons.Default.MenuBook
    ) {
        fun createRoute(userId: String): String {
            return "discover/$userId"
        }

        fun getUserId(arguments: Bundle?): String {
            return arguments?.getString("user_id") ?: ""
        }
    }

    data object Saved: Screen(
        route = "saved/{user_id}",
        title = "Saved",
        icon = Icons.Default.Bookmark
    ) {
        fun createRoute(userId: String): String {
            return "saved/$userId"
        }
    }

    data object Library: Screen(
        route = "library/{user_id}",
        title = "Library",
        icon = Icons.Default.AutoStories
    ) {
        fun createRoute(userId: String): String {
            return "library/$userId"
        }
    }

    data object Profile: Screen(
        route = "profile/{user_id}",
        title = "Profile",
        icon = Icons.Default.Person
    )  {
        fun createRoute(userId: String): String {
            return "profile/$userId"
        }
    }

    data object Registration: Screen(
        route = "registration",
        title = "Registration"
    )

    object Login : Screen(
        route = "login",
        title = "Login"
    )

    data object BookDetail : Screen(
        route = "book_detail/{user_id}/{book_id}",
        title = "BookDetail"
    ) {
        fun createRoute(userId: String, bookId: String): String {
            return "book_detail/${userId}/${bookId}"
        }

        fun getBookId(arguments: Bundle?): String {
            return arguments?.getString("book_id") ?: ""
        }
    }

    data object Search : Screen(
        route = "search/{user_id}",
        title = "Search",
        icon = Icons.Default.Search
    ) {
        fun createRoute(userId: String): String {
            return "search/$userId"
        }
    }

}