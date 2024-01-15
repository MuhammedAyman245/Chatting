package com.example.chatapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    private var firebaseUser:FirebaseUser? = null
    private var ref:DatabaseReference? = null
    private lateinit var imgProfile: ImageView
    private lateinit var userName: TextView
    private lateinit var imgBack: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        imgProfile = findViewById(R.id.imgProfile)
        userName = findViewById(R.id.tvUserName)
        imgBack = findViewById(R.id.imgBack)

        var intent = getIntent()
        var userId = intent.getStringExtra("userId")

        imgBack.setOnClickListener {
            onBackPressed()
        }


        firebaseUser = FirebaseAuth.getInstance().currentUser
        ref = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)

        ref!!.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUser = snapshot.getValue(User::class.java)
                userName.text = currentUser!!.userName
                if(currentUser.userImg.isEmpty()){
                    imgProfile.setImageResource(R.drawable.ic_launcher_background)
                } else{
                    Glide.with(this@ChatActivity).load(currentUser.userImg).into(imgProfile)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}