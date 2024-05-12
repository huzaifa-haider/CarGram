package com.example.cargram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var auth = FirebaseAuth.getInstance()
        val webapi = WebAPI(this)

        findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.loginbtn).setOnClickListener{
            val email = findViewById<EditText>(R.id.emailfield)
            val password = findViewById<EditText>(R.id.passwordfield)

            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener{
                if (it.isSuccessful){

                    webapi.getUser(auth.currentUser?.uid!!,
                        onSuccess = {user->
                            if(user.type == "user"){
                                startActivity(Intent(this, HomeScreen::class.java))
                                finish()
                            }
                            else{
                                startActivity(Intent(this, AdminDashboardScreen::class.java))
                                finish()
                            }
                        },
                        onError = {

                        })


                }
            }
        }

        findViewById<Button>(R.id.loginguestbtn).setOnClickListener {
            startActivity(Intent(this, GuestHome::class.java))
        }


        findViewById<TextView>(R.id.signuptxt).setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
        }
    }
}