package com.example.nfccard

import android.content.Context
import android.content.SharedPreferences

class Preferences(private val context: Context) {

    private var sharedPref: SharedPreferences = context.getSharedPreferences("SHARED_PREFS", Context.MODE_PRIVATE)


    var userId:String?
        get() = sharedPref.getString("userId", "")
        set(value) {
            sharedPref.edit().putString("userId", value).apply()
        }


}