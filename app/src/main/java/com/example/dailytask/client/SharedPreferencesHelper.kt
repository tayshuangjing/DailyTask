package com.example.dailytask.client

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        const val USERNAME_KEY = "username"
    }

    var username: String?
        get() = sharedPreferences.getString(USERNAME_KEY, null)
        set(value) {
            editor.putString(USERNAME_KEY, value)
            editor.apply()
        }
}