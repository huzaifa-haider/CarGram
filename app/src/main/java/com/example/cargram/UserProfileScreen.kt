package com.example.cargram

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserProfileScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile_screen)
        val webapi = WebAPI(this)
        val auth = FirebaseAuth.getInstance()
        val url = this.getString(R.string.url)

        findViewById<ImageView>(R.id.b).setOnClickListener{
            finish()
        }

        webapi.getUser(auth.currentUser?.uid!!,
            onSuccess = {
                if (it.type == "admin"){
                    findViewById<Button>(R.id.followbtn).visibility = View.GONE
                }
            },
            onError = {

            })

        webapi.getUser(intent.getStringExtra("userid")!!,
            onSuccess = { user ->
                findViewById<TextView>(R.id.usernametop).setText(user.username)
                findViewById<TextView>(R.id.username).setText(user.name)
                findViewById<TextView>(R.id.bio).setText(user.bio)
                Picasso.get().load("$url/images/${user.profilepic}").into(findViewById<CircleImageView>(R.id.profilepic))
            },
            onError = { error ->
            })

        webapi.checkFollow(intent.getStringExtra("userid")!!, FirebaseAuth.getInstance().currentUser?.uid!!,
            onSuccess = { follows ->
                var follow = false
                if (follows) {
                    findViewById<Button>(R.id.followbtn).setText("Unfollow")
                    follow = true
                }

                findViewById<Button>(R.id.followbtn).setOnClickListener{
                    if (!follow){
                        webapi.addFollow(intent.getStringExtra("userid")!!, FirebaseAuth.getInstance().currentUser?.uid!!,
                            onSuccess = {
                                follow = true
                                findViewById<Button>(R.id.followbtn).setText("Unfollow")
                            },
                            onError = {

                            }
                            )
                    }
                    else if (follow){
                        webapi.removeFollow(intent.getStringExtra("userid")!!, FirebaseAuth.getInstance().currentUser?.uid!!,
                            onSuccess = {
                                follow = false
                                findViewById<Button>(R.id.followbtn).setText("Follow")
                            },
                            onError = {

                            }
                        )
                    }
                }
            },
            onError = { error ->
                // Handle error
            }
        )

        val userids = listOf(intent.getStringExtra("userid")) // Your list of userids

        webapi.getPosts(userids) { postslist ->
            if (postslist != null) {
                val postsrv = findViewById<RecyclerView>(R.id.userpostsrv)
                val adapter = ProfilePostsAdapter(this, postslist.asReversed())
                postsrv.adapter = adapter
                val layoutManager = GridLayoutManager(this, 3)
                postsrv.layoutManager = layoutManager

            } else {
                Toast.makeText(this, "No Luck", Toast.LENGTH_SHORT).show()
            }
        }
    }
}