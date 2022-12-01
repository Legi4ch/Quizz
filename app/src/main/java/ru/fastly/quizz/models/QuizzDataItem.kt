package ru.fastly.quizz.models

data class QuizzDataItem(
    val Answers: List<Answer>,
    val Question: String,
    val id: Int
)