package com.example.hongca

import java.io.Serializable

data class MyData(var word:String, var meaning:String, var star:String, var noteTitle:String) : Serializable{
    constructor():this("noinfo","noinfo","false", "noinfo") //디폴트 생성자
}
