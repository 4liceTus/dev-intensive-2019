package ru.skillbranch.devintensive

import android.app.Application
import android.content.Context

class App: Application() {

    companion object {
        private var instance: App? = null

        fun applicationContex(): Context {
            return instance!!.applicationContext
        }
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
    }
}