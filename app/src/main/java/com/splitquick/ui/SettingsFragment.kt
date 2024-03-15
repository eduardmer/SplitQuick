package com.splitquick.ui

import android.app.ActivityManager
import android.content.ActivityNotFoundException
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.splitquick.R
import com.splitquick.databinding.FragmentSettingsBinding
import com.splitquick.domain.model.Settings
import com.splitquick.ui.model.UiState
import com.splitquick.utils.launchAndRepeatWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SharedViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchAndRepeatWithLifecycle {
            viewModel.settings.collectLatest { uiState ->
                when(uiState) {
                    is UiState.Success<*> -> {
                        binding.data = uiState.data as Settings
                        binding.executePendingBindings()
                        showProgressBar(false)
                    }
                    is UiState.Error -> {
                        Toast.makeText(requireContext(), uiState.error, Toast.LENGTH_SHORT).show()
                        showProgressBar(false)
                    }
                    UiState.Loading -> showProgressBar(true)
                }
            }
        }
        binding.apply {
            darkModeSwitch.setOnCheckedChangeListener { _, value ->
                viewModel.enableDarkMode(value)
            }
            languageLayout.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.language)
                    .setSingleChoiceItems(
                        R.array.languages,
                        if (selectedLanguage.text.toString() == "English")
                            0
                        else 1
                    ) { _, index ->
                        viewModel.setLanguage(if (index == 0) "English" else "Shqip")
                        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(
                            if (index == 0)
                                "en"
                            else "sq"
                        ))
                    }
                    .show()
            }
            rateLayout.setOnClickListener {
                try {
                    startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.Splitwise.SplitwiseMobile"))
                    )
                } catch (e : ActivityNotFoundException) {
                    startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.Splitwise.SplitwiseMobile"))
                    )
                }
            }
            logOutLayout.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(R.string.log_out)
                    .setMessage(R.string.delete_data)
                    .setPositiveButton(R.string.delete) { _, _ ->
                        (context?.getSystemService(ACTIVITY_SERVICE) as ActivityManager).clearApplicationUserData()
                    }.setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    private fun showProgressBar(isVisible: Boolean) {
        binding.progressBar.isVisible = isVisible
    }

}