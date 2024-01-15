package com.example.chatapp.activity
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.R
import com.example.chatapp.R.id.ConfirmPassEt
import com.example.chatapp.R.id.EmailEt
import com.example.chatapp.R.id.NameEt
import com.example.chatapp.R.id.PasswordEt
import com.example.chatapp.R.id.PhoneEt
import com.example.chatapp.R.id.SignUpBtn
import com.example.chatapp.R.id.btnSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth:FirebaseAuth
    private lateinit var databaseRef:DatabaseReference
    private lateinit var registerBtn:Button
    private lateinit var logInBtn:Button
    private lateinit var userName:EditText
    private lateinit var userEmail:EditText
    private lateinit var userPass:EditText
    private lateinit var userConfirmPass:EditText
    private lateinit var userNumber:EditText


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        logInBtn = findViewById(btnSignIn)
        registerBtn = findViewById(SignUpBtn)
        userName = findViewById(NameEt)
        userEmail = findViewById(EmailEt)
        userPass = findViewById(PasswordEt)
        userConfirmPass = findViewById(ConfirmPassEt)
        userNumber = findViewById(PhoneEt)

        registerBtn.setOnClickListener{
            if(userPass.text.toString()!=userConfirmPass.text.toString()){
                Toast.makeText(applicationContext, "Password doesn't match", Toast.LENGTH_SHORT).show()
            }else {
                if (userName.text.isNotEmpty() && userEmail.text.isNotEmpty() && userPass.text.isNotEmpty()) {
                    register(
                        userName.text.toString().trim(),
                        userEmail.text.toString().trim(),
                        userPass.text.toString().trim()
                    )
                }
            }
        }
        logInBtn.setOnClickListener{
            logIn()
        }
    }

    private fun register(userName:String,email:String, pass:String){
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener{
            if(it.isSuccessful){
                var user: FirebaseUser? = auth.currentUser
                var userId:String = user!!.uid

                databaseRef=FirebaseDatabase.getInstance().getReference("Users").child(userId)

                var hashMap: HashMap<String,String> = HashMap()

                hashMap["userId"] = userId
                hashMap["userName"] = userName
                hashMap["userEmail"] = userEmail.text.toString()
                hashMap["userNumber"] = userNumber.text.toString()
                hashMap["profileImg"] = ""

                databaseRef.setValue(hashMap).addOnCompleteListener(this){ it ->
                    if(it.isSuccessful){

                        val intent = Intent(this@SignUpActivity, UserActivity::class.java)

                        startActivity(intent)
                    }
                    else{
                        Toast.makeText(applicationContext, it.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else{
                Toast.makeText(applicationContext, it.exception?.message.toString(),Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun logIn(){
        val intent = Intent(this@SignUpActivity, SignInActivity::class.java)
        startActivity(intent)
    }
}