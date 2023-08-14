package com.project.playlistmaker.settings_screen.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.project.playlistmaker.R
import com.project.playlistmaker.databinding.ActivitySettingsBinding
import com.project.playlistmaker.settings_screen.ui.view_model.ActivitySettingsViewModel
import com.project.playlistmaker.utils.App

class SettingsActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding: ActivitySettingsBinding
    //ViewModel
    private var activitySettingsViewModel: ActivitySettingsViewModel? = null
    //Application
    private var app = App()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        activitySettingsViewModel =
            ViewModelProvider(
                this,
                ActivitySettingsViewModel.getViewModelFactory(this)
            )[ActivitySettingsViewModel::class.java]

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()

        binding.ibBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() }

        activitySettingsViewModel!!.themeSettingsState().observe(this) { themeSettings ->
            binding.smTheme.isChecked = themeSettings.darkTheme
        }
    }

    private fun initListeners() {
        binding.btnShare.setOnClickListener {
            activitySettingsViewModel!!.shareApp()
        }
        binding.btnSupport.setOnClickListener {
            activitySettingsViewModel!!.supportEmail()
        }
        binding.btnTermsOfUse.setOnClickListener {
            activitySettingsViewModel!!.openAgreement()
        }
        binding.smTheme.setOnCheckedChangeListener { _, checked ->
            activitySettingsViewModel!!.switchTheme(checked)
            app.switchTheme(checked)
        }
    }
}
