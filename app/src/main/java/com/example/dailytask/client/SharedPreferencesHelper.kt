package com.example.dailytask.client

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        const val USER_ID_KEY = "user_id"
    }

    var userId: String?
        get() = sharedPreferences.getString(USER_ID_KEY, null)
        set(value) {
            editor.putString(USER_ID_KEY, value)
            editor.apply()
        }
}
