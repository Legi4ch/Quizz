package ru.fastly.quizz.data

data class QuizzDataItem(
    val Answers: List<Answer>,
    val Question: String,
    val id: Int
)