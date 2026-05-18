package ru.itis.bookmatch.domain

import ru.itis.bookmatch.data.repository.BookRepository
import javax.inject.Inject

class GetBooksForSwipeUseCase @Inject constructor(
    private val repository: BookRepository
) {

    suspend operator fun invoke(): List<Book> {
        return repository.getBookForSwipe()
    }

}