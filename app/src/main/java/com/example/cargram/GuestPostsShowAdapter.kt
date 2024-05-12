package com.example.cargram

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class GuestPostsShowAdapter(private val context: Context, private val posts: List<Post>) :
    RecyclerView.Adapter<GuestPostsShowAdapter.PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_post_card, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val url = context.getString(R.string.url)
        val webapi = WebAPI(context)
        Picasso.get().load("$url/images/${posts[position].postimg}").into(holder.img)
        holder.caption.setText(posts[position].caption)

        webapi.getUser(posts[position].userid,
            onSuccess = {user ->
                holder.profusername.setText(user.username)
                Picasso.get().load("$url/images/${user.profilepic}").into(holder.profimg)
            },
            onError = {

            }
        )

        holder.likebtn.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.postimg)
        val profimg: CircleImageView = itemView.findViewById(R.id.profileimg)
        val profusername: TextView = itemView.findViewById(R.id.username)
        val caption: TextView = itemView.findViewById(R.id.caption)
        val likebtn: ImageView = itemView.findViewById(R.id.likebtn)
    }
}
