package com.example.cargram

class Post(val postid : Int, val userid : String, val postimg : String, val caption : String) {
    constructor() : this(0, "", "", "")
}