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
import kotlin.random.Random

/**
 * ViewModel for managing Todo app data
 */
class TodoViewModel : ViewModel() {
    
    // CardViewPagerAdapter 的页面数据
    private var _cardPages = listOf(
        "Card Page 1",
        "Card Page 2",
        "Card Page 3"
    ).map {
        val itemCount = Random.nextInt(6, 15)
        val items = (1..itemCount).joinToString("\n") { i -> "RecyclerViewItem $i" }
        "$it\n$items"
    }
    val cardPages get() = _cardPages
    
    // TodoViewPagerAdapter 的页面标题
    private var _todoPages = listOf(
        "Page 1: Tasks",
        "Page 2: Statistics",
        "Page 3: Settings"
    )
    val todoPages get() = _todoPages
    
    // TodoViewPagerAdapter 的页面数据
    private var _todoPageData = mutableMapOf<Int, List<String>>()
    
    init {
        // 初始化TodoViewPagerAdapter的页面数据
        for (i in _todoPages.indices) {
            _todoPageData[i] = generateRandomItems()
        }
    }
    
    val todoPageData: Map<Int, List<String>> get() = _todoPageData
    
    /**
     * 生成随机数量的项目列表
     */
    private fun generateRandomItems(): List<String> {
        val itemCount = Random.nextInt(15, 20)
        return (1..itemCount).map { "RecyclerViewItem $it" }
    }
    
    /**
     * 更新CardViewPagerAdapter的数据
     */
    fun updateCardPages(newPages: List<String>) {
        _cardPages = newPages
    }
    
    /**
     * 更新TodoViewPagerAdapter的数据
     */
    fun updateTodoPages(newPages: List<String>, newPageData: Map<Int, List<String>>? = null) {
        _todoPages = newPages
        
        // 如果提供了新数据，则使用新数据，否则为每个页面生成随机数据
        if (newPageData != null) {
            _todoPageData = newPageData.toMutableMap()
        } else {
            _todoPageData.clear()
            // 为每个页面生成随机数据
            for (i in newPages.indices) {
                _todoPageData[i] = generateRandomItems()
            }
        }
    }
    
    /**
     * 刷新所有数据（重新生成随机数据）
     */
    fun refreshAllData() {
        // 刷新CardViewPagerAdapter数据
        _cardPages = listOf(
            "Card Page 1",
            "Card Page 2",
            "Card Page 3"
        ).map {
            val itemCount = Random.nextInt(6, 15)
            val items = (1..itemCount).joinToString("\n") { i -> "RecyclerViewItem $i" }
            "$it\n$items"
        }
        
        // 刷新TodoViewPagerAdapter数据
        _todoPageData.clear()
        for (i in _todoPages.indices) {
            _todoPageData[i] = generateRandomItems()
        }
    }
}