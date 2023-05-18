package com.example.idlesquares

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import java.math.BigInteger
import java.time.Instant
import kotlin.concurrent.thread
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    var score : BigInteger = BigInteger.ZERO
    var combo = 0
    var clicks = 0
    private lateinit var idleView: IdleView
    var comboTimer = 0
    val comboTimerMax = 35

    private var scoreThread = thread{
        var shouldPause = false
        while(true) {

            if (comboTimer <= 0) {
                    combo = 0
            }
            comboTimer--
            val tScore = ((sqrt(clicks * 1.0f)*(1.0f+kotlin.random.Random.nextFloat()) + combo).toInt()).toBigInteger()
            //Log.d("Score", tScore.toString())
            score = score.add(tScore)
            Thread.sleep(25)
            if (this::idleView.isInitialized) {
                idleView.invalidate()
            }
           
        }
    }

    fun resetScore() {
        val shpr = getSharedPreferences("IdleSquaresPreferences", MODE_PRIVATE)
        val shprE = shpr.edit()
        shprE.putString("score", "0")
        shprE.putInt("clicks", 0)
        shprE.putLong("time", System.currentTimeMillis())
        shprE.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        idleView = findViewById<IdleView>(R.id.idleView)
        idleView.mainActivity = this
        //resetScore()
        val shpr = getSharedPreferences("IdleSquaresPreferences", MODE_PRIVATE)
        score = shpr.getString("score","0")?.let { BigInteger(it) } ?: score
        clicks = shpr.getInt("clicks",0)
        val prevTime = shpr.getLong("time", System.currentTimeMillis())
        score = score.add((((System.currentTimeMillis() - prevTime)*40/1000)*((sqrt(clicks * 1.0) + combo).toInt())).toBigInteger())
    }

    @SuppressLint("CommitPrefEdits")
    override fun onPause() {
        super.onPause()
        val shpr = getSharedPreferences("IdleSquaresPreferences", MODE_PRIVATE)
        val shprE = shpr.edit()
        shprE.putString("score", score.toString())
        shprE.putInt("clicks", clicks)
        shprE.putLong("time", System.currentTimeMillis())
        shprE.apply()
    }

    override fun onResume() {
        super.onResume()
        //val shpr = getSharedPreferences("IdleSquaresPreferences", MODE_PRIVATE)
        //score = shpr.getString("score","0")?.let { BigInteger(it) } ?: score
        //clicks = shpr.getInt("clicks",0)
    }
    @SuppressLint("CommitPrefEdits")
    override fun onStop() {
        super.onStop()
        val shpr = getSharedPreferences("IdleSquaresPreferences", MODE_PRIVATE)
        val shprE = shpr.edit()
        shprE.putString("score", score.toString())
        shprE.putInt("clicks", clicks)
        shprE.putLong("time", System.currentTimeMillis())
        shprE.apply()
    }

}