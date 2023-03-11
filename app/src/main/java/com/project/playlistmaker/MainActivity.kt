package com.project.playlistmaker

import android.os.Bundle
import android.widget.Toast
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
//        val btnClickListener: View.OnClickListener = object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                Toast.makeText(this@MainActivity, "Клик!", Toast.LENGTH_SHORT).show()
//            }
//        }
//        searchBtn.setOnClickListener(btnClickListener)
//        mediaLibraryBtn.setOnClickListener(btnClickListener)
//        settingsBtn.setOnClickListener(btnClickListener)

        //Способ 2. Лямбда-выражение
        searchBtn.setOnClickListener {
            Toast.makeText(this@MainActivity, "Клик! Поиск", Toast.LENGTH_SHORT).show()
        }
        mediaLibraryBtn.setOnClickListener {
            Toast.makeText(this@MainActivity, "Клик! Медиатека", Toast.LENGTH_SHORT).show()
        }
        settingsBtn.setOnClickListener {
            Toast.makeText(this@MainActivity, "Клик! Настройки", Toast.LENGTH_SHORT).show()
        }
    }
}