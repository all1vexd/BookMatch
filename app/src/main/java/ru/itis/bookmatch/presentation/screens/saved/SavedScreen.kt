package ru.itis.bookmatch.presentation.screens.saved

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import ru.itis.bookmatch.presentation.ui.components.BookMatchTopBar
import ru.itis.bookmatch.presentation.ui.components.EmptyStateScreen
import ru.itis.bookmatch.presentation.ui.components.ErrorScreen
import ru.itis.bookmatch.presentation.ui.components.LoadingScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import ru.itis.bookmatch.BookMatchApplication
import ru.itis.bookmatch.data.toHighQualityUrl
import ru.itis.bookmatch.domain.Book

@Composable
fun SavedScreen(
    userId: String,
    onBookClick: (String) -> Unit,
) {
    val context: Context = LocalContext.current
    val appComponent = (context.applicationContext as BookMatchApplication).appComponent

    val viewModel: SavedScreenViewModel = viewModel(
        key = userId,
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return appComponent.savedScreenViewModelFactory().create(userId) as T
            }
        }
    )

    val state by viewModel.state.collectAsState()

    when (state) {
        is SavedScreenState.Content -> {
            Scaffold(
                topBar = {
                    BookMatchTopBar(icon = Icons.Default.Bookmark, title = "Saved Books")
                },
                containerColor = MaterialTheme.colorScheme.background
            ) { paddingValues ->

                if ((state as SavedScreenState.Content).likedBooks.isEmpty()) {
                    EmptyStateScreen(
                        icon = Icons.Default.Bookmark,
                        title = "No saved books yet",
                        subtitle = "Swipe right on books to save them",
                        modifier = Modifier.fillMaxSize().padding(paddingValues)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items((state as SavedScreenState.Content).likedBooks) { book ->
                            SavedBookCard(
                                book = book,
                                onRemove = {
                                    viewModel.processCommand(SavedScreenCommand.RemoveBook(book.id))
                                },
                                onMarkAsRead = {
                                    viewModel.processCommand(SavedScreenCommand.MarkAsRead(it.id))
                                },
                                onBookClick = { bookId ->
                                    onBookClick(bookId)
                                }
                            )
                        }
                    }
                }
            }
        }
        is SavedScreenState.Error -> {
            ErrorScreen(
                message = "Error: ${(state as SavedScreenState.Error).errorMessage}",
                onRetry = { viewModel.loadData() },
                modifier = Modifier.fillMaxSize()
            )
        }
        SavedScreenState.Loading -> {
            LoadingScreen(modifier = Modifier.fillMaxSize())
        }
    }


}

@Composable
fun SavedBookCard(
    book: Book,
    onRemove: (Book) -> Unit,
    onMarkAsRead: (Book) -> Unit,
    onBookClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = {
            onBookClick(book.id)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            val imageUrl = toHighQualityUrl(book.thumbnailUrl)
            AsyncImage(
                model = imageUrl,
                contentDescription = "Cover of ${book.title}",
                modifier = Modifier
                    .fillMaxHeight()
                    .size(100.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = book.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                if (book.authors.isNotEmpty()) {
                    Text(
                        text = book.authors.joinToString(", "),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                if (book.categories.isNotEmpty()) {
                    Text(
                        text = book.categories.first(),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                if (book.publishedDate.isNotEmpty()) {
                    Text(
                        text = book.publishedDate.take(4),
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

            }

            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(
                    onClick = { onRemove(book) },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove from saved",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                    )
                }
                IconButton(
                    onClick = { onMarkAsRead(book) },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoStories,
                        contentDescription = "Mark as read",
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )
                }
            }

        }
    }
}