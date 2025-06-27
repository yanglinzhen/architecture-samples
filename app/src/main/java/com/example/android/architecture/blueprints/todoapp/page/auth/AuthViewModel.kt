package com.example.android.architecture.blueprints.todoapp.page.auth

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.android.architecture.blueprints.todoapp.data.entity.AuthData
import com.example.android.architecture.blueprints.todoapp.data.entity.Credentials
import com.example.android.architecture.blueprints.todoapp.data.entity.CustomNavType
import kotlin.reflect.typeOf

class AuthViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val authData: AuthData by lazy {
        savedStateHandle.toRoute<AuthData>(
            typeMap = mapOf(
//                    typeOf<AuthData>() to CustomNavType.AuthDataType
                typeOf<Credentials>() to CustomNavType.CredentialsType
            )
        )
    }
}