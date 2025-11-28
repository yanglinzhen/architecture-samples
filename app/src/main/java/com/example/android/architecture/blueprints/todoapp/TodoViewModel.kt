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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * ViewModel for managing Todo app data
 */
class TodoViewModel : ViewModel() {

    // CardViewPagerAdapter 的页面数据
    private val _cardPages = MutableStateFlow<List<String>>(
        listOf(
            "Card Page 1",
            "Card Page 2",
            "Card Page 3"
        ).map {
            val itemCount = Random.nextInt(6, 15)
            val items = (1..itemCount).joinToString("\n") { i -> "RecyclerViewItem $i" }
            "$it\n$items"
        }
    )
    val cardPages: StateFlow<List<String>> = _cardPages.asStateFlow()

    // TodoViewPagerAdapter 的页面标题
    private val _todoPages = MutableStateFlow(
        listOf(
            "Card Page 1",
            "Card Page 2",
            "Card Page 3"
        )
    )
    val todoPages: StateFlow<List<String>> = _todoPages.asStateFlow()

    // TodoViewPagerAdapter 的页面数据
    private val _todoPageData = MutableStateFlow<Map<Int, List<String>>>(emptyMap())
    val todoPageData: StateFlow<Map<Int, List<String>>> = _todoPageData.asStateFlow()

    init {
        // 初始化TodoViewPagerAdapter的页面数据
        initializeTodoPageData()
    }

    /**
     * 初始化TodoViewPagerAdapter的页面数据
     */
    private fun initializeTodoPageData() {
        viewModelScope.launch {
            generateNewTodoPageData()
        }
    }

    fun generateNewTodoPageData() {
        _todoPages.value.mapIndexed { i, s ->
            i to generateRandomItems(s)
        }.toMap().also {
            _todoPageData.value = it
        }
    }

    /**
     * 生成随机数量的项目列表
     */
    private fun generateRandomItems(pageName: String): List<String> {
        val itemCount = Random.nextInt(15, 20)
        return (1..itemCount).map { "$pageName - RecyclerViewItem $it" }
    }

    /**
     * 更新CardViewPagerAdapter的数据
     */
    fun updateCardPages(newPages: List<String>) {
        viewModelScope.launch {
            _cardPages.value = newPages
        }
    }

    /**
     * 刷新所有数据（重新生成随机数据）
     */
    fun refreshAllData() {
        viewModelScope.launch {
            // 刷新CardViewPagerAdapter数据
            _cardPages.value = listOf(
                "Card Page 1",
                "Card Page 2",
                "Card Page 3"
            ).map {
                val itemCount = Random.nextInt(6, 15)
                val items = (1..itemCount).joinToString("\n") { i -> "RecyclerViewItem $i" }
                "$it\n$items"
            }
            generateNewTodoPageData()
        }
    }
}