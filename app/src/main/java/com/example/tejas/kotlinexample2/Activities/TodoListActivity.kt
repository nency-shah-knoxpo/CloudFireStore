package com.example.tejas.kotlinexample2.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import com.example.tejas.kotlinexample2.Fragments.SharedToDoFragment
import com.example.tejas.kotlinexample2.Fragments.ToDoListFragment
import com.example.tejas.kotlinexample2.R
import kotlinx.android.synthetic.main.activity_todo_list.*
import java.util.*

class TodoListActivity : AppCompatActivity() {

    companion object {
        fun getStartIntent(context: Context) = Intent(context, TodoListActivity::class.java)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_list)
        var mAdapter = FragmentAdapter(supportFragmentManager)
        mAdapter.addFragment("My TODOs")
        mAdapter.addFragment("Shared ToDos")
        viewPager.adapter = mAdapter
        tablayout.setupWithViewPager(viewPager)
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }


    class FragmentAdapter : FragmentStatePagerAdapter {

         var mFragments: ArrayList<Fragment>? = null
        var title: ArrayList<String> = ArrayList()

        constructor(fm: FragmentManager?) : super(fm) {
            mFragments = ArrayList()
        }


        override fun getItem(position: Int): Fragment {
            return mFragments!!.get(position)

        }

        override fun getCount(): Int {
            return title.size
        }

        fun addFragment(name: String) {
            title.add(name)
            mFragments!!.add(ToDoListFragment())
            mFragments!!.add(SharedToDoFragment())
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return title[position]
        }

    }

}
