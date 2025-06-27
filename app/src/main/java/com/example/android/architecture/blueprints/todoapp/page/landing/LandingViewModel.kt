package com.example.android.architecture.blueprints.todoapp.page.landing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.android.architecture.blueprints.todoapp.data.entity.UserData

class LandingViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val userData by lazy {
        savedStateHandle.toRoute<UserData>()
    }
}