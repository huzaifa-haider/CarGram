package com.example.cargram

class Like(val likeid : Int, val postid : Int, likedby : String) {
    constructor() : this(0, 0, "")
}