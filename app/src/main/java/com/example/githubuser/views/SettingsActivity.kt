package com.example.githubuser.views

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivitySettingsBinding
import com.example.githubuser.helpers.AlarmReceiver
import com.example.githubuser.helpers.ReminderPreference

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySettingsBinding
    private val alarmReceiver = AlarmReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setTitle(R.string.settings)

        val reminderPreference = ReminderPreference(this)
        binding.switchReminder.isChecked = reminderPreference.getReminder()

        binding.switchReminder.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                saveReminder(true)
                alarmReceiver.setRepeatingAlarm(
                    this,
                    getString(R.string.repeating_alarm),
                    getString(R.string.nine),
                    getString(R.string.github_reminder)
                )

            } else {
                saveReminder(false)
                alarmReceiver.cancelAlarm(this)
            }
        }

        binding.btnSetLanguage.setOnClickListener(this)
    }

    private fun saveReminder(state: Boolean) {
        val reminderPreference = ReminderPreference(this)
        reminderPreference.setReminder(state)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_set_language -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
        }
    }
}