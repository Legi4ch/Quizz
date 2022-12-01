package ru.fastly.quizz.utils

open class Utils {

    companion object {
        fun getScores(scores: Int, question: Int): String {
            return when (scores.toDouble() / question.toDouble()) {
                in 0.0..Points.BAD.value -> Points.BAD.desc
                in Points.BAD.value..Points.OK.value -> Points.OK.desc
                in Points.OK.value..Points.GOOD.value -> Points.GOOD.desc
                else -> Points.BEST.desc
            }
        }
    }
}