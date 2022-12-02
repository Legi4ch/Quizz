package ru.fastly.quizz.view_models

import androidx.lifecycle.ViewModel
import ru.fastly.quizz.models.Answer
import ru.fastly.quizz.utils.App

class QuizzViewModel: ViewModel() {

    private var currentIndex: Int = 0
    private var correctCount: Int = 0
    private var answers: MutableList<Int> = mutableListOf() //ответы на вопросы
    private var visited: MutableList<Int> = mutableListOf() //вопросы на которые уже были даны ответы


    fun clear() {
        currentIndex = 0
        correctCount = 0
        answers = mutableListOf()
        visited = mutableListOf()
    }

    //просмотренный вопрос или нет
    fun isVisited(): Boolean {
        return currentIndex in visited
    }

    fun isLastQuestion():Boolean {
        return currentIndex+1 == getDataCount()
    }

    //возвращает index ответа на вопрос, если он был установлен
    fun getCurrentAnswerId():Int {
        return if (isVisited()) {
            answers[currentIndex]
        } else {
            -1
        }
    }

    private fun getDataCount(): Int {
        return App.dataset.getDataCount()
    }

    //проверка на правильный ответ
    private fun isAnswerCorrect(id:Int):Boolean {
        return App.dataset.getRightAnswerIdForQuestion(currentIndex) == id
    }

    fun setAnswer(id:Int) {
        visited.add(currentIndex)
        answers.add(id)
        if (isAnswerCorrect(id)) {
            correctCount++
        }
    }

    fun getQuestion(id:Int): String {
        return App.dataset.getQuestion(id)
    }

    fun getAnswers(id: Int): List<Answer> {
        return  App.dataset.getAnswersForQuestion(id)
    }

    fun getProgress():String {
        return (currentIndex+1).toString() + " / " + getDataCount().toString()
    }

    fun getScores():Int {
        return correctCount
    }

    fun stepForward() {
        currentIndex++
    }

    fun stepBackward() {
        currentIndex--
    }

    fun getCurrentStep(): Int {
        return currentIndex
    }
}