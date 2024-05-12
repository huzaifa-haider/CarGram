package com.example.cargram

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class ProfilePostsAdapter(private val context: Context, private val postUrls: List<Post>) :
    RecyclerView.Adapter<ProfilePostsAdapter.PostViewHolder>() {

    private var itemWidth: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.profile_post_card, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val url = context.getString(R.string.url)
        Picasso.get().load("$url/images/${postUrls[position].postimg}").into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return postUrls.size
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        calculateItemWidth(recyclerView)
    }

    private fun calculateItemWidth(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager
        val screenWidth = context.resources.displayMetrics.widthPixels
        val columns = (screenWidth / SQUARE_SIZE_DP).toInt()
        itemWidth = screenWidth / columns
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.profilepostimg)

        init {
            // Set ImageView width and height to be square
            imageView.layoutParams.width = itemWidth
            imageView.layoutParams.height = itemWidth
        }
    }

    companion object {
        private const val SQUARE_SIZE_DP = 240 // Adjust this value as needed for desired image size
    }
}