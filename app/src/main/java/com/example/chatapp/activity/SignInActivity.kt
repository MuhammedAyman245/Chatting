package com.example.chatapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth


class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var email:EditText
    private lateinit var pass: EditText
    private lateinit var signUpBtn:Button
    private lateinit var signInBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        auth = FirebaseAuth.getInstance()
        email = findViewById(R.id.EmailEt)
        pass = findViewById(R.id.PasswordEt)
        signInBtn = findViewById(R.id.btnSignIn)
        signUpBtn = findViewById(R.id.SignUpBtn)

        if(auth.currentUser!=null) {
            val intent = Intent(this@SignInActivity, ProfileActivity::class.java)
            startActivity(intent)
        } else{
            signInBtn.setOnClickListener {
                if (email.text.toString().isNotEmpty() && pass.text.toString().isNotEmpty()) {
                    signIn()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "You have to enter you email and password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            signUpBtn.setOnClickListener {
                signUp()
            }
        }
    }


    private fun signIn(){
        val intent = Intent(this@SignInActivity, UserActivity::class.java)
        auth.signInWithEmailAndPassword(email.text.toString().trim(),pass.text.toString().trim()).addOnCompleteListener{
            if(it.isSuccessful){
                email.setText("")
                pass.setText("")
                Toast.makeText(applicationContext, "LogIn Successfully", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            }
            else{
                Toast.makeText(applicationContext, it.exception?.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signUp(){
        val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
        startActivity(intent)
    }
}

