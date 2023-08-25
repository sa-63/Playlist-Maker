package com.project.playlistmaker.settings_screen.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.playlistmaker.databinding.FragmentSettingsBinding
import com.project.playlistmaker.settings_screen.ui.view_model.ActivitySettingsViewModel
import com.project.playlistmaker.utils.App
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentSettings : Fragment() {

    //ViewBinding
    private lateinit var binding: FragmentSettingsBinding

    //ViewModel
    private val activitySettingsViewModel by viewModel<ActivitySettingsViewModel>()

    //Application
    private var app = App()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        activitySettingsViewModel.themeSettingsState()
            .observe(viewLifecycleOwner) { themeSettings ->
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
