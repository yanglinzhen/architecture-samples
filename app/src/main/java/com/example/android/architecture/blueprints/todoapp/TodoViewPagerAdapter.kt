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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.architecture.blueprints.todoapp.databinding.ViewpagerPageBinding

/**
 * Simple ViewPager2 adapter for demo purposes
 */
class TodoViewPagerAdapter : RecyclerView.Adapter<TodoViewPagerAdapter.ViewHolder>() {

    private var pages = listOf<String>()
    
    // 存储每个页面的数据
    private var pageData = mapOf<Int, List<String>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewpagerPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pages[position], pageData[position] ?: emptyList())
    }

    override fun getItemCount(): Int = pages.size

    /**
     * 更新适配器数据
     * @param newPages 新的页面标题列表
     * @param newPageData 新的页面数据映射（可选）
     */
    fun updateData(newPages: List<String>, newPageData: Map<Int, List<String>>? = null) {
        pages = newPages
        pageData = newPageData ?: emptyMap()
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ViewpagerPageBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(pageTitle: String, items: List<String>) {
            // 创建并设置 RecyclerView 适配器
            val recyclerViewAdapter = RecyclerViewAdapter(items)
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = recyclerViewAdapter
            }
        }
    }
}
