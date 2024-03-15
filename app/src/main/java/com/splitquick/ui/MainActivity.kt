package com.splitquick.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.splitquick.R
import com.splitquick.databinding.ActivityMainBinding
import com.splitquick.ui.model.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen().apply {
            setKeepOnScreenCondition {
                true
            }
        }
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.configurations.collectLatest {
                    when(it) {
                        is UiState.Success<*> -> {
                            AppCompatDelegate.setDefaultNightMode(
                                if (it.data as Boolean)
                                    AppCompatDelegate.MODE_NIGHT_YES
                                else AppCompatDelegate.MODE_NIGHT_NO
                            )
                            splashScreen.setKeepOnScreenCondition {
                                false
                            }
                        }
                        is UiState.Error -> {
                            Toast.makeText(this@MainActivity, it.error, Toast.LENGTH_SHORT).show()
                            splashScreen.setKeepOnScreenCondition {
                                false
                            }
                        }
                        UiState.Loading -> Log.i("MainActivityLog", "loading")
                    }
                }
            }
        }
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.groupsFragment || destination.id == R.id.activitesFragment || destination.id == R.id.settingsFragment) {
                binding.bottomNavigation.visibility = View.VISIBLE
                binding.extendedFab.isVisible = destination.id != R.id.settingsFragment
            }
            else {
                binding.bottomNavigation.visibility = View.GONE
                binding.extendedFab.isVisible = false
            }
        }
        binding.extendedFab.setOnClickListener {
            navController.navigate(R.id.addExpensesFragment)
        }
    }

}