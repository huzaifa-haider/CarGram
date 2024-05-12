package com.example.cargram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        val fragmentManager = supportFragmentManager

        val homeFragment = HomeFragment()
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, homeFragment)
            .commit()

        findViewById<BottomNavigationView>(R.id.navBar).setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    val homeFragment = HomeFragment()
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, homeFragment).commit()
                    true
                }

                R.id.navigation_search -> {
                    val searchFragment = SearchScreen()
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, searchFragment).commit()
                    true
                }

                R.id.navigation_post -> {
                    val addFragment = CreatePostScreen()
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, addFragment)
                        .commit()
                    true
                }

                R.id.navigation_likes -> {
                    val likesFragment = LikedScreen()
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, likesFragment).commit()
                    true
                }

                R.id.navigation_profile -> {
                    val profileFragment = MyProfileScreen()
                    fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, profileFragment).commit()
                    true
                }

                else -> false
            }
        }
    }
}