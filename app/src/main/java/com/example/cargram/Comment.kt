package com.example.cargram

class Comment(val commentid : Int, val postid : Int, val commentby : String, val commentcontent : String) {
    constructor() : this(0, 0, "", "")
}