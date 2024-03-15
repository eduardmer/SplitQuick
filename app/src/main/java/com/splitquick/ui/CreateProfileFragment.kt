package com.splitquick.ui

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.splitquick.R
import com.splitquick.databinding.FragmentCreateProfileBinding
import com.splitquick.domain.model.User
import com.splitquick.ui.model.UiState
import com.splitquick.utils.content
import com.splitquick.utils.isEmpty
import com.splitquick.utils.launchAndRepeatWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CreateProfileFragment : Fragment() {

    private lateinit var binding: FragmentCreateProfileBinding
    private val viewModel: SharedViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCreateProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        launchAndRepeatWithLifecycle {
            viewModel.user.collectLatest { uiState ->
                when(uiState) {
                    is UiState.Success<*> -> {
                        if (!(uiState.data as User).isEmpty())
                            findNavController().navigate(CreateProfileFragmentDirections.actionCreateProfileFragmentToGroupsFragment())
                    }
                    is UiState.Error -> Toast.makeText(requireContext(), uiState.error, Toast.LENGTH_SHORT).show()
                    UiState.Loading -> Log.i("CreateProfileLog", "loading")
                }
            }
        }
        binding.apply {
            firstNameText.doAfterTextChanged {
                isButtonEnabled(it.toString().trim(), lastNameText.content(), emailText.content())
            }
            lastNameText.doAfterTextChanged {
                isButtonEnabled(firstNameText.content(), it.toString().trim(), emailText.content())
            }
            emailText.doAfterTextChanged {
                isButtonEnabled(firstNameText.content(), lastNameText.content(), it.toString().trim())
            }
            confirmButton.setOnClickListener {
                if (firstNameText.isEmpty() || lastNameText.isEmpty())
                    Toast.makeText(requireContext(), R.string.name_not_valid, Toast.LENGTH_SHORT).show()
                else if (!Patterns.EMAIL_ADDRESS.matcher(emailText.content()).matches())
                    Toast.makeText(requireContext(), R.string.email_not_valid, Toast.LENGTH_SHORT).show()
                else
                    viewModel.saveUser(
                        firstNameText.content(),
                        lastNameText.content(),
                        emailText.content()
                    )
            }
        }
    }

    private fun isButtonEnabled(firstName: String, lastName: String, email: String) {
        binding.confirmButton.isEnabled = firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty()
    }

}