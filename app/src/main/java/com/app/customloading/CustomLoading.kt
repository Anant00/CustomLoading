package com.app.customloading

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.Transformation

class CustomLoading: View {

    private var dotDistance = 50
    private var actualDotRadius = 8
    private  lateinit var  typedArray: TypedArray
    private var bigDotRadius = 13
    var isAnimationRunning = false
    private val tag by lazy{
        this::class.java.simpleName
    }
    private val loadingAnimation by lazy { LoadingAnimation() }
    private var dotPosition: Int = 0
    private var dotCount = DEF_COUNT
    private var timeout = DEF_TIMEOUT
    private val paint by lazy { Paint() }
    // Define colors for each dot
    private  var c1: Int = Color.parseColor("#1875e5")
    private  var c2: Int = Color.parseColor("#499255")
    private  var c3: Int = Color.parseColor("#e8cd02")
    private  var c4: Int = Color.parseColor("#f2960c")
    private  var c5: Int = Color.parseColor("#c5523f")
    private lateinit var listColor: List<Int>

    constructor(context: Context) : super(context) {
        initDotSize()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initDotSize()
        applyAttrs(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initDotSize()
        applyAttrs(context, attrs)
    }
    private fun initDotSize() {
        val scale = resources.displayMetrics.density
        dotDistance = (dotDistance * scale).toInt()
        actualDotRadius = (actualDotRadius * scale).toInt()
        bigDotRadius = (bigDotRadius * scale).toInt()
    }

    private fun applyAttrs(context: Context, attrs: AttributeSet) {
        typedArray  = context.theme.obtainStyledAttributes(
            attrs, R.styleable.ToolDotProgress, 0, 0
        )
        try {
            c1 = typedArray.getColor(R.styleable.ToolDotProgress_color, c1)
            c2 = typedArray.getColor(R.styleable.ToolDotProgress_color, c2)
            c3 = typedArray.getColor(R.styleable.ToolDotProgress_color, c3)
            c4 = typedArray.getColor(R.styleable.ToolDotProgress_color, c4)
            c5 = typedArray.getColor(R.styleable.ToolDotProgress_color, c5)
            listColor = listOf(c1, c2, c3, c4, c5)
            dotCount = typedArray.getInteger(R.styleable.ToolDotProgress_count, dotCount)
            dotCount = dotCount.coerceAtLeast(MIN_COUNT).coerceAtMost(MAX_COUNT)
            timeout = typedArray.getInteger(R.styleable.ToolDotProgress_timeout, timeout)
            timeout = timeout.coerceAtLeast(MIN_TIMEOUT).coerceAtMost(MAX_TIMEOUT)
        }
        finally {
            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isShown) {
            createDots(canvas, paint)
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimation()
    }

    private fun createDots(canvas: Canvas, paint: Paint) {
        var radius: Int
        for (i in listColor.indices) {
            paint.color =listColor[i]
            if (i == dotPosition) {
                radius = bigDotRadius
            }else {
                radius = actualDotRadius
                paint.color = Color.parseColor("#bdbdbd")
            }
            canvas.drawCircle((dotDistance / 2 + i * dotDistance).toFloat(), bigDotRadius.toFloat(), radius.toFloat(), paint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(dotDistance * dotCount, bigDotRadius * 2)
    }

    private fun startAnimation() {
        loadingAnimation.duration = DEF_DURATION
        loadingAnimation.repeatCount = Animation.INFINITE
        loadingAnimation.fillAfter = true
        loadingAnimation.interpolator = DecelerateInterpolator()
        loadingAnimation.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(animation: Animation) {
                Log.d(tag, "onAnimationStart: ")
                isAnimationRunning = true
            }

            override fun onAnimationEnd(animation: Animation) {
                Log.d(tag, "onAnimationEnd: ")
                isAnimationRunning = false

            }

            override fun onAnimationRepeat(animation: Animation) {
                if (++dotPosition >= dotCount) {
                    dotPosition = 0
                }
            }
        })
        startAnimation(loadingAnimation)
    }

    private inner class LoadingAnimation : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            super.applyTransformation(interpolatedTime, t)
            invalidate()
        }
    }

    fun startAnimations(){
        if (!isAnimationRunning) {
            startAnimation()
        }
    }

    companion object {
        private const val DEF_DURATION = 500L
        private const val MIN_COUNT = 1
        private const val DEF_COUNT = 3
        private const val MAX_COUNT = 5
        private const val MIN_TIMEOUT = 10
        private const val DEF_TIMEOUT = 5
        private const val MAX_TIMEOUT = 3000
    }
}