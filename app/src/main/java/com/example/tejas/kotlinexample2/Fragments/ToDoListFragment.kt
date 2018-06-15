package com.example.tejas.kotlinexample2.Fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.tejas.kotlinexample2.Models.ToDo
import com.example.tejas.kotlinexample2.Models.ToDoCollection
import com.example.tejas.kotlinexample2.R
import com.example.tejas.kotlinexample2.ShareIdDialogBox
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_todo_list.view.*
import kotlinx.android.synthetic.main.list_todo.*
import kotlinx.android.synthetic.main.list_todo.view.*
import java.util.*


private val TAG = "ToDoListFragment"

class ToDoListFragment : Fragment(){


    var count = 0

    private var toDoList: ArrayList<ToDo> = ArrayList()
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private var adapter = ToDoAdapter(toDoList)
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val REQUEST_USER_ID = 0
    private var todoId: String? = null
    private var shareUserId: String? = null
    private var uid = mAuth.currentUser!!.uid

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v: View = inflater.inflate(R.layout.fragment_todo_list, container, false)

        v.toDoRV.layoutManager = LinearLayoutManager(activity)
        v.toDoRV.adapter = adapter

        fetchData()

        v.addToDoFAB.setOnClickListener {
            addData()
        }


        return v
    }


    inner class ToDoHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

        fun bindToDo(todo: ToDo) {

            itemView.toDOTitleTV.text = todo.mToDoTitle
            itemView.toDoCHB.isChecked = todo.mIsChecked!!
            itemView.deleteIB.setOnClickListener {

                db.collection(ToDoCollection.NAME).document(todo.mToDoId!!).delete()


            }

            itemView.shareToDoBtn.setOnClickListener {
                showDialog(todo.mToDoId!!)


            }

            itemView.editIB.setOnClickListener {

                val todoRef: DocumentReference = db.collection(ToDoCollection.NAME).document(todo.mToDoId!!)
                var isChecked: Boolean = toDoCHB.isChecked
                val updatedMap: MutableMap<String, Any> = mutableMapOf(ToDoCollection.Fields.TITLE to "Nency:-Updated ToDo", ToDoCollection.Fields.ISCHECKED to isChecked)
                todoRef.update(updatedMap).addOnSuccessListener(OnSuccessListener<Void>() {

                    Toast.makeText(activity, "updated", Toast.LENGTH_SHORT).show()
                })

            }

            itemView.toDoCHB.setOnClickListener {

                val todoRef: DocumentReference = db.collection(ToDoCollection.NAME).document(todo.mToDoId!!)
                var isChecked: Boolean = toDoCHB.isChecked

                val updatedMap: MutableMap<String, Any> = mutableMapOf(ToDoCollection.Fields.ISCHECKED to isChecked)
                todoRef.update(updatedMap)
            }

        }

    }


    fun showDialog(todoId: String) {
        var dialog = ShareIdDialogBox.newInstance(todoId)
        var fm = this.fragmentManager
        dialog.setTargetFragment(this, REQUEST_USER_ID)
        dialog.show(fm, "dialog")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_USER_ID) {

            todoId = data!!.getStringExtra(ShareIdDialogBox.ARGS_TODO_ID)
            shareUserId = data.getStringExtra(ShareIdDialogBox.ARGS_SHARED_USER_ID)
            val todoRef: DocumentReference = db.collection(ToDoCollection.NAME).document(this.todoId!!)
            var isChecked: Boolean = toDoCHB.isChecked

            val updatedMap: MutableMap<String, Any?> = mutableMapOf(ToDoCollection.Fields.TITLE to "Nency:-Updated ToDo", ToDoCollection.Fields.ISCHECKED to isChecked, ToDoCollection.Fields.SHAREID to shareUserId)
            todoRef.update(updatedMap)


        }
    }


    inner class ToDoAdapter(toDos: ArrayList<ToDo>) : RecyclerView.Adapter<ToDoHolder>() {

        private var mToDos: ArrayList<ToDo> = ArrayList()

        init {
            mToDos = toDos
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoHolder {
            val layoutInflater = LayoutInflater.from(activity)
            val view = layoutInflater
                    .inflate(R.layout.list_todo, parent, false)
            return ToDoHolder(view)

        }

        override fun getItemCount() = mToDos.size


        override fun onBindViewHolder(holder: ToDoHolder, position: Int) {

            var todo: ToDo = mToDos.get(position)
            holder.bindToDo(todo)
        }

    }


    private fun fetchData() {

        db.collection(ToDoCollection.NAME)
                .whereEqualTo(ToDoCollection.Fields.USERID, uid)
                .addSnapshotListener { querySnapshot, error ->
                    toDoList.clear()
                    if (querySnapshot != null) {
                        for (document in querySnapshot.documents) {
                            val todo = ToDo()
                            todo.mToDoId = document.id
                            todo.mToDoTitle = document.get(ToDoCollection.Fields.TITLE).toString()
                            todo.mIsChecked = (document.get(ToDoCollection.Fields.ISCHECKED) as Boolean?)
                            toDoList.add(todo)
                        }
                    }
                    Toast.makeText(activity, error?.message ?: "No error", Toast.LENGTH_LONG).show()
                    adapter.notifyDataSetChanged()

                }


    }

    private fun fetchData(db: FirebaseFirestore, mAdapter: ToDoAdapter) {
        db.collection("todoList").get()
                .addOnCompleteListener {


                    if (it.isSuccessful) {
                        var todo: ToDo?

                        for (document in it.result) {
                            todo = ToDo()
                            todo.mUserId = document.get(ToDoCollection.Fields.USERID).toString()
                            todo.mToDoId = document.id
                            todo.mToDoTitle = document.get(ToDoCollection.Fields.TITLE).toString()
                            todo.mIsChecked = (document.get(ToDoCollection.Fields.ISCHECKED) as Boolean?)
                            toDoList.add(todo)
                        }


                    }

                    mAdapter.notifyDataSetChanged()
                }

    }

    private fun addData() {

        val map: MutableMap<String, Any> = mutableMapOf(ToDoCollection.Fields.TITLE to "Nency:-todo $count", ToDoCollection.Fields.ISCHECKED to false, ToDoCollection.Fields.USERID to uid)
        db.collection(ToDoCollection.NAME).add(map as Map<String, Any>)
        adapter.notifyDataSetChanged()
        count++
    }


}

