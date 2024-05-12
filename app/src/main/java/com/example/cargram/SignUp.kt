package com.example.cargram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        var auth = FirebaseAuth.getInstance()
        var webapi = WebAPI(this)
        val name = findViewById<EditText>(R.id.namefield)
        val email = findViewById<EditText>(R.id.emailfield)
        val username = findViewById<EditText>(R.id.usernamefield)
        val password = findViewById<EditText>(R.id.passwordfield)
        val phone = findViewById<EditText>(R.id.phonefield)
        val country = findViewById<EditText>(R.id.countryfield)

        findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.signupbtn).setOnClickListener{

            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener(this){
                if (it.isSuccessful){
                    FirebaseMessaging.getInstance().token.addOnCompleteListener{task->
                        if(task.isSuccessful){
                            val token = task.result
                            val user = User(auth.currentUser?.uid!!, username.text.toString(), name.text.toString(),email.text.toString(), password.text.toString(), phone.text.toString(), country.text.toString(), "", "", token, "user")
                            webapi.createUser(user)

                            //SignIn User
                            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener(){signinTask->
                                if (signinTask.isSuccessful){
                                    val intent = Intent(this, HomeScreen::class.java)
                                    intent.putExtra("username", user.username)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                    }

                }
            }
        }


        findViewById<TextView>(R.id.loginpagebtn).setOnClickListener{
            finish()
        }
    }
}