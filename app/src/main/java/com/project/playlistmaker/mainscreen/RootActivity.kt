package com.project.playlistmaker.mainscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.project.playlistmaker.R
import com.project.playlistmaker.databinding.ActivityRootBinding
import com.project.playlistmaker.utils.ArgsTransfer

class RootActivity : AppCompatActivity(), ArgsTransfer {

    private lateinit var binding: ActivityRootBinding
    private var bundleArgs: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.root_view_container) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavView.setupWithNavController(navController)
    }

    override fun postArgs(args: Bundle?) {
        bundleArgs = args
    }

    override fun getArgs(): Bundle? {
        return bundleArgs
    }

    companion object {
        const val BUNDLE_ARGS = "args"
    }
}