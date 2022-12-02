package ru.fastly.quizz.providers

import android.content.Context
import com.google.gson.Gson
import ru.fastly.quizz.data.Answer
import ru.fastly.quizz.data.QuizzData
import ru.fastly.quizz.utils.Params
import java.io.InputStream

class DataProvider(private val context:Context) {

    private lateinit var data:QuizzData

    init {
        setData()
    }

    private fun setData() {
        val inputStream: InputStream = context.assets.open(Params.DATA_FILE_NAME)
        val inputString = inputStream.bufferedReader().use { it.readText() }

        val gson = Gson()
        data = gson.fromJson(inputString, QuizzData::class.java)
    }

    fun getDataCount(): Int {
        return data.size
    }

    fun getQuestion (id:Int): String {
        return data[id].Question
    }

    fun getAnswersForQuestion(id:Int):List<Answer> {
        return data[id].Answers
    }

    fun getRightAnswerIdForQuestion(id:Int):Int {
        val answers = getAnswersForQuestion(id)
        var result:Int = -1
        for (answer in answers) {
            if (answer.valid) {
                result = answer.id
            }
        }
        return result
    }
}