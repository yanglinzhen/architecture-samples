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

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.architecture.blueprints.todoapp.databinding.ViewpagerMainPageBinding
import com.example.android.architecture.blueprints.todoapp.databinding.ViewpagerPageBinding

/**
 * Callback interface for card image bottom coordinate
 */
interface CardImageBottomCallback {
    fun onCardImageBottomCalculated(bottomY: Int)
    fun onCardImageBottomCalculatedInDp(bottomYInDp: Int)
}

/**
 * ViewPager2 adapter for displaying cards with centered images
 */
class CardViewPagerAdapter(
    private val callback: CardImageBottomCallback? = null
) : RecyclerView.Adapter<CardViewPagerAdapter.ViewHolder>() {

    private var pages = listOf<String>()
    
    // 保存每个位置的bottomYInDp值
    private val bottomYInDpMap = mutableMapOf<Int, Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewpagerMainPageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pages[position], this, position)
    }

    override fun getItemCount(): Int = pages.size
    
    /**
     * 获取指定位置的bottomYInDp值
     */
    fun getBottomYInDp(position: Int): Int? = bottomYInDpMap[position]
    
    /**
     * 通知指定位置的bottomYInDp值已更新
     */
    fun notifyBottomYInDpCalculated(position: Int, bottomYInDp: Int) {
        Log.d("CardViewPagerAdapter", "notifyBottomYInDpCalculated: position=$position, bottomYInDp=$bottomYInDp")
        bottomYInDpMap[position] = bottomYInDp
        callback?.onCardImageBottomCalculatedInDp(bottomYInDp)
    }
    
    /**
     * 更新适配器数据
     * @param newPages 新的页面数据列表
     */
    fun updateData(newPages: List<String>) {
        pages = newPages
        // 清空之前的bottomYInDp缓存
        bottomYInDpMap.clear()
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ViewpagerMainPageBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(pageTitle: String, adapter: CardViewPagerAdapter, position: Int) {
            Log.d("CardViewPagerAdapter", "bind: position=$position, pageTitle=$pageTitle")
            // 设置页面标题
            binding.pageTitle.text = pageTitle
            
            // 在视图布局完成后计算card_image的底部坐标
            binding.root.post {
                val bottomY = binding.pageTitle.height + binding.cardImage.height
                Log.d("CardViewPagerAdapter", "bind: position=$position, bottomY(px)=$bottomY")
                // 将像素值转换为dp值
                val bottomYInDp = DisplayUtils.pxToDp(binding.root.context, bottomY)
                Log.d("CardViewPagerAdapter", "bind: position=$position, bottomYInDp=$bottomYInDp")

                // 保存并通知bottomYInDp值
                adapter.notifyBottomYInDpCalculated(position, bottomYInDp)
            }
        }
    }
}