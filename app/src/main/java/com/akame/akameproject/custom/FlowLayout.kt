package com.akame.akameproject.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import kotlin.math.max

class FlowLayout(context: Context, attributeSet: AttributeSet) : ViewGroup(context, attributeSet) {
    private val mHorizontalSpacing = 20 //定义每个子view之间的左右间距
    private val mVerticalSpacing = 20 //定义每个子view之间的上下间距
    private val allLines = arrayListOf<List<View>>()
    private val lineHeights = arrayListOf<Int>()

    private fun cleanParams() {
        allLines.clear()
        lineHeights.clear()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (childCount == 0) {
            return super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
        cleanParams()
        // 每一行子view使用的总宽度
        var lineWidthUsed = 0
        //每一行子view的最大的高度
        var lineHeight = 0
        //viewGroup解析父亲给自己的参考宽高
        val selfWidth = MeasureSpec.getSize(widthMeasureSpec)
        val selfHeight = MeasureSpec.getSize(heightMeasureSpec)

        //父view需要的真是宽度 就是每一行所有子view宽度最大值
        var parentRealNeedWith = 0
        //父view需要的真实高度
        var parentRealNeedHeight = 0
        //一行所有的view
        val lineViews = arrayListOf<View>()
        for (i in 0..childCount) {
            //拿到每个子view
            val childView = getChildAt(i) ?: continue
            //获取每个子view的在xml定义的宽高值: android:layout_width = "" ...
            val childLayoutParams = childView.layoutParams ?: continue
            //得到每个子view真是的测量标准
            val childWidthMeasureSpec = getChildMeasureSpec(
                widthMeasureSpec,
                paddingLeft + paddingRight,
                childLayoutParams.width
            )
            val childHeightMeasureSpec = getChildMeasureSpec(
                heightMeasureSpec,
                paddingTop + paddingBottom,
                childLayoutParams.height
            )
            //测量子view
            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec)
            //获取子view测量后的宽高
            val childMeasuredWidth = childView.measuredWidth
            val childMeasuredHeight = childView.measuredHeight

            //判断是否需要换行 用这一行子view的宽度  - 父view宽度 ？
            val linChildWidthUsed = lineWidthUsed + childMeasuredWidth + mHorizontalSpacing
            if (linChildWidthUsed > selfWidth) {
                //需要换行
                //先记录这一行所有的view 这样添加的是上一个所有的view 本次循环的lineViews并没有被添加。所以会漏掉一行
                val views = arrayListOf<View>()
                views.addAll(lineViews)
                allLines.add(views)
                //记录这一行的最大高度
                lineHeights.add(lineHeight)

                parentRealNeedWith = max(lineWidthUsed + mHorizontalSpacing, parentRealNeedWith)
                parentRealNeedHeight += lineHeight + mVerticalSpacing

                //换行：清除这一行记录
                lineViews.clear()
                lineWidthUsed = 0
                lineHeight = 0
            }
            lineWidthUsed += childMeasuredWidth + mHorizontalSpacing
            lineViews.add(childView)
            lineHeight = max(lineHeight, childMeasuredHeight)
            //处理最后一行
            if (i == childCount - 1) {
                allLines.add(lineViews)
                lineHeights.add(lineHeight)
                parentRealNeedWith = max(lineWidthUsed + mHorizontalSpacing, parentRealNeedWith)
                parentRealNeedHeight += lineHeight + mVerticalSpacing
            }
        }

        // 测量自己  父view
        //获取父view的测量模式
        val parentWithMode = MeasureSpec.getMode(widthMeasureSpec)
        val parentHeightMode = MeasureSpec.getMode(heightMeasureSpec)
        // 如果父view是确定的宽度或者高度 那么不需要用真实的高度。显示不了就截取
        val parentWith =
            if (parentWithMode == MeasureSpec.EXACTLY) selfWidth else parentRealNeedWith
        val parentHeight =
            if (parentHeightMode == MeasureSpec.EXACTLY) selfHeight else parentHeightMode
        //保存父view的宽高
        setMeasuredDimension(parentWith, parentHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var curT = paddingTop
        var curL = paddingLeft
        allLines.forEachIndexed { index, list ->
            list.forEach {
                val left = curL
                val top = curT
                val right = left + it.measuredWidth
                val button = top + it.measuredHeight
                it.layout(left, top, right, button)
                //布局一个view 延长Left的边界
                curL = right + mHorizontalSpacing
            }
            //换行的时候需要重置top 和 left
            curT += mVerticalSpacing + lineHeights[index]
            curL = paddingLeft
        }
    }

}