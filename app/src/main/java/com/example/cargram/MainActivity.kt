package com.example.cargram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val webapi = WebAPI(this)

        if (FirebaseAuth.getInstance().currentUser != null){
            webapi.getUser(FirebaseAuth.getInstance().currentUser?.uid!!,
                onSuccess = {user->
                    if (user.type == "user"){
                        startActivity(Intent(this, HomeScreen::class.java))
                        finish()
                    }
                    else{
                        startActivity(Intent(this, AdminDashboardScreen::class.java))
                        finish()
                    }
                },
                onError = {

                }
            )

        }
        else {
            Handler().postDelayed({
                startActivity(Intent(this, Login::class.java))
                finish()
            }, 1000)
        }
    }

    override fun onStart() {
        super.onStart()

    }
}