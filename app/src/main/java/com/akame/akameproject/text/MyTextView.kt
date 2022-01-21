package com.akame.akameproject.text

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class MyTextView(context: Context, attributeSet: AttributeSet) :
    AppCompatTextView(context, attributeSet) {
    var drawText = "首页"
    private val mPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        color = Color.BLACK
        textSize = 66f
    }

    private val mColorPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.RED
        isAntiAlias = true
        textSize = 66f
    }

    private val mLinePaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.RED
        strokeWidth = 3f
    }

    //滑动进度
    private var process = 0f

    //是否反转
    private var isReverse = false

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            //drawLine(this)
            drawText(this)
            drawColorText(this)
        }
    }

    private fun drawLine(canvas: Canvas) {
        canvas.drawLine(width / 2f, 0f, width / 2f, height.toFloat(), mLinePaint)
        canvas.drawLine(0f, height / 2f, width.toFloat(), height / 2f, mLinePaint)
    }

    private fun drawText(canvas: Canvas) {
        /**
         * @sample fontMetrics.top  文字最大的顶部
         * @sample fontMetrics.bottom 文字最大的底部
         * @sample fontMetrics.ascent 一般文字的顶部
         * @sample fontMetrics.bottom 一般文字的底部
         * @sample fontMetrics.leading 2行文字间距 上面一行的bottom到下面一行的top
         */
        canvas.save()
        val fontMetrics = mPaint.fontMetrics
        //文字的高度
        val textHeight = fontMetrics.descent - fontMetrics.ascent
        val textY = height / 2f + textHeight / 2 - fontMetrics.descent
        //文字的宽度
        val textWidth = mPaint.measureText(drawText)
        val textX = (width - textWidth) / 2f
        val rect = if (isReverse) getLeftToRightRect(textX, textWidth)
        else getRightToLeftRect(textX, textWidth)
        canvas.clipRect(rect)
        canvas.drawText(drawText, textX, textY, mPaint)
        canvas.restore()
    }

    private fun drawColorText(canvas: Canvas) {
        canvas.save()
        //计算文字的高度
        val fontMetrics = mColorPaint.fontMetrics
        val textHeight = fontMetrics.descent - fontMetrics.ascent
        val textY = height / 2f + textHeight / 2f - fontMetrics.descent
        val textWidth = mColorPaint.measureText(drawText)
        val textX = (width - textWidth) / 2f
        val rect = if (isReverse) getRightToLeftRect(textX, textWidth)
        else getLeftToRightRect(textX, textWidth)
        canvas.clipRect(rect)
        canvas.drawText(drawText, textX, textY, mColorPaint)
        canvas.restore()
    }

    /**
     * 从左向右偏移  --->
     */
    private fun getLeftToRightRect(textX: Float, textWidth: Float): Rect {
        return Rect(textX.toInt(), 0, (textX + textWidth * process).toInt(), height)
    }

    /**
     * 从右向左偏移  <---
     */
    private fun getRightToLeftRect(textX: Float, textWidth: Float): Rect {
        return Rect(
            (textX + process * textWidth).toInt(),
            0,
            (textX + textWidth).toInt(),
            height
        )
    }

    fun setProcess(process: Float, isReverse: Boolean = false) {
        this.process = process
        this.isReverse = isReverse
        invalidate()
    }

//    private val annotation = ValueAnimator.ofFloat(0f, 1f).apply {
//        addUpdateListener {
//            val animatedValue = it.animatedValue
//            process = animatedValue as Float
//            invalidate()
//        }
//        repeatMode = ValueAnimator.REVERSE
//        repeatCount = ValueAnimator.INFINITE
//        interpolator = LinearInterpolator()
//        duration = 3000
//    }
//
//    fun startAnimator() {
//        annotation.start()
//    }
}