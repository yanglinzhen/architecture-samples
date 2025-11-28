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

import android.content.Context
import android.util.TypedValue

/**
 * 显示工具类，用于处理像素和dp之间的转换
 */
object DisplayUtils {
    
    /**
     * 将像素值转换为dp值
     * @param context 上下文
     * @param px 像素值
     * @return dp值
     */
    fun pxToDp(context: Context, px: Int): Int {
        val density = context.resources.displayMetrics.density
        return (px / density).toInt()
    }
    
    /**
     * 将dp值转换为像素值
     * @param context 上下文
     * @param dp dp值
     * @return 像素值
     */
    fun dpToPx(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }
}