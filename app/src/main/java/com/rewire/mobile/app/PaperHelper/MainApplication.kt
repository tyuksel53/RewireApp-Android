package com.rewire.mobile.app.PaperHelper

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex

class MainApplication : Application() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttacth(base, "en"))
        MultiDex.install(base)
    }
}
