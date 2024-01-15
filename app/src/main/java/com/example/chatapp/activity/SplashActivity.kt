package com.example.chatapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth



class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        auth = FirebaseAuth.getInstance()
        var intentHome = Intent(this@SplashActivity, UserActivity::class.java)
        var intentLogIn = Intent(this@SplashActivity, SignInActivity::class.java)
        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            if(auth.currentUser != null){
               startActivity(intentHome)
            }else{
                startActivity(intentLogIn)
            }
        },2000)    }
}