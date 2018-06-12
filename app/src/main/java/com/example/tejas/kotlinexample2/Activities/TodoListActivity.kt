package com.example.tejas.kotlinexample2.Activities

import android.support.v4.app.Fragment
import com.example.tejas.kotlinexample2.Fragments.ToDoListFragment

class TodoListActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment = ToDoListFragment()

}
