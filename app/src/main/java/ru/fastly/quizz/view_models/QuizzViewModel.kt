package ru.fastly.quizz.view_models

import androidx.lifecycle.ViewModel

class QuizzViewModel: ViewModel() {

    var currentIndex: Int = 0
    var correctCount: Int = 0
    var answers: MutableList<Int> = mutableListOf() //ответы на вопросы
    var visited: MutableList<Int> = mutableListOf() //вопросы на которые уже были даны ответы


    fun clear() {
        currentIndex = 0
        correctCount = 0
        answers = mutableListOf()
        visited = mutableListOf()
    }

    override fun onCleared() {
        super.onCleared()
    }
}