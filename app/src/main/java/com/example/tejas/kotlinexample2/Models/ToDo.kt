package com.example.tejas.kotlinexample2.Models


class ToDo {


    var mToDoId: String? = null
    var mToDoTitle: String = ""
    var mIsChecked: Boolean? = false
    var mUserId: String? = null


    override fun toString() = "ID: $mToDoId \n TITLE: $mToDoTitle"


}