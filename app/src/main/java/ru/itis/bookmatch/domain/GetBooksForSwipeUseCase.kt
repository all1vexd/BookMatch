package ru.itis.bookmatch.domain

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import ru.itis.bookmatch.domain.repository.LikedBooksRepository
import ru.itis.bookmatch.domain.repository.BookRepository
import javax.inject.Inject

class GetBooksForSwipeUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val likedBooksRepository: LikedBooksRepository,
    private val recommendationEngine: RecommendationEngine
) {

    suspend operator fun invoke(userId: String, startIndex: Int): List<Book> {
        val likedBooks = likedBooksRepository.getLikedBooksFlow(userId).first()
        val queries = recommendationEngine.getQueries(likedBooks)

        val books = coroutineScope {
            queries
                .map { query -> async { runCatching { bookRepository.getBookForSwipe(query = query, startIndex = startIndex) }.getOrDefault(emptyList()) } }
                .flatMap { it.await() }
                .shuffled()
        }

        if (books.isNotEmpty()) return books
        return bookRepository.getBookForSwipe(recommendationEngine.getReliableFallbackQuery(), startIndex, maxResults = 20)
    }

}
