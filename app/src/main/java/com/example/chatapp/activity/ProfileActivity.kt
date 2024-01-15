package com.example.chatapp.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.UUID

class ProfileActivity : AppCompatActivity() {
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var databaseReference: DatabaseReference

    private var filePath: Uri? = null

    private val PICK_IMAGE_REQUEST: Int = 2020

    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private lateinit var userName: EditText
    private lateinit var userImage: ImageView
    private lateinit var imgBack: ImageView
    private lateinit var btnSave: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        userName = findViewById(R.id.etUserName)
        userImage = findViewById(R.id.userImage)
        imgBack = findViewById(R.id.imgBack)
        btnSave = findViewById(R.id.btnSave)
        progressBar = findViewById(R.id.progressBar)

        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        databaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.uid)

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                userName.setText(user!!.userName)

                if (user.userImg == "") {
                    userImage.setImageResource(R.drawable.ic_launcher_background)
                } else {
                    Glide.with(this@ProfileActivity).load(user.userImg).into(userImage)
                }
            }
        })

        imgBack.setOnClickListener {
            onBackPressedDispatcher
        }

        userImage.setOnClickListener {
            chooseImage()
        }

        btnSave.setOnClickListener {
            uploadImage()
            progressBar.visibility = View.VISIBLE
        }

    }
    private fun chooseImage() {
        val intent: Intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode != null) {
            filePath = data!!.data
            try {
                var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                userImage.setImageBitmap(bitmap)
                btnSave.visibility = View.VISIBLE
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    private fun uploadImage() {
        if (filePath != null) {

            var ref: StorageReference = storageRef.child("image/" + UUID.randomUUID().toString())
            ref.putFile(filePath!!)
                .addOnSuccessListener {

                    val hashMap:HashMap<String,String> = HashMap()
                    hashMap["userName"] = userName.text.toString()
                    hashMap["profileImage"] = filePath.toString()
                    databaseReference.updateChildren(hashMap as Map<String, Any>)
                    progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, "Uploaded", Toast.LENGTH_SHORT).show()
                    btnSave.visibility = View.GONE
                }
                .addOnFailureListener {
                    progressBar.visibility = View.GONE
                    Toast.makeText(applicationContext, "Failed" + it.message, Toast.LENGTH_SHORT)
                        .show()

                }

        }
    }

}