package com.project.playlistmaker.settingsscreen.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.playlistmaker.databinding.FragmentSettingsBinding
import com.project.playlistmaker.settingsscreen.ui.viewmodel.SettingsViewModel
import com.project.playlistmaker.utils.App
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    //ViewBinding
    private lateinit var binding: FragmentSettingsBinding

    //ViewModel
    private val settingsViewModel by viewModel<SettingsViewModel>()

    //Application
    private var app = App.newInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        settingsViewModel.themeSettingsState()
            .observe(viewLifecycleOwner) { themeSettings ->
                binding.smTheme.isChecked = themeSettings.darkTheme
            }
    }

    private fun initListeners() {
        binding.btnShare.setOnClickListener {
            settingsViewModel.shareApp()
        }
        binding.btnSupport.setOnClickListener {
            settingsViewModel.supportEmail()
        }
        binding.btnTermsOfUse.setOnClickListener {
            settingsViewModel.openAgreement()
        }
        binding.smTheme.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.switchTheme(checked)
            app.switchTheme(checked)
        }
    }
}
