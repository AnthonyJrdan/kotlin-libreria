package com.example.kotlin_libreria.model

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "kotlin_libreria_prefs"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_USER_NOMBRE = "user_nombre"
    private const val KEY_TOKEN = "auth_token"

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()

    var userEmail: String?
        get() = prefs.getString(KEY_USER_EMAIL, null)
        set(value) = prefs.edit().putString(KEY_USER_EMAIL, value).apply()

    var userNombre: String?
        get() = prefs.getString(KEY_USER_NOMBRE, null)
        set(value) = prefs.edit().putString(KEY_USER_NOMBRE, value).apply()

    fun saveUserSession(email: String, nombre: String, token: String) {
        isLoggedIn = true
        userEmail = email
        userNombre = nombre
        authToken = token
    }

    var authToken: String?
        get() = prefs.getString(KEY_TOKEN, null)
        set(value) = prefs.edit().putString(KEY_TOKEN, value).apply()

    fun logout() {
        prefs.edit().clear().apply()
    }
}
