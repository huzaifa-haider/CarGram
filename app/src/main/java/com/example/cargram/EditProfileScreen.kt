package com.example.cargram

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream

class EditProfileScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_screen)
        var auth = FirebaseAuth.getInstance()
        val webapi = WebAPI(this)
        var url = this.getString(R.string.url)

        val nameedit = findViewById<EditText>(R.id.nameInput)
        val usernameedit = findViewById<EditText>(R.id.usernameInput)
        val bioedit = findViewById<EditText>(R.id.bioInput)
        val phoneedit = findViewById<EditText>(R.id.phoneInput)
        val countryedit = findViewById<EditText>(R.id.countryInput)

        var selectimage=registerForActivityResult(ActivityResultContracts.GetContent()){

            if(it != null) {
                val img: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
                webapi.uploadImage(img,
                    onSuccess = { img ->
                        Picasso.get().load(it).into(findViewById<CircleImageView>(R.id.editProfile))
                        webapi.updateUserImg(img, auth.currentUser?.uid!!)
                    },
                    onError = { error ->
                    }
                )
            }

        }
        findViewById<CircleImageView>(R.id.editProfile).setOnClickListener {
            selectimage.launch("image/*")
        }

        webapi.getUser(
            auth.currentUser?.uid!!,
            onSuccess = { user ->
                nameedit.setText(user.name)
                usernameedit.setText(user.username)
                bioedit.setText(user.bio)
                phoneedit.setText(user.phone)
                countryedit.setText(user.country)
                if(user.profilepic != "") {
                    Picasso.get().load("$url/images/${user.profilepic}")
                        .into(findViewById<CircleImageView>(R.id.editProfile))
                }
            },
            onError = { error ->
            }
        )

        findViewById<Button>(R.id.updateBtn).setOnClickListener{
            webapi.updateUser(nameedit.text.toString(), usernameedit.text.toString(), bioedit.text.toString(), phoneedit.text.toString(), countryedit.text.toString(), auth.currentUser?.uid!!)
        }

        findViewById<ImageView>(R.id.backBtn).setOnClickListener{
            finish()
        }
    }

    fun bitmapToBase64(dp: Bitmap):String{
        var stream= ByteArrayOutputStream()
        dp.compress(Bitmap.CompressFormat.JPEG,100,stream)
        stream.toByteArray()
        return Base64.encodeToString(stream.toByteArray(),Base64.DEFAULT).toString()
    }
}