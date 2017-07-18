package ru.worksolutions.worksolutionslauncher

import android.app.Application

class App: Application() {
    init {
        instance = this
    }

    companion object {
        @JvmStatic private lateinit var instance: App

        @JvmStatic fun get(): App {
            return App.instance
        }
    }

}