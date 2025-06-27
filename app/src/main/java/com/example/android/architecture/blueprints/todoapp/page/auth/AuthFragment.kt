package com.example.android.architecture.blueprints.todoapp.page.auth

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.entity.AuthType
import com.example.android.architecture.blueprints.todoapp.data.entity.UserData
import com.example.android.architecture.blueprints.todoapp.databinding.FragmentAuthBinding

class AuthFragment : Fragment() {

    private val viewModel: AuthViewModel by viewModels()

    private lateinit var binding: FragmentAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.authData.also { data ->
            binding.password.inputType = when (data.type) {
                AuthType.PIN -> InputType.TYPE_CLASS_NUMBER
                AuthType.PWD -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                AuthType.BIOMETRIC -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.confirm.setOnClickListener {
                setFragmentResult(
                    "requestKey",
                    bundleOf(
                        "result" to arrayOf(data.toString(), binding.password.text).toString()
                    )
                )
                findNavController().navigate(
                    route = UserData("23525", "Marco"),
                    navOptions {
                        this.launchSingleTop = true
                    }
                )
//                findNavController().popBackStack(
//                    route = UserData("3", "f0wij"),
//                    inclusive = true
//                )
//                findNavController().navigate(
//
//                )
            }
        }

    }
}