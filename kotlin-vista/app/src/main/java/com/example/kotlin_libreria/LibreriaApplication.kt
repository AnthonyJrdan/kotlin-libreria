package com.example.kotlin_libreria

import android.app.Application
import com.example.kotlin_libreria.model.SessionManager

class LibreriaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SessionManager.init(this)
    }
}
