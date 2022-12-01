// просто enum класс для отображения результата опроса

package ru.fastly.quizz.utils

import ru.fastly.quizz.R



enum class Points(val value:Double, val desc:String) {
    BAD(0.3, App.getContext().getString(R.string.bad)),
    OK(0.5, App.getContext().getString(R.string.norm)),
    GOOD(0.8,App.getContext().getString(R.string.good)),
    BEST(1.0,App.getContext().getString(R.string.best))
}