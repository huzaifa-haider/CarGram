package com.example.cargram

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.IOException

class FCMMessaging(context: Context) {
    val c = context
    val serverKey = "AAAAis4sCog:APA91bExGjIE4k2Syqw-4YtFgmVHTQhmA4VzFS7TFLtSIASL2VtV5r3nxY0j2jwHh1sBj-N0Pr8Q5hiic2GEM5GBwi9UllzaUF13UWYZadMngEp5BkgFfZyjy56X_G1btogAz5Qkmbo-"
    fun sendlikeNotification(usertosend : User, usersendfrom : User) {
        val url = "https://fcm.googleapis.com/fcm/send"
        var requestQueue = Volley.newRequestQueue(c)
        val jsonNotification = JSONObject()
        jsonNotification.put("title", "Like Alert")
        jsonNotification.put("body", "${usersendfrom.name} liked your post")

        val jsonData = JSONObject()
        jsonData.put("type", "like")
        jsonData.put("name", "${usersendfrom.name}")

        val jsonBody = JSONObject()
        jsonBody.put("to", usertosend.fcmtoken)
        jsonBody.put("notification", jsonNotification)
        jsonBody.put("data", jsonData)

        var headers = mutableMapOf<String, String>()
        headers["Authorization"] = "key=$serverKey"
        headers["Content-Type"] = "application/json"

        val request = object : JsonObjectRequest(Request.Method.POST, url, jsonBody,
            { response ->
                // Handle successful response
            },
            { error ->
                // Handle error
            }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "key=$serverKey"
                headers["Content-Type"] = "application/json"
                return headers
            }
        }



    // Add the request to the RequestQueue
        requestQueue.add(request)
    }
}