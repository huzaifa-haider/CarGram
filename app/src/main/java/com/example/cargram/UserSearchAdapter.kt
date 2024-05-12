package com.example.cargram

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class UserSearchAdapter(private val context: Context, private val userList: List<User>) :
    RecyclerView.Adapter<UserSearchAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.username)
        val bio: TextView = itemView.findViewById(R.id.bio)
        val profileimg: CircleImageView = itemView.findViewById(R.id.profilepic)
        val card: CardView = itemView.findViewById(R.id.searchcard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.search_profile_card, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val url = context.getString(R.string.url)
        val currentUser = userList[position]
        holder.username.text = currentUser.username
        holder.bio.text = currentUser.bio
        Picasso.get().load("${url}/images/${currentUser.profilepic}").into(holder.profileimg)

        holder.card.setOnClickListener{
            val intent = Intent(context, UserProfileScreen::class.java)
            intent.putExtra("userid", userList[position].userid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}