package com.akame.akameproject.fish

import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator
import kotlin.math.cos
import kotlin.math.sin

/**
 * 一条鱼
 */
class FishDrawable : Drawable() {
    private val mPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true //设置抗锯齿
        isDither = true //设置防抖动
        color = Color.parseColor("#CCFF9A9E")
    }

    private val mPath = Path()

    //鱼的重心
    private val middlePoint: PointF

    //鱼的朝向角度
    private var fishMainAngle = 0f

    private var annotationValue = 0f

    /**
     * 鱼的长度值
     */
    //鱼头的半径
    private val HEAD_RADIUS = 100f

    //鱼身的长度
    private val BODY_LENGTH = HEAD_RADIUS * 3.2f

    //寻找鱼鳍起始点坐标的线长
    private val FIND_FINS_LENGTH = HEAD_RADIUS * 0.9f

    //鱼鳍的长度
    private val FINS_LENGTH = HEAD_RADIUS * 1.3f

    //大圆的半径
    private val BIG_CIRCLE_RADIUS = HEAD_RADIUS * 0.6f

    //中圆的半径
    private val MIDDLE_CIRCLE_RADIUS = BIG_CIRCLE_RADIUS * 0.6f

    //小圆的半径
    private val SMALL_CIRCLE_RADIUS = MIDDLE_CIRCLE_RADIUS * 0.4f

    //寻找尾部中心圆圆心的线长
    private val FIND_MIDDLE_CIRCLE_LENGTH = BIG_CIRCLE_RADIUS * (0.6f + 1)

    //寻找尾部小圆圆心的线长
    private val FIND_SMALL_CIRCLE_LENGTH = MIDDLE_CIRCLE_RADIUS * (0.4f + 2.7f)

    //寻找大三角底边中心点的线长
    private val FIND_TRIANGLE_LENGTH = MIDDLE_CIRCLE_RADIUS * 2.7f

    init {
        middlePoint = PointF(4.19f * HEAD_RADIUS, 4.19f * HEAD_RADIUS)
    }

    override fun draw(canvas: Canvas) {
        val fishAngle = fishMainAngle
        //绘制鱼头
        val headPoint = calculatePoint(middlePoint, BODY_LENGTH / 2, fishAngle)
        canvas.drawCircle(headPoint.x, headPoint.y, HEAD_RADIUS, mPaint)
        //绘制右鱼鳍
        val rightFinsPoint = calculatePoint(headPoint, FIND_FINS_LENGTH, fishAngle - 110)
        makeFins(canvas, rightFinsPoint, fishAngle, true)
        //绘制左鱼鳍
        val leftFinsPoint = calculatePoint(headPoint, FIND_FINS_LENGTH, fishAngle + 110)
        makeFins(canvas, leftFinsPoint, fishAngle, false)
        //画节肢1
        //获取节肢的中心点
        val bodyBottomCenterPoint = calculatePoint(headPoint, BODY_LENGTH, fishAngle - 180)
        val middleCenterPoint =
            makeSegment(
                canvas,
                bodyBottomCenterPoint,
                BIG_CIRCLE_RADIUS,
                MIDDLE_CIRCLE_RADIUS,
                FIND_MIDDLE_CIRCLE_LENGTH,
                fishAngle,
                true
            )
        //绘制节肢2
        makeSegment(
            canvas,
            middleCenterPoint,
            MIDDLE_CIRCLE_RADIUS,
            SMALL_CIRCLE_RADIUS,
            FIND_SMALL_CIRCLE_LENGTH,
            fishAngle,
            false
        )
        //画尾巴
        makeTriangel(canvas, middleCenterPoint, FIND_TRIANGLE_LENGTH, BIG_CIRCLE_RADIUS, fishAngle)
        //小尾巴
        makeTriangel(
            canvas,
            middleCenterPoint,
            FIND_TRIANGLE_LENGTH - 10,
            BIG_CIRCLE_RADIUS - 20,
            fishAngle
        )
        //画身体
        makeBody(canvas, headPoint, bodyBottomCenterPoint, fishAngle)
    }

    private fun makeBody(
        canvas: Canvas,
        headPoint: PointF,
        bodyBottomCenterPoint: PointF,
        fishAngle: Float
    ) {
        //身体的4个点都求出来
        val topLeftPoint = calculatePoint(headPoint, HEAD_RADIUS, fishAngle + 90)
        val topRightPoint = calculatePoint(headPoint, HEAD_RADIUS, fishAngle - 90)
        val bottomLeftPoint =
            calculatePoint(bodyBottomCenterPoint, BIG_CIRCLE_RADIUS, fishAngle + 90)
        val bottomRightPoint =
            calculatePoint(bodyBottomCenterPoint, BIG_CIRCLE_RADIUS, fishAngle - 90)

        //鱼身的腰线
        val controlLeft = calculatePoint(headPoint, BODY_LENGTH * 0.56f, fishAngle + 130)
        val controlRight = calculatePoint(headPoint, BODY_LENGTH * 0.56f, fishAngle - 130)
        mPath.reset()
        mPath.moveTo(topLeftPoint.x, topLeftPoint.y)
        mPath.quadTo(controlLeft.x, controlLeft.y, bottomLeftPoint.x, bottomLeftPoint.y)
        mPath.lineTo(bottomRightPoint.x, bottomRightPoint.y)
        mPath.quadTo(controlRight.x, controlRight.y, topRightPoint.x, topRightPoint.y)
        mPaint.alpha = 160
        canvas.drawPath(mPath, mPaint)
    }

    private fun makeTriangel(
        canvas: Canvas,
        startPoint: PointF,
        findCenterLength: Float,
        findEdgeLength: Float,
        fishAngle: Float
    ) {
        //三角形底边中心坐标
        val centerPoint = calculatePoint(startPoint, findCenterLength, fishAngle - 180)
        val leftPoint = calculatePoint(centerPoint, findEdgeLength, fishAngle + 90)
        val rightPoint = calculatePoint(centerPoint, findEdgeLength, fishAngle - 90)
        mPath.reset()
        mPath.moveTo(startPoint.x, startPoint.y)
        mPath.lineTo(leftPoint.x, leftPoint.y)
        mPath.lineTo(rightPoint.x, rightPoint.y)
        canvas.drawPath(mPath, mPaint)
    }

    private fun makeSegment(
        canvas: Canvas,
        bottomCenterPoint: PointF,
        bigRadius: Float,
        smallRadius: Float,
        findSmallCircleLength: Float,
        fishAngle: Float,
        hasBigCircle: Boolean
    ): PointF {
        //获取上底圆的圆心
        val upperCenterPoint =
            calculatePoint(bottomCenterPoint, findSmallCircleLength, fishAngle - 180)
        val bottomLeftPoint = calculatePoint(bottomCenterPoint, bigRadius, fishAngle + 90)
        val bottomRightPoint = calculatePoint(bottomCenterPoint, bigRadius, fishAngle - 90)
        val upperLeftPoint = calculatePoint(upperCenterPoint, smallRadius, fishAngle + 90)
        val upperRightPoint = calculatePoint(upperCenterPoint, smallRadius, fishAngle - 90)
        if (hasBigCircle) {
            canvas.drawCircle(bottomCenterPoint.x, bottomCenterPoint.y, bigRadius, mPaint)
        }
        canvas.drawCircle(upperCenterPoint.x, upperCenterPoint.y, smallRadius, mPaint)
        //画梯形
        mPath.reset()
        mPath.moveTo(upperLeftPoint.x, upperLeftPoint.y)
        mPath.lineTo(upperRightPoint.x, upperRightPoint.y)
        mPath.lineTo(bottomRightPoint.x, bottomRightPoint.y)
        mPath.lineTo(bottomLeftPoint.x, bottomLeftPoint.y)
        canvas.drawPath(mPath, mPaint)
        return upperCenterPoint
    }

    /**
     * 绘制鱼鳍
     */
    private fun makeFins(canvas: Canvas, startPoint: PointF, fishAngle: Float, isRight: Boolean) {
        val controlAngle = 115f
        //需要绘制贝塞尔曲线
        //鱼鳍的终点。靠近身体下方的点
        val endPoint = calculatePoint(startPoint, FINS_LENGTH, fishAngle - 180)
        //贝塞尔曲线的控制点
        val controlPoint = calculatePoint(
            startPoint, FINS_LENGTH * 1.8f,
            if (isRight) fishAngle - controlAngle else fishAngle + controlAngle
        )
        mPath.reset()
        mPath.moveTo(startPoint.x, startPoint.y)
        mPath.quadTo(controlPoint.x, controlPoint.y, endPoint.x, endPoint.y)
        canvas.drawPath(mPath, mPaint)
    }

    /**
     * 获取某个点偏移的角度和长度对应的x、y的坐标
     *      |  /
     *  ____|/____
     *      |
     *  @param startPoint 原点坐标
     *  @param length 斜线的长度
     *  @param angle 斜线距离X轴的长度
     */
    private fun calculatePoint(startPoint: PointF, length: Float, angle: Float): PointF {
        val deltaX = cos(Math.toRadians(angle.toDouble())) * length
        val deltaY = sin(Math.toRadians((angle - 180).toDouble())) * length
        return PointF(startPoint.x + deltaX.toFloat(), startPoint.y + deltaY.toFloat())
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun getIntrinsicHeight(): Int {
        return (8.38f * HEAD_RADIUS).toInt()
    }

    override fun getIntrinsicWidth(): Int {
        return intrinsicHeight
    }

    val annotation = ValueAnimator.ofFloat(0f, 1f).apply {
        addUpdateListener {
            annotationValue = (it.animatedValue as Float) * 360
            invalidateSelf()
        }
        interpolator = LinearInterpolator()
        repeatMode = ValueAnimator.RESTART
        repeatCount = ValueAnimator.INFINITE
        duration = 2000
    }

    fun startAnimator() {
        annotation.start()
    }
}