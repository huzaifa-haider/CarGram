package com.example.cargram

class User(val userid : String, val username : String, val name : String, val email : String, val password : String, val phone : String, val country : String, val profilepic : String, val bio : String, val fcmtoken : String, val type : String) {
    constructor() : this("", "", "", "", "", "", "", "", "","", "")
}