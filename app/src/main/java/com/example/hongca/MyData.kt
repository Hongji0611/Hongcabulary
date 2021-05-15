package com.example.hongca

import java.io.Serializable

data class MyData(var id:Int = 0, var title:String, var word:String, var meaning:String, var star:String="false") : Serializable{
}
