package com.example.tejas.kotlinexample2.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.tejas.kotlinexample2.Fragments.LoginFragment
import com.example.tejas.kotlinexample2.R

class MainActivity:SingleFragmentActivity() {

    companion object {
        fun getStartIntent(context: Context): Intent {
            val intent = Intent(context, MainActivity::class.java)
            return intent
        }

    }
    override fun createFragment(): Fragment {
return LoginFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

}