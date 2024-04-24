package com.example.supabase

import android.content.Context

class SharedPreferenceHelper(private val context: Context) {
    companion object{
        private const val MY_PREF_KEY = "MY_PREF"
    }

    fun saveStringData(key: String, data: String){
        val sharedPreference = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        sharedPreference.edit().putString(key,data).apply()
    }
    fun getStringData(key: String): String? {
        val sharedPreference = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreference.getString(key,null)
    }
    fun clearPreferences(){
        val sharedPreferencesHelper = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        sharedPreferencesHelper.edit().clear().apply()
    }
}