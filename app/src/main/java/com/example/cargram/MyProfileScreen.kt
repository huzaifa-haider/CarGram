package com.example.cargram

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MyProfileScreen : Fragment() {
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_my_profile_screen, container, false)
        var auth = FirebaseAuth.getInstance()
        val webapi = WebAPI(requireContext())
        val url = requireContext().getString(R.string.url)

        webapi.getUser(auth.currentUser?.uid!!,
            onSuccess = { user ->
                view.findViewById<TextView>(R.id.username).setText(user.name)
                view.findViewById<TextView>(R.id.bio).setText(user.bio)
                Picasso.get().load("$url/images/${user.profilepic}").into(view.findViewById<CircleImageView>(R.id.profilepic))
            },
            onError = { error ->
            })


        view.findViewById<Button>(R.id.editprofilebtn).setOnClickListener{
            startActivity(Intent(context, EditProfileScreen::class.java))
        }

        val userids = listOf(auth.currentUser?.uid) // Your list of userids

        webapi.getPosts(userids) { postslist ->
            if (postslist != null) {
                Toast.makeText(requireContext(), "Lucky", Toast.LENGTH_SHORT).show()
                val postsrv = view.findViewById<RecyclerView>(R.id.profilepostsrv)
                val adapter = ProfilePostsAdapter(requireContext(), postslist)
                postsrv.adapter = adapter
                val layoutManager = GridLayoutManager(requireContext(), 3)
                postsrv.layoutManager = layoutManager

            } else {
                Toast.makeText(requireContext(), "No Luck", Toast.LENGTH_SHORT).show()
            }
        }



        view.findViewById<TextView>(R.id.logoutbtn).setOnClickListener{
            auth.signOut()
            activity?.finish()
        }

        swipeRefreshLayout = view.findViewById(R.id.refresher) as SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
        webapi.getUser(auth.currentUser?.uid!!,
            onSuccess = { user ->
                view.findViewById<TextView>(R.id.username).setText(user.name)
                view.findViewById<TextView>(R.id.bio).setText(user.bio)
                Picasso.get().load("$url/images/${user.profilepic}").into(view.findViewById<CircleImageView>(R.id.profilepic))
            },
            onError = { error ->
            })

        swipeRefreshLayout.isRefreshing = false
    }

        return view
    }
}