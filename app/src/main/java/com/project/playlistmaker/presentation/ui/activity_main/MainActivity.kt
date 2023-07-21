package com.project.playlistmaker.presentation.ui.activity_main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.project.playlistmaker.R
import com.project.playlistmaker.presentation.ui.activity_media_library.MediaLibraryActivity
import com.project.playlistmaker.presentation.ui.activity_search.SearchActivity
import com.project.playlistmaker.presentation.ui.activity_settings.SettingsActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBtn = findViewById<Button>(R.id.search_btn_main)
        val mediaLibraryBtn = findViewById<Button>(R.id.media_library_btn_main)
        val settingsBtn = findViewById<Button>(R.id.settings_btn_main)

        searchBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, SearchActivity::class.java))
        }
        mediaLibraryBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, MediaLibraryActivity::class.java))
        }
        settingsBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
        }
    }
}