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
import kotlin.random.Random

/**
 * Simple ViewPager2 adapter for demo purposes
 */
class TodoViewPagerAdapter : RecyclerView.Adapter<TodoViewPagerAdapter.ViewHolder>() {

    private val pages = listOf(
        "Page 1: Tasks",
        "Page 2: Statistics",
        "Page 3: Settings"
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewpagerPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        // 生成随机数量的项目（5-10个）
        val itemCount = Random.nextInt(15, 20)
        val items = (1..itemCount).map { "RecyclerViewItem $it" }

        // 创建并设置 RecyclerView 适配器
        val recyclerViewAdapter = RecyclerViewAdapter(items)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerViewAdapter
        }

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pages[position])
    }

    override fun getItemCount(): Int = pages.size

    class ViewHolder(private val binding: ViewpagerPageBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(pageTitle: String) {
        }
    }
}
