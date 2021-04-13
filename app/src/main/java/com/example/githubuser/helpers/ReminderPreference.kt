package com.example.githubuser.helpers

import android.content.Context

class ReminderPreference(context: Context) {

    private val preference = context.getSharedPreferences("reminder_pref", Context.MODE_PRIVATE)

    fun setReminder(isReminded: Boolean) {
        val editor = preference.edit()
        editor.putBoolean("isReminded", isReminded)
        editor.apply()
    }

    fun getReminder(): Boolean {
        return preference.getBoolean("isReminded", false)
    }

}