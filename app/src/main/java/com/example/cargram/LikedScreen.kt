package com.example.cargram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class LikedScreen : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_liked_screen, container, false)
        val webapi = WebAPI(requireContext())


        webapi.getLikedPosts(FirebaseAuth.getInstance().currentUser?.uid!!,
            onSuccess = { postslist ->
                if (postslist != null) {
                    val postsrv = view.findViewById<RecyclerView>(R.id.liked_posts_rv)
                    val adapter = ProfilePostsAdapter(requireContext(), postslist)
                    postsrv.adapter = adapter
                    val layoutManager = GridLayoutManager(requireContext(), 3)
                    postsrv.layoutManager = layoutManager

                } else {
                    Toast.makeText(requireContext(), "No Luck", Toast.LENGTH_SHORT).show()
                }
        },
            onError = {

            }
        )

        return view
    }
}