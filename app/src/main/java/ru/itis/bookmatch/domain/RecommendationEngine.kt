package ru.itis.bookmatch.domain

import javax.inject.Inject

class RecommendationEngine @Inject constructor() {

    private val popularGenres = listOf(
        "Fantasy", "Science Fiction", "Mystery", "Romance",
        "Thriller", "Historical Fiction", "Biography", "Horror"
    )

    fun getQueries(likedBooks: List<Book>): List<String> {
        val topGenres = topGenres(likedBooks)

        if (topGenres.isEmpty()) {
            return popularGenres.shuffled().take(4).map { "subject:$it" }
        }

        val queries = mutableListOf<String>()

        queries.add("subject:\"${topGenres[0]}\"")

        val second = topGenres.getOrNull(1) ?: popularGenres.filter { it != topGenres[0] }.random()
        queries.add("subject:\"$second\"")

        val topAuthor = topAuthor(likedBooks)
        if (topAuthor != null) queries.add("inauthor:\"$topAuthor\"")

        val usedGenres = topGenres.take(2).toSet()
        queries.add("subject:${popularGenres.filter { it !in usedGenres }.random()}")

        return queries
    }

    fun getReliableFallbackQuery(): String = "subject:${popularGenres.first()}"

    private fun topGenres(likedBooks: List<Book>): List<String> {
        return likedBooks
            .flatMap { book -> book.categories.map { it.split("/").last().trim() } }
            .filter { it.isNotEmpty() }
            .groupingBy { it }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .take(3)
            .map { it.key }
    }

    private fun topAuthor(likedBooks: List<Book>): String? {
        return likedBooks
            .flatMap { it.authors }
            .filter { it.isNotEmpty() }
            .groupingBy { it }
            .eachCount()
            .maxByOrNull { it.value }
            ?.key
    }
}
