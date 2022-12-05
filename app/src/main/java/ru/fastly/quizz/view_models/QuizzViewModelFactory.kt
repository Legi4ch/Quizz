package ru.fastly.quizz.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.fastly.quizz.providers.DataProvider

class QuizzViewModelFactory constructor(private val dataset:DataProvider):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizzViewModel::class.java)) {
            return QuizzViewModel(dataset) as T
        } else {
         throw java.lang.IllegalArgumentException("ViewModel not found")
        }
    }
}