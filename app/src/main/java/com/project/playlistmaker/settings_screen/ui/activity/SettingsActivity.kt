package com.project.playlistmaker.settings_screen.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import com.project.playlistmaker.R
import com.project.playlistmaker.databinding.ActivitySettingsBinding
import com.project.playlistmaker.settings_screen.ui.view_model.ActivitySettingsViewModel

class SettingsActivity : ComponentActivity() {

    //ViewBinding
    private lateinit var binding: ActivitySettingsBinding
    //ViewModel
    private lateinit var activitySettingsViewModel: ActivitySettingsViewModel

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

        binding.backImageBtnInSettings.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() }

        activitySettingsViewModel.themeSettingsState().observe(this) { themeSettings ->
            binding.themeSwitch.isChecked = themeSettings.darkTheme
        }
    }

    private fun initListeners() {
        binding.shareBtn.setOnClickListener {
            activitySettingsViewModel.shareApp()
        }
        binding.supportBtn.setOnClickListener {
            activitySettingsViewModel.supportEmail()
        }
        binding.termsOfUseBtn.setOnClickListener {
            activitySettingsViewModel.openAgreement()
        }
        binding.themeSwitch.setOnCheckedChangeListener { _, checked ->
            activitySettingsViewModel.switchTheme(checked)
        }
    }
}
