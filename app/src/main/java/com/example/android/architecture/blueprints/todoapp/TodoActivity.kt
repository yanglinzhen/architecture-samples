/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.architecture.blueprints.todoapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.createGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import androidx.navigation.fragment.fragment
import com.example.android.architecture.blueprints.todoapp.data.entity.AuthData
import com.example.android.architecture.blueprints.todoapp.data.entity.Credentials
import com.example.android.architecture.blueprints.todoapp.data.entity.CustomNavType
import com.example.android.architecture.blueprints.todoapp.data.entity.UserData
import com.example.android.architecture.blueprints.todoapp.page.auth.AuthFragment
import com.example.android.architecture.blueprints.todoapp.page.landing.LandingFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.reflect.typeOf

/**
 * Main activity for the todoapp
 */
@AndroidEntryPoint
class TodoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.nav_host)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        val navController = findNavController(R.id.nav_host)

        navController.graph = navController.createGraph(
            startDestination = UserData("1", "Jake")
        ) {
            fragment<LandingFragment, UserData> {
                label = "Landing"
                this.route.also {
                    Timber.d("route: $it")
                }
            }
            fragment<AuthFragment, AuthData>(
                typeMap = mapOf(
                    typeOf<Credentials>() to CustomNavType.CredentialsType
                )
            ) {
                label = "Auth"
            }
        }
    }
}
