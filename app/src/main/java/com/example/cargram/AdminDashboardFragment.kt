package com.example.cargram

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class AdminDashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.admin_home_fragment, container, false)
        val auth = FirebaseAuth.getInstance()
        val webapi = WebAPI(requireContext())

        webapi.getUser(auth.currentUser?.uid!!,
            onSuccess = {
                view.findViewById<TextView>(R.id.adminname).setText(it.username)
            },
            onError = {

            })

        webapi.getStats(onSuccess = {
            view.findViewById<TextView>(R.id.totalusers).setText(it[0].toString())
            view.findViewById<TextView>(R.id.totallikes).setText(it[1].toString())
        },
            onError = {

            })

        return view
    }

}