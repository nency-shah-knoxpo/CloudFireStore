package com.example.tejas.kotlinexample2.Activities

import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.tejas.kotlinexample2.Fragments.SharedToDoFragment
import com.example.tejas.kotlinexample2.R

class SharedToDoActivity : SingleFragmentActivity() {
    override fun createFragment(): Fragment {
        return SharedToDoFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_to_do)
    }
}
