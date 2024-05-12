package com.example.cargram

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val auth = FirebaseAuth.getInstance()
        val webapi = WebAPI(requireContext())

        webapi.getFollowedUsers(auth.currentUser?.uid!!,
            onSuccess = {users->
                webapi.getPosts(users){posts ->
                    val recyclerView: RecyclerView = view.findViewById(R.id.home_posts_rv)

                    val adapter = posts?.let { HomePostAdapter(requireContext(), it.asReversed(), childFragmentManager) }
                    recyclerView.adapter = adapter

                    // Optionally, set a layout manager for the RecyclerView
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                }
            },
            onError = {

            }
            )

        return view
    }
}