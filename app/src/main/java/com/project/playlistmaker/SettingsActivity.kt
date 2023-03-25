package com.project.playlistmaker

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("IntentReset")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val backBtn = findViewById<ImageButton>(R.id.back_imageBtn).setOnClickListener {
            onBackPressed()
        }

        val shareBtn = findViewById<ImageButton>(R.id.shareBtn).setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/android-developer/")
            }
            checkIntent(sendIntent)
        }

        val supportBtn = findViewById<ImageButton>(R.id.supportBtn).setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:${getString(R.string.developers_email)}")
                putExtra(
                    Intent.EXTRA_SUBJECT,
                    getString(R.string.message_to_developers)
                )
                putExtra(
                    Intent.EXTRA_TEXT,
                    getString(R.string.thanks_to_developers)
                )
            }
            checkIntent(sendIntent)
        }


        val termsOfUseBtn = findViewById<ImageButton>(R.id.termsOfUseBtn).setOnClickListener {
            val sendIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse("https://yandex.ru/legal/practicum_offer/")
            }
            checkIntent(sendIntent)
        }
    }

    private fun checkIntent(intent: Intent) {
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Log.w("IntentSettings", "IntentNotStarted")
        }
    }
}
