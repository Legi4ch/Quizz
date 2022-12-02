/*
*  Костыль - Заморочка с доступом к ресурсам приложения
*  из классов где нет Context
* Возвращаю статику getContext и из нее берем ресуры
* в манифесте нужно указать android:name = .App
* Также здесь хранится объект данных для доступа к нему из ViewModel
*/
package ru.fastly.quizz.utils

import android.app.Application
import android.content.Context
import ru.fastly.quizz.providers.DataProvider

class App: Application() {

    companion object {
        private lateinit var instance: App
        lateinit var dataset:DataProvider

        fun getContext(): Context {
            return instance
        }
    }



    override fun onCreate() {
        super.onCreate()
        instance = this
        dataset = DataProvider(this)
    }

}