package com.example.cargram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchScreen : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_search_screen, container, false)
        val webapi = WebAPI(requireContext())

        view.findViewById<ImageView>(R.id.searchicon).setOnClickListener {
            webapi.searchUsers(view.findViewById<EditText>(R.id.searchText).text.toString()) { userslist ->
                if (userslist != null) {
                    val searchrv = view.findViewById<RecyclerView>(R.id.search_results_rv)
                    val adapter = UserSearchAdapter(requireContext(), userslist)
                    searchrv.adapter = adapter
                    val layoutManager = LinearLayoutManager(requireContext())
                    searchrv.layoutManager = layoutManager

                } else {
                    Toast.makeText(requireContext(), "No Luck", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }
}