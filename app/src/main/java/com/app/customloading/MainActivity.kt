package com.app.customloading

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart.setOnClickListener {
            if (dots_progress.isAnimationRunning){
                Toast.makeText(this, "Animation Already Running", Toast.LENGTH_LONG).show()
            }else{
                startAnimation()
            }
        }

        btnStop.setOnClickListener {
            if (!dots_progress.isAnimationRunning){
                Toast.makeText(this, "Animation Already Stopped", Toast.LENGTH_LONG).show()
            }else{
                stopAnimation()
            }
        }
    }

    private fun stopAnimation(){
        dots_progress.clearAnimation()
    }

    private fun startAnimation(){
        dots_progress.startAnimations()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAnimation()
    }

    override fun onPause() {
        super.onPause()
        stopAnimation()
    }

    override fun onResume() {
        super.onResume()
        startAnimation()
    }
}
