package com.example.cargram

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class CreatePostScreen : Fragment() {
    var img : Bitmap?= null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_post, container, false)
        var auth = FirebaseAuth.getInstance()
        val webapi = WebAPI(requireContext())

        val selectimage=registerForActivityResult(ActivityResultContracts.GetContent()){

            if(it != null) {
                img = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
                Picasso.get().load(it).into(view.findViewById<ImageView>(R.id.uploadimg))
            }

        }
        view.findViewById<ImageView>(R.id.uploadimg).setOnClickListener {
            selectimage.launch("image/*")
        }

        view.findViewById<Button>(R.id.uploadBtn).setOnClickListener {
            if(img != null) {
                webapi.uploadImage(img!!,
                    onSuccess = { imgu ->
                        val post = Post(
                            0,
                            auth.currentUser?.uid!!,
                            imgu,
                            view.findViewById<EditText>(R.id.postcaption).text.toString()
                        )
                        webapi.createPost(post,
                            onSuccess = {
                                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                                view.findViewById<ImageView>(R.id.uploadimg).setImageDrawable(null)
                                img = null
                                view.findViewById<EditText>(R.id.postcaption).text.clear()
                            },
                            onError = {

                            })
                    },
                    onError = { error ->
                    }
                )
            }
        }

        return view
    }
}