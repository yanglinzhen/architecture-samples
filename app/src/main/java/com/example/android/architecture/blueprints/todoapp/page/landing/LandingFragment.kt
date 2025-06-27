package com.example.android.architecture.blueprints.todoapp.page.landing

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.example.android.architecture.blueprints.todoapp.data.entity.AuthData
import com.example.android.architecture.blueprints.todoapp.data.entity.AuthType
import com.example.android.architecture.blueprints.todoapp.data.entity.Credentials
import com.example.android.architecture.blueprints.todoapp.databinding.FragmentLandingBinding
import timber.log.Timber

class LandingFragment : Fragment() {

    private lateinit var binding: FragmentLandingBinding

    private val viewModel: LandingViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLandingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title.text = viewModel.userData.userName
        binding.text.text = viewModel.userData.guid
        binding.button.setOnClickListener {

            setFragmentResultListener("requestKey") { k, b ->
                Timber.tag("TAG").d("onViewCreated - k: $k, b: ${b.getString("result")}")
            }

            findNavController().navigate(
                route = AuthData(
                    AuthType.PIN,
                    Credentials(
                        viewModel.userData,
                        ""
                    )
                )
            )
        }
    }
}