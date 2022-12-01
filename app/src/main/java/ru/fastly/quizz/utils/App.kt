/*
*  Костыль - Заморочка с доступом к ресурсам приложения
*  из классов где нет Context
* Возвращаю статику getContext и из нее берем ресуры
* в манифесте нужно указать android:name = .App
*/
package ru.fastly.quizz.utils

import android.app.Application
import android.content.Context

open class App: Application() {

    companion object {
        private lateinit var instance: App

        fun getContext(): Context {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}