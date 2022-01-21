package com.akame.akameproject.nested

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.core.widget.NestedScrollView.OnScrollChangeListener
import androidx.recyclerview.widget.RecyclerView

class MyNestedScrollView(context: Context, attributeSet: AttributeSet) :
    NestedScrollView(context, attributeSet) {
    private lateinit var contentView: View
    private lateinit var recyclerContent: RecyclerView
    private var mHeadViewHeight = 0
    private var velocityY = 0
    private val mFlyingHelper by lazy { FlingHelper(context) }

    init {
        setOnScrollChangeListener(OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            //如果滑到底了
            if (this@MyNestedScrollView.scrollY >= mHeadViewHeight) {
                //剩下交给子view去滑动
                dispatchChildFling()
            }
        })
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        contentView = (getChildAt(0) as ViewGroup).getChildAt(1)
        recyclerContent = (contentView as ViewGroup).getChildAt(1) as RecyclerView
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        contentView.layoutParams.height = measuredHeight
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mHeadViewHeight = (getChildAt(0) as ViewGroup).getChildAt(0).measuredHeight
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        val isScroll = dy > 0 && scrollY < mHeadViewHeight
        if (isScroll) {
            scrollBy(0, dy)
            consumed[1] = dy
        }
    }

    override fun fling(velocityY: Int) {
        super.fling(velocityY)
        this.velocityY = velocityY
    }

    private fun dispatchChildFling() {
        if (velocityY != 0) {
            val splineFlingDistance = mFlyingHelper.getSplineFlingDistance(velocityY)
            if (splineFlingDistance > scrollY) {
                val childUseDy = splineFlingDistance - scrollY
                val childVelocityY = mFlyingHelper.getVelocityByDistance(childUseDy)
                recyclerContent.fling(0, childVelocityY)
            }
        }
        velocityY = 0
    }

}