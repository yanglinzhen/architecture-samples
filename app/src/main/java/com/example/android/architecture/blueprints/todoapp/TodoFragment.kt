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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.android.architecture.blueprints.todoapp.databinding.FragmentTodoBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

/**
 * Main fragment for the todoapp
 */
class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>
    private lateinit var adapter: TodoViewPagerAdapter
    private lateinit var mainContentAdapter: CardViewPagerAdapter
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupViews()
        setupMainContentViewPager()
        setupViewPager()
        setupBottomSheet()
    }

    private fun setupViews() {
        // 设置 Toolbar
        binding.toolbar.title = "Todo App"
        
        // 设置 DrawerLayout 和 Toggle
        setupDrawer()
        
        // 找到 BottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
    }

    private fun setupDrawer() {
        actionBarDrawerToggle = ActionBarDrawerToggle(
            requireActivity(),
            binding.drawerLayout,
            binding.toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        
        // 设置导航项点击监听
        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // 处理首页点击
                    binding.drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_tasks -> {
                    // 处理任务点击
                    binding.drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_completed -> {
                    // 处理已完成点击
                    binding.drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_statistics -> {
                    // 处理统计点击
                    binding.drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_settings -> {
                    // 处理设置点击
                    binding.drawerLayout.closeDrawers()
                    true
                }
                R.id.nav_about -> {
                    // 处理关于点击
                    binding.drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupMainContentViewPager() {
        // 设置主内容区域的 ViewPager2 适配器
        mainContentAdapter = CardViewPagerAdapter()
        binding.mainContentViewpager.adapter = mainContentAdapter
    }

    private fun setupViewPager() {
        // 设置 ViewPager2 的适配器
        adapter = TodoViewPagerAdapter()
        binding.viewPager.adapter = adapter
    }

    private fun setupBottomSheet() {
        // 设置 BottomSheet 的初始状态为收起（从底部开始）
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        
        // 设置高度 - 收起时的高度
        bottomSheetBehavior.peekHeight = 500 // 200dp 的peek高度
        
        // 设置滑动监听器
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        // 全屏展开时的处理
                        binding.appBarLayout.setExpanded(true, true)
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        // 收起时的处理
                        binding.appBarLayout.setExpanded(false, true)
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
                        // 拖拽中的处理
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                        // 半展开状态的处理
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // 滑动过程中的处理
                // slideOffset 从 -1 到 1，-1 表示完全收起，1 表示完全展开
            }
        })
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
