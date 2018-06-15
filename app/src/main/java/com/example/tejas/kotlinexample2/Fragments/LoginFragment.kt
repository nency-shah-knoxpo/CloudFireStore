package com.example.tejas.kotlinexample2.Fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tejas.kotlinexample2.Activities.MainActivity
import com.example.tejas.kotlinexample2.Activities.TodoListActivity
import com.example.tejas.kotlinexample2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment() {
    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()

        var mUser: FirebaseUser? = mAuth.currentUser!!

        if (mUser == null) {
            var i: Intent = MainActivity.getStartIntent(this.context!!)
            startActivity(i)
        }
        else{
            var i: Intent = TodoListActivity.getStartIntent(this.context!!)
            startActivity(i)

        }

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v: View = inflater.inflate(R.layout.fragment_login, container, false)

        v.signInBtn.setOnClickListener {


            mAuth.signInWithEmailAndPassword(emailET.text.toString(), passwordET.text.toString())
                    .addOnSuccessListener {
                        Toast.makeText(activity,"success",Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(activity,it.message,Toast.LENGTH_SHORT).show()

                    }
                    .addOnCompleteListener {
                        if (it.isSuccessful) {

                            var i: Intent = TodoListActivity.getStartIntent(context!!)
                            startActivity(i)
                        } else {

                            Toast.makeText(activity, "invalid emailId or password", Toast.LENGTH_SHORT).show()
                        }

                    }
        }




        return v
    }

}
