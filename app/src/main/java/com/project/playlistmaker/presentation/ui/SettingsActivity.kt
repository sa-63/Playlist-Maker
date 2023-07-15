package com.project.playlistmaker.presentation.ui

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import com.project.playlistmaker.app.App
import com.project.playlistmaker.R

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("IntentReset")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val themeSwitch = findViewById<SwitchMaterial>(R.id.theme_switch)

        when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_YES -> {
                themeSwitch.isChecked = true
            }

            AppCompatDelegate.MODE_NIGHT_NO -> {
                themeSwitch.isChecked = false
            }
        }
        themeSwitch.setOnCheckedChangeListener { switch, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        val backBtn = findViewById<ImageButton>(R.id.back_imageBtn_in_settings).setOnClickListener {
            onBackPressed()
        }

        val shareBtn = findViewById<ImageButton>(R.id.shareBtn).setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.android_course_link))
            }
            checkIntent(sendIntent)
        }

        val supportBtn = findViewById<ImageButton>(R.id.supportBtn).setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:${getString(R.string.developers_email)}")
                putExtra(
                    Intent.EXTRA_SUBJECT, getString(R.string.message_to_developers)
                )
                putExtra(
                    Intent.EXTRA_TEXT, getString(R.string.thanks_to_developers)
                )
            }
            checkIntent(sendIntent)
        }

        val termsOfUseBtn = findViewById<ImageButton>(R.id.termsOfUseBtn).setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(getString(R.string.terms_of_use_link))
            }
            checkIntent(sendIntent)
        }
    }

    private fun checkIntent(intent: Intent) {
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
            Log.w("IntentSettings", "IntentNotStarted")
        }
    }
}