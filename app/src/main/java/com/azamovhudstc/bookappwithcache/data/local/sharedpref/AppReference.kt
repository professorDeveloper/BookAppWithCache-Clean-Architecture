package com.azamovhudstc.bookappwithcache.data.local.sharedpref


import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class AppReference private constructor() {

    companion object {
        private lateinit var sharedPref: SharedPreferences
        private lateinit var editor: SharedPreferences.Editor
        private var shp: AppReference? = null

        fun init(context: Context) {
            shp = AppReference()
            sharedPref = context.getSharedPreferences("auth", MODE_PRIVATE)
            editor = sharedPref.edit()
        }

        fun getInstance() = shp!!
    }

    fun setToken(token: String) {
        editor.putString("TOKEN", token)
        editor.apply()
    }

    fun getToken(): String? {
        return sharedPref.getString("TOKEN", "")
    }

    var startScreen: String
        set(value) = sharedPref.edit().putString("INTRO", value).apply()
        get() = sharedPref.getString("INTRO", "INTRO")!!

    var userName: String
        set(value) = sharedPref.edit().putString("USERNAME", value).apply()
        get() = sharedPref.getString("USERNAME", "USERNAME")!!

    var verifyToken: String
        set(value) = sharedPref.edit().putString("VERIFY_TOKEN", value).apply()
        get() = sharedPref.getString("VERIFY_TOKEN", "")!!

    fun clear() {
        editor.clear()
    }
}