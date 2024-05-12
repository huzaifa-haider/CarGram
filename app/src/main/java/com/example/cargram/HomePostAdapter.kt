package com.example.cargram

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class HomePostAdapter(private val context: Context, private val posts: List<Post>, fragmentManager: FragmentManager) :
    RecyclerView.Adapter<HomePostAdapter.PostViewHolder>() {

    val fragmentManager = fragmentManager
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


        holder.commentbtn.setOnClickListener{
            val bottomSheetFragment = CommentsDialog()
            bottomSheetFragment.arguments = Bundle().apply {
                putString("postid", posts[position].postid.toString())
                putString("commenter", FirebaseAuth.getInstance().currentUser?.uid)
            }
            bottomSheetFragment.show(fragmentManager, bottomSheetFragment.tag)
        }


        webapi.checkLike(posts[position].postid, FirebaseAuth.getInstance().currentUser?.uid!!,
            onSuccess = {
                var liked = false
                if(it){
                    liked = true
                    holder.likebtn.setImageResource(R.drawable.notif_btn)
                }
                holder.likebtn.setOnClickListener{
                    if (liked){
                        webapi.removeLike(posts[position].postid, FirebaseAuth.getInstance().currentUser?.uid!!,
                            onSuccess = {
                                holder.likebtn.setImageResource(R.drawable.unliked_btn)
                                liked = false
                            },
                            onError = {

                            }
                            )
                    }
                    else if (!liked){
                        webapi.addLike(posts[position].postid, FirebaseAuth.getInstance().currentUser?.uid!!,
                            onSuccess = {
                                holder.likebtn.setImageResource(R.drawable.notif_btn)
                                liked = true
                                webapi.getUser(posts[position].userid,
                                    onSuccess = {user->
                                        webapi.getUser(FirebaseAuth.getInstance().currentUser?.uid!!,
                                            onSuccess = {
                                                val notifaction = FCMMessaging(context)
                                                notifaction.sendlikeNotification(user, it)
                                            },
                                            onError = {

                                            })
                                    },
                                    onError = {

                                    }
                                )
                            },
                            onError = {

                            }
                        )
                    }
                }

            },
            onError = {

            }
        )
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
        val commentbtn: ImageView = itemView.findViewById(R.id.commentbutton)
    }
}
