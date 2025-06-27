package com.example.android.architecture.blueprints.todoapp.data.entity

import android.os.Bundle
import androidx.navigation.NavType
import androidx.savedstate.SavedState
import com.google.gson.Gson
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val guid: String,
    val userName: String
)

@Serializable
data class AuthData(
    val type: AuthType,
    val credentials: Credentials
)

object CustomNavType {
    val CredentialsType = object : NavType<Credentials>(true) {
        override fun get(bundle: Bundle, key: String) =
            bundle.getString(key)?.let {
                Gson().fromJson(it, Credentials::class.java)
            }

        override fun parseValue(value: String): Credentials {
            return Gson().fromJson(value, Credentials::class.java)
        }

        override fun serializeAsValue(value: Credentials): String {
            return Gson().toJson(value)
        }

        override fun put(bundle: SavedState, key: String, value: Credentials) {
            bundle.putString(key, Gson().toJson(value))
        }

    }
}

@Serializable
data class Credentials(
    val userData: UserData,
    val identifier: String
)

enum class AuthType {
    PWD,
    PIN,
    BIOMETRIC
}