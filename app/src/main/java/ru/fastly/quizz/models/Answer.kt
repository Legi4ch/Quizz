package ru.fastly.quizz.models

data class Answer(
    val id: Int,
    val text: String,
    val valid: Boolean
)