package com.rewire.mobile.app.PaperHelper

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.preference.PreferenceManager

import java.util.Locale

object LocaleHelper {

    val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

    fun onAttacth(context: Context): Context {
        val lang = getPersistedData(context, Locale.getDefault().language)
        return setLocale(context, lang)
    }

    fun onAttacth(context: Context, defaultLanguage: String): Context {
        val lang = getPersistedData(context, defaultLanguage)
        return setLocale(context, lang)
    }

    fun setLocale(context: Context, lang: String?): Context {
        persist(context, lang)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            updateResources(context, lang)
        } else updateResourcesLegacy(context, lang)

    }

    @SuppressWarnings("deprecation")
    private fun updateResourcesLegacy(context: Context, lang: String?): Context {
        val locale = Locale(lang)
        Locale.setDefault(locale)

        val resource = context.resources

        val config = resource.configuration
        config.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLayoutDirection(locale)
        }

        resource.updateConfiguration(config, resource.displayMetrics)

        return context
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, lang: String?): Context {
        val locale = Locale(lang)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }

    private fun persist(context: Context, lang: String?) {
        val pref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = pref.edit()

        editor.putString(SELECTED_LANGUAGE, lang)
        editor.apply()

    }

    private fun getPersistedData(context: Context, language: String): String? {

        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(SELECTED_LANGUAGE, language)

    }

}
