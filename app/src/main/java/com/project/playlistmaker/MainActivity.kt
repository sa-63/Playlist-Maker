package com.project.playlistmaker

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchBtn = findViewById<MaterialButton>(R.id.search_btn_main)
        val mediaLibraryBtn = findViewById<MaterialButton>(R.id.media_library_btn_main)
        val settingsBtn = findViewById<MaterialButton>(R.id.settings_btn_main)

        //Способ 1. Реализация анонимного класса
//        val searchBtnListener: View.OnClickListener = object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                startActivity(Intent(this@MainActivity, SearchActivity::class.java))
//            }
//        }
//        searchBtn.setOnClickListener(searchBtnListener)
//
//        val mediaLibraryBtnListener: View.OnClickListener = object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                startActivity(Intent(this@MainActivity, MediaLibraryActivity::class.java))
//            }
//        }
//        mediaLibraryBtn.setOnClickListener(mediaLibraryBtnListener)
//
//        val settingsBtnListener: View.OnClickListener = object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
//            }
//        }
//        settingsBtn.setOnClickListener(settingsBtnListener)


        //Способ 2. Лямбда-выражение
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