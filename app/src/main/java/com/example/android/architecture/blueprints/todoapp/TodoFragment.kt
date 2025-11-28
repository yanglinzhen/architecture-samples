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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.android.architecture.blueprints.todoapp.databinding.FragmentTodoBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.marginTop
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import timber.log.Timber

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
    
    // 使用ViewModel管理数据
    private val todoViewModel: TodoViewModel by viewModels()

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
        
        // 监听ViewModel数据变化
        observeViewModel()
    }
    
    /**
     * 监听ViewModel数据变化
     */
    private fun observeViewModel() {
        // 监听CardViewPagerAdapter的数据变化
        viewLifecycleOwner.lifecycleScope.launch {
            todoViewModel.cardPages.collect { pages ->
                mainContentAdapter.updateData(pages)
            }
        }
        
        // 监听TodoViewPagerAdapter的页面标题变化
        viewLifecycleOwner.lifecycleScope.launch {
            todoViewModel.todoPages.collect { pages ->
                // 更新适配器数据，保持当前页面数据不变
                adapter.updateData(pages, todoViewModel.todoPageData.value)
            }
        }
        
        // 监听TodoViewPagerAdapter的页面数据变化
        viewLifecycleOwner.lifecycleScope.launch {
            todoViewModel.todoPageData.collect { pageData ->
                // 更新适配器数据，保持当前页面标题不变
                adapter.updateData(todoViewModel.todoPages.value, pageData)
            }
        }
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
        mainContentAdapter = CardViewPagerAdapter(object : CardImageBottomCallback {
            override fun onCardImageBottomCalculated(bottomY: Int) {
                // 像素值回调（保留用于其他用途）
                Log.d("TodoFragment", "onCardImageBottomCalculated: bottomY(px)=$bottomY")
            }
            
            override fun onCardImageBottomCalculatedInDp(bottomYInDp: Int) {
                updatePeekHeight(bottomYInDp)
            }
        })
        binding.mainContentViewpager.adapter = mainContentAdapter
        
        // 添加页面切换监听器
        binding.mainContentViewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                // 同步view_pager的滚动状态
                when (state) {
                    ViewPager2.SCROLL_STATE_DRAGGING -> {
                        // 开始拖拽时，同步view_pager的状态
                    }
                    ViewPager2.SCROLL_STATE_SETTLING -> {
                        // 自动滚动到页面时，同步view_pager的状态
                    }
                    ViewPager2.SCROLL_STATE_IDLE -> {
                        // 滚动完成时，确保view_pager的页面位置一致
                        if (binding.viewPager.currentItem != binding.mainContentViewpager.currentItem) {
                            binding.viewPager.setCurrentItem(binding.mainContentViewpager.currentItem, false)
                        }
                    }
                }
            }
            
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                // 同步view_pager的滑动位置
                syncViewPagerScroll(position, positionOffset)
            }
            
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.d("TodoFragment", "onPageSelected: position=$position")
                
                // 同步view_pager的页面切换
                if (binding.viewPager.currentItem != position) {
                    binding.viewPager.setCurrentItem(position, false)
                }
                
                // 当页面切换完成后，获取该位置的bottomYInDp值并调用回调
                mainContentAdapter.getBottomYInDp(position)?.let { bottomYInDp ->
                    updatePeekHeight(bottomYInDp)
                } ?: run {
                    Log.d("TodoFragment", "onPageSelected: No bottomYInDp found for position $position")
                }
            }
        })
        
        // 禁用view_pager的用户输入，但允许程序控制
        binding.viewPager.isUserInputEnabled = false
        
        // 禁用view_pager的嵌套滚动，防止滑动冲突
        (binding.viewPager.getChildAt(0) as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
    }

    fun updatePeekHeight(bottomYInDp: Int) {
        // 获取page_container的高度（像素值）
        val pageContainerHeight = binding.pageContainer.height
        // 将page_container的高度转换为dp值
        val pageContainerHeightDp = DisplayUtils.pxToDp(requireContext(), pageContainerHeight)
        val appbarLayoutHeight = DisplayUtils.pxToDp(requireContext(), binding.appBarLayout.height + binding.mainContentViewpager.marginTop)
        // 计算page_container的高度减去bottomYInDp
        val newPeekHeight = pageContainerHeightDp - (bottomYInDp + appbarLayoutHeight)
        val newPeekHeightPx = DisplayUtils.dpToPx(requireContext(), newPeekHeight)
        // 更新bottomSheetBehavior的高度
        Timber.d("onCardImageBottomCalculatedInDp: bottomYInDp=$bottomYInDp, pageContainerHeightDp=$pageContainerHeightDp, newPeekHeight=$newPeekHeight, px=$newPeekHeightPx")
        bottomSheetBehavior.peekHeight = newPeekHeightPx
    }

    private fun setupViewPager() {
        // 设置 ViewPager2 的适配器
        adapter = TodoViewPagerAdapter()
        binding.viewPager.adapter = adapter
        
        // 禁用view_pager的左右滑动功能
        binding.viewPager.isUserInputEnabled = false
        
        // 设置view_pager的初始页面与main_content_viewpager同步
        binding.viewPager.setCurrentItem(binding.mainContentViewpager.currentItem, false)
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
    
    /**
     * 更新CardViewPagerAdapter的数据
     */
    fun updateMainContentData(newPages: List<String>) {
        todoViewModel.updateCardPages(newPages)
        // 适配器将通过StateFlow监听自动更新，无需手动调用
    }
    
    /**
     * 更新TodoViewPagerAdapter的数据
     */
    fun updateTodoViewData(newPages: List<String>, newPageData: Map<Int, List<String>>? = null) {
        // 适配器将通过StateFlow监听自动更新，无需手动调用
    }
    
    /**
     * 刷新所有数据（重新生成随机数据）
     */
    fun refreshAllData() {
        todoViewModel.refreshAllData()
        // 适配器将通过StateFlow监听自动更新，无需手动调用
    }
    
    /**
     * 同步ViewPager的滑动位置
     * @param position 当前页面位置
     * @param positionOffset 页面偏移量（0-1）
     */
    private fun syncViewPagerScroll(position: Int, positionOffset: Float) {
        try {
            // 使用反射获取ViewPager2的RecyclerView
            val recyclerView = binding.viewPager.getChildAt(0) as RecyclerView
            val layoutManager = recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
            
            // 计算目标滚动位置
            val itemWidth = recyclerView.width
            val targetScrollX = (position + positionOffset) * itemWidth
            
            // 计算当前滚动位置
            val currentScrollX = recyclerView.computeHorizontalScrollOffset()
            val scrollDelta = (targetScrollX - currentScrollX).toInt()
            
            // 滚动到目标位置
            recyclerView.scrollBy(scrollDelta, 0)
            
            // 确保页面位置正确
            if (positionOffset == 0f && binding.viewPager.currentItem != position) {
                binding.viewPager.setCurrentItem(position, false)
            }
        } catch (e: Exception) {
            Log.e("TodoFragment", "Error syncing ViewPager scroll", e)
            // 如果反射失败，使用setCurrentItem作为备选方案
            if (binding.viewPager.currentItem != position) {
                binding.viewPager.setCurrentItem(position, false)
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
