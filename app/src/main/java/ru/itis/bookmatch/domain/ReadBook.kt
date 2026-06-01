package ru.itis.bookmatch.domain

data class ReadBook (
    val book: Book,
    val rating: Double = 0.0,
    val feedback: String = ""
)