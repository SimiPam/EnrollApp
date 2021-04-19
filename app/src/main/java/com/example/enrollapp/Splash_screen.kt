package com.example.enrollapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView

//import kotlinx.android.synthetic.main.activity_splash_screen

class Splash_screen : AppCompatActivity() {

    private val SPLASH_TIME: Long = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val topAnimation =  AnimationUtils.loadAnimation(this, R.anim.top_animation)
        val bottomAnimation =  AnimationUtils.loadAnimation(this, R.anim.bottom_animation)

        val topImg: ImageView = findViewById(R.id.img_logo)
        topImg.startAnimation(topAnimation)

        val bottomImg: ImageView = findViewById(R.id.img_logo1)
        bottomImg.startAnimation(bottomAnimation)

        Handler().postDelayed(Runnable { val intent = Intent(Splash_screen@this, MainActivity::class.java)
            startActivity(intent)
            finish() }, SPLASH_TIME)
    }
}