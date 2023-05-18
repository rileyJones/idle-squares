package com.example.idlesquares

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import java.lang.Thread.sleep
import java.math.BigInteger
import kotlin.concurrent.thread

class IdleView(context: Context?, attrs: AttributeSet?) : View(context, attrs), GestureDetector.OnGestureListener {
    private var mDetector = GestureDetectorCompat(this.context, this)

    private var mWidth : Float = 0.0f
    private var mHeight : Float = 0.0F

    lateinit var mainActivity: MainActivity

    private var lPos : Float = kotlin.random.Random.nextFloat()*(1.0f - 0.15f)
    private var uPos : Float = kotlin.random.Random.nextFloat()*(1.0f - 0.15f)
    private var rPos : Float = lPos+0.15f+(kotlin.random.Random.nextFloat()*kotlin.math.min(0.4f,(1.0f - 0.15f-lPos)))
    private var dPos : Float = uPos+0.15f+(kotlin.random.Random.nextFloat()*kotlin.math.min(0.4f,(1.0f - 0.15f-uPos)))

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(mDetector.onTouchEvent(event)) {
            return true
        }
        return super.onTouchEvent(event)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // caching these so they don't have to be recomputed
        mWidth = w.toFloat()
        mHeight = h.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint()

        // nw
        //paint.color = 0x11d7ed

        paint.color = Color.parseColor("#11d7ed")
        canvas.drawRect(0.0f, 0.0f, mWidth, mHeight, paint)

        paint.color = Color.parseColor("#ed9b0e")
        canvas.drawRect(lPos*mWidth, uPos*mHeight, rPos*mWidth, dPos*mHeight, paint)

        paint.color = Color.BLACK
        paint.textAlign = Paint.Align.CENTER
        paint.textSize =  kotlin.math.min(mWidth,mHeight)*0.1f
        if(this::mainActivity.isInitialized) {
            canvas.drawText("Score: ${mainActivity.score.toString()}", mWidth*0.5f, paint.textSize, paint)
            canvas.drawText("Clicks: ${mainActivity.clicks}", mWidth*0.5f, paint.textSize*2.2f, paint)
            canvas.drawText("Combo: ${mainActivity.combo}", mWidth*0.5f, paint.textSize*3.4f, paint)
        }
    }


    override fun onDown(p0: MotionEvent?): Boolean {
        return true
    }

    override fun onShowPress(p0: MotionEvent?) {
    }

    override fun onSingleTapUp(p0: MotionEvent?): Boolean {
        //Log.d("Position", "There")
        if(p0 != null) {

            if(p0.x/mWidth in (lPos-0.03f)..(rPos+0.03f) && p0.y/mHeight in (uPos-0.03f)..(dPos+0.03f)) {
                lPos = kotlin.random.Random.nextFloat()*(1.0f - 0.15f)
                uPos = kotlin.random.Random.nextFloat()*(1.0f - 0.15f)
                rPos = lPos+0.15f+(kotlin.random.Random.nextFloat()*kotlin.math.min(0.4f,(1.0f - 0.15f-lPos)))
                dPos = uPos+0.15f+(kotlin.random.Random.nextFloat()*kotlin.math.min(0.4f,(1.0f - 0.15f-uPos)))
                if (this::mainActivity.isInitialized) {
                    mainActivity.clicks++
                    mainActivity.combo++
                    mainActivity.comboTimer = mainActivity.comboTimerMax
                }
                invalidate()
            }

        }
        return true
    }

    override fun onScroll(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        //return onSingleTapUp(p0)
        return false
    }

    override fun onLongPress(p0: MotionEvent?) {
        onSingleTapUp(p0)
    }

    override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
        return onSingleTapUp(p0)
    }

}
