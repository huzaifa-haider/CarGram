package com.example.cargram

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.google.firebase.auth.FirebaseAuth
import org.json.JSONArray
import java.io.ByteArrayOutputStream


class WebAPI(private val context: Context) {
    val queue = Volley.newRequestQueue(context)
    val url : String = context.getString(R.string.url)

    fun createUser(user: User) {
        val url = "$url/users"
        val jsonBody = JSONObject().apply {
            put("userid", user.userid)
            put("username", user.username)
            put("name", user.name)
            put("email", user.email)
            put("password", user.password)
            put("phone", user.phone)
            put("country", user.country)
            put("profilepic", user.profilepic)
            put("bio", user.bio)
            put("fcmtoken", user.fcmtoken)
            put("type", user.type)
        }

        val request = JsonObjectRequest(
            Request.Method.POST,
            url,
            jsonBody,
            { response ->
                Toast.makeText(context, user.name + " account created", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }

    fun updateUser(name: String, username: String, bio: String, phone: String, country: String, userId: String) {
        val url = "$url/users/$userId"
        val jsonBody = JSONObject().apply {
            put("name", name)
            put("username", username)
            put("bio", bio)
            put("phone", phone)
            put("country", country)
        }

        val request = object : JsonObjectRequest(
            Method.PUT,
            url,
            jsonBody,
            { response ->

            },
            { error ->

            }
        )
        {}

        queue.add(request)
    }

    fun updateUserImg(imgname: String, userId: String) {
        val url = "$url/users/$userId"
        val jsonBody = JSONObject().apply {
            put("profilepic", imgname)
        }

        val request = object : JsonObjectRequest(
            Method.PUT,
            url,
            jsonBody,
            { response ->

            },
            { error ->

            }
        )
        {}

        queue.add(request)
    }
    fun getUser(userId: String, onSuccess: (User) -> Unit, onError: (String) -> Unit) {
        val url = "$url/users/$userId"

        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->

                // Unpack JSON response and construct User object
                val userid = response.getString("userid")
                val username = response.getString("username")
                val name = response.getString("name")
                val email = response.getString("email")
                val password = response.getString("password")
                val phone = response.getString("phone")
                val country = response.getString("country")
                val bio = response.getString("bio")
                val profilepic = response.getString("profilepic")
                val fcmtoken = response.optString("fcmtoken", "")
                val type = response.getString("type")

                val user = User(userid, username, name, email, password, phone, country, profilepic, bio, fcmtoken, type)
                onSuccess(user)

            },
            { error ->
                // Handle error
                onError(error.message ?: "Unknown error occurred")
            }
        )

        queue.add(request)
    }

    fun uploadImage(bitmap: Bitmap, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT)

        val jsonObject = JSONObject().apply {
            put("image", encodedImage)
        }

        val request = JsonObjectRequest(
            Request.Method.POST,
            "$url/upload",
            jsonObject,
            { response ->
                val imagePath = response.optString("image_file")
                onSuccess(imagePath)
            },
            { error ->
                onError(error.message ?: "Unknown error occurred")
            }
        )

        queue.add(request)
    }

    fun createPost(post : Post, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val jsonObject = JSONObject().apply {
            put("userid", post.userid)
            put("postimg", post.postimg)
            put("caption", post.caption)
        }

        val request = JsonObjectRequest(
            Request.Method.POST,
            "$url/posts",
            jsonObject,
            { response ->
                val msg = response.optString("message")
                onSuccess(msg)
            },
            { error ->
                onError(error.message ?: "Unknown error occurred")
            }
        )

        queue.add(request)
    }

    fun getPosts(userids: List<String?>, callback: (List<Post>?) -> Unit){
        val jsonArray = JSONArray(userids)
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.POST,
            "$url/posts/users",
            jsonArray,
            { response ->
                val posts = mutableListOf<Post>()
                for (i in 0 until response.length()) {
                    val postObject = response.getJSONObject(i)
                    val post = Post(
                        postObject.getInt("postid"),
                        postObject.getString("userid"),
                        postObject.optString("postimg", null),
                        postObject.optString("caption", null)
                    )
                    posts.add(post)
                }
                callback(posts)
            },
            { error ->
            }
        )

        queue.add(jsonArrayRequest)
    }

    fun getallPosts(callback: (List<Post>?) -> Unit){
        // Request a JSON response from the provided URL.
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, "$url/posts", null,
            { response ->
                val postlist = mutableListOf<Post>()
                // Process the JSON response
                for (i in 0 until response.length()) {
                    val post: JSONObject = response.getJSONObject(i)
                    val post_holder = Post(post.getInt("postid"), post.getString("userid"), post.getString("postimg"), post.optString("caption"))

                    postlist.add(post_holder)
                }

                callback(postlist)
            },
            { error ->
                // Handle errors
                Log.e("VolleyError", "Error: ${error.message}")
            }
        )

        // Add the request to the RequestQueue.
        queue.add(jsonArrayRequest)
    }

    fun searchUsers(searchQuery: String, callback: (List<User>?) -> Unit) {
        val jsonObject = JSONObject().apply {
            put("search", searchQuery)
        }

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,
            "$url/search",
            jsonObject,
            { response ->
                val users = mutableListOf<User>()

                val usersArray = response.getJSONArray("users")
                for (i in 0 until usersArray.length()) {
                    val userObject = usersArray.getJSONObject(i)
                    val user = User(
                        userObject.getString("userid"),
                        userObject.getString("username"),
                        userObject.getString("name"),
                        userObject.getString("email"),
                        userObject.getString("password"),
                        userObject.getString("phone"),
                        userObject.getString("country"),
                        userObject.getString("profilepic"),
                        userObject.getString("bio"),
                        userObject.getString("fcmtoken"),
                        userObject.getString("type")
                    )
                    if (user.userid != FirebaseAuth.getInstance().currentUser?.uid) {

                    }
                    users.add(user)
                }
                callback(users)
            },
            { error ->

            }
        )

        queue.add(jsonObjectRequest)
    }


    fun checkFollow(followedUser: String, follower: String, onSuccess: (Boolean) -> Unit, onError: (String) -> Unit) {

        val jsonObject = JSONObject().apply {
            put("followed_user", followedUser)
            put("follower", follower)
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, "$url/check_follow", jsonObject,
            { response ->
                onSuccess(response.getBoolean("follows"))
            },
            { error ->
                onError(error.message ?: "An error occurred")
            }
        )

        queue.add(jsonObjectRequest)
    }

    fun addFollow(followedUser: String, follower: String, onSuccess: (Boolean) -> Unit, onError: (String) -> Unit) {

        val jsonObject = JSONObject().apply {
            put("followed_user", followedUser)
            put("follower", follower)
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, "$url/add_follow", jsonObject,
            { response ->
                onSuccess(true)
            },
            { error ->
                onError(error.message ?: "An error occurred")
            }
        )

        queue.add(jsonObjectRequest)
    }

    fun removeFollow(followedUser: String, follower: String, onSuccess: (Boolean) -> Unit, onError: (String) -> Unit) {

        val jsonObject = JSONObject().apply {
            put("followed_user", followedUser)
            put("follower", follower)
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, "$url/remove_follow", jsonObject,
            { response ->
                onSuccess(true)
            },
            { error ->
                onError(error.message ?: "An error occurred")
            }
        )

        queue.add(jsonObjectRequest)
    }

    fun getFollowedUsers(follower: String, onSuccess: (List<String?>) -> Unit, onError: (String) -> Unit) {
        val jsonObject = JSONObject().apply {
            put("follower", follower)
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, "$url/get_followed_users", jsonObject,
            { response ->
                val followedUsersJsonArray = response.getJSONArray("followed_users")
                val followedUsersList = mutableListOf<String>()
                for (i in 0 until followedUsersJsonArray.length()) {
                    val userJson = followedUsersJsonArray.getJSONObject(i)
                    val user = User(
                        userJson.getString("userid"),
                        userJson.getString("username"),
                        userJson.getString("name"),
                        userJson.getString("email"),
                        userJson.getString("password"),
                        userJson.getString("phone"),
                        userJson.getString("country"),
                        userJson.getString("bio"),
                        userJson.getString("profilepic"),
                        userJson.optString("fcmtoken", ""),
                        userJson.getString("type_")
                    )

                    followedUsersList.add(user.userid)
                }
                onSuccess(followedUsersList)
            },
            { error ->
            }
        )

        queue.add(jsonObjectRequest)
    }

    fun checkLike(postid: Int, likedby: String, onSuccess: (Boolean) -> Unit, onError: (String) -> Unit) {

        val jsonObject = JSONObject().apply {
            put("postid", postid)
            put("likedby", likedby)
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, "$url/check_like", jsonObject,
            { response ->
                onSuccess(response.getBoolean("exists"))
            },
            { error ->
                onError(error.message ?: "An error occurred")
            }
        )

        queue.add(jsonObjectRequest)
    }

    fun addLike(postid: Int, likedby: String, onSuccess: (Boolean) -> Unit, onError: (String) -> Unit) {

        val jsonObject = JSONObject().apply {
            put("postid", postid)
            put("likedby", likedby)
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, "$url/insert_like", jsonObject,
            { response ->
                onSuccess(true)
            },
            { error ->
                onError(error.message ?: "An error occurred")
            }
        )

        queue.add(jsonObjectRequest)
    }

    fun removeLike(postid: Int, likedby: String, onSuccess: (Boolean) -> Unit, onError: (String) -> Unit) {

        val jsonObject = JSONObject().apply {
            put("postid", postid)
            put("likedby", likedby)
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, "$url/delete_like", jsonObject,
            { response ->
                onSuccess(true)
            },
            { error ->
                onError(error.message ?: "An error occurred")
            }
        )

        queue.add(jsonObjectRequest)
    }

    fun getLikedPosts(likedby: String, onSuccess: (List<Post>) -> Unit, onError: (String) -> Unit){
        val jsonObject = JSONObject().apply {
            put("userid", likedby)
        }

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, "$url/liked_posts", jsonObject,
            { response ->
                val followedUsersJsonArray = response.getJSONArray("liked_posts")
                val followedUsersList = mutableListOf<Post>()
                for (i in 0 until followedUsersJsonArray.length()) {
                    val userJson = followedUsersJsonArray.getJSONObject(i)
                    val post = Post(
                        userJson.getInt("postid"),
                        userJson.getString("userid"),
                        userJson.getString("postimg"),
                        userJson.getString("caption")
                    )

                    followedUsersList.add(post)
                }
                onSuccess(followedUsersList)
            },
            { error ->
            }
        )

        queue.add(jsonObjectRequest)

    }

    fun getStats(onSuccess: (List<Int>) -> Unit, onError: (String) -> Unit){
        val url = "$url/stats"

        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                val stats = mutableListOf<Int>()

                stats.add(response.getInt("total_users"))
                stats.add(response.getInt("total_likes"))

                onSuccess(stats)

            },
            { error ->
                // Handle error
                onError(error.message ?: "Unknown error occurred")
            }
        )

        queue.add(request)
    }

    fun createComment(postid: Int, userid: String, commentContent: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val jsonObject = JSONObject().apply {
            put("postid", postid)
            put("userid", userid)
            put("comment_content", commentContent)
        }

        val request = JsonObjectRequest(
            Request.Method.POST, "$url/create_comment", jsonObject,
            { response ->
                // Comment created successfully
                onSuccess()
            },
            { error ->
                onError("Error: ${error.message}")
            }
        )

        // Add the request to the RequestQueue.
        queue.add(request)
    }
}