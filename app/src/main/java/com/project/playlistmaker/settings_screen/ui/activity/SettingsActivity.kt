package com.project.playlistmaker.settings_screen.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.project.playlistmaker.R
import com.project.playlistmaker.databinding.ActivitySettingsBinding
import com.project.playlistmaker.settings_screen.ui.view_model.ActivitySettingsViewModel
import com.project.playlistmaker.utils.App
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding: ActivitySettingsBinding
    //ViewModel
    private val activitySettingsViewModel by viewModel<ActivitySettingsViewModel>()
    //Application
    private var app = App()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initListeners()

        binding.ibBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() }

        activitySettingsViewModel.themeSettingsState().observe(this) { themeSettings ->
            binding.smTheme.isChecked = themeSettings.darkTheme
        }
    }

    private fun initListeners() {
        binding.btnShare.setOnClickListener {
            activitySettingsViewModel.shareApp()
        }
        binding.btnSupport.setOnClickListener {
            activitySettingsViewModel.supportEmail()
        }
        binding.btnTermsOfUse.setOnClickListener {
            activitySettingsViewModel.openAgreement()
        }
        binding.smTheme.setOnCheckedChangeListener { _, checked ->
            activitySettingsViewModel.switchTheme(checked)
            app.switchTheme(checked)
        }
    }
}
