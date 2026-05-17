package ru.itis.bookmatch.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.itis.bookmatch.data.repository.BookRepositoryImpl
import ru.itis.bookmatch.domain.GetBooksForSwipeUseCase
import ru.itis.bookmatch.presentation.navigation.BookMatchApp
import ru.itis.bookmatch.ui.theme.BookMatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookMatchTheme {
                BookMatchApp()
            }
        }
    }
}