package ru.itis.bookmatch

import android.app.Application

class BookMatchApplication: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .factory()
            .create(AppModule(this))

    }
}