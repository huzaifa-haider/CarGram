package com.example.cargram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GuestHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_home)
        val webapi = WebAPI(this)

        webapi.getallPosts(){posts ->
            val recyclerView: RecyclerView = findViewById(R.id.guestrv)

            val adapter = posts?.let { GuestPostsShowAdapter(this, it.asReversed()) }
            recyclerView.adapter = adapter

            // Optionally, set a layout manager for the RecyclerView
            recyclerView.layoutManager = LinearLayoutManager(this)
        }

    }
}