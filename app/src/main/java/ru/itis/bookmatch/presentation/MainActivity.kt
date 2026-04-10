package ru.itis.bookmatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.itis.bookmatch.data.BookRepositoryImpl
import ru.itis.bookmatch.domain.GetBooksForSwipeUseCase
import ru.itis.bookmatch.presentation.screens.mainScreen.MainScreen
import ru.itis.bookmatch.ui.theme.BookMatchTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        val getBooksForSwipeUseCase = GetBooksForSwipeUseCase(BookRepositoryImpl())

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookMatchTheme {
                MainScreen(
                    getBooksForSwipeUseCase = getBooksForSwipeUseCase
                )
            }
        }
    }
}