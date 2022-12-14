package ru.fastly.quizz.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.fastly.quizz.data.Answer
import ru.fastly.quizz.App
import ru.fastly.quizz.App.Companion.dataset
import ru.fastly.quizz.providers.DataProvider

class QuizzViewModel(dataset: DataProvider) : ViewModel() {

    open var state = MutableLiveData<Int>(0)
    private var currentIndex: Int = 0
    private var correctCount: Int = 0
    private var answers: MutableList<Int> = mutableListOf() //индексы данных ответов
    private var visited: MutableList<Int> = mutableListOf() //вопросы которые посмотрели (и ответили)


    fun clear() {

        state.value = 0
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
        return dataset.getDataCount()
    }

    //проверка на правильный ответ
    private fun isAnswerCorrect(id:Int):Boolean {
        return dataset.getRightAnswerIdForQuestion(currentIndex) == id
    }

    private fun setAnswer(id:Int) {
        visited.add(currentIndex)
        answers.add(id)
        if (isAnswerCorrect(id)) {
            correctCount++
        }
    }

    fun getQuestion(id:Int): String {
        return dataset.getQuestion(id)
    }

    fun getAnswers(id: Int): List<Answer> {
        return  dataset.getAnswersForQuestion(id)
    }

    fun getProgress():String {
        return (currentIndex+1).toString() + " / " + getDataCount().toString()
    }

    fun getScores():Int {
        return correctCount
    }

    fun stepForward(id:Int) {
        if (id > -1) {
            setAnswer(id)
        }
        currentIndex++
        state.value = currentIndex
    }

    fun stepBackward() {
        if(currentIndex > 0) {
            currentIndex--
            state.value = currentIndex
        }
    }

    fun getCurrentStep(): Int {
        return currentIndex
    }
}

