package com.example.chatapp.activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.adapter.UserAdapter
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var imgBack: ImageView
    private lateinit var imgProfile: ImageView
    private var userList = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        recyclerView = findViewById(R.id.userRecyclerView)
        imgBack = findViewById(R.id.imgBack)
        imgProfile = findViewById(R.id.imgProfile)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        imgBack.setOnClickListener {
            onBackPressed()
        }

        imgProfile.setOnClickListener {
            val intent = Intent(
                this@UserActivity,
                ProfileActivity::class.java
            )
            startActivity(intent)
        }

        getUserList()
    }

    private fun getUserList(){
        var firebase: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
        var databaseRef:DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

        databaseRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                val currentUser = snapshot.getValue(User::class.java)
                if(currentUser!!.userImg.isEmpty()){
                    imgProfile.setImageResource(R.drawable.ic_launcher_background)
                } else{
                    Glide.with(this@UserActivity).load(currentUser.userImg).into(imgProfile)
                }

                for (dataSnapshot:DataSnapshot in snapshot.children){
                    val user = dataSnapshot.getValue(User::class.java)

                    if(user!!.userId.equals(firebase.uid)){
                        userList.add(user)
                    }

                }
                val userAdapter = UserAdapter(this@UserActivity,userList)
                recyclerView.adapter = userAdapter
            }

            override fun onCancelled(error: DatabaseError) {

                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }


        })
    }
}