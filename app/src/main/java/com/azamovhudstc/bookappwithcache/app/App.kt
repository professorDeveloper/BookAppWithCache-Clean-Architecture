package com.azamovhudstc.bookappwithcache.app

import android.app.Application
import com.azamovhudstc.bookappwithcache.data.local.database.appDatabase.AppDatabase
import com.azamovhudstc.bookappwithcache.data.local.sharedpref.AppReference

class App : Application() {
    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        AppReference.init(this)
        AppDatabase.init(this)
    }

}