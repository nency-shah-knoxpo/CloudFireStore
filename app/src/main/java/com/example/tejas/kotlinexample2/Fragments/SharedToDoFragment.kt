package com.example.tejas.kotlinexample2.Fragments

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
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.list_todo.*
import kotlinx.android.synthetic.main.list_todo.view.*
import kotlinx.android.synthetic.main.shared_to_do_fragment.view.*
import java.util.*

class SharedToDoFragment : Fragment() {

    private var shareToDoList: ArrayList<ToDo> = ArrayList()
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var adapter = ShareToDoAdapter(shareToDoList)

    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    var uid = mAuth.currentUser!!.uid


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v: View = inflater.inflate(R.layout.shared_to_do_fragment, container, false)
        v.shareToDoRV.layoutManager = LinearLayoutManager(activity)
        v.shareToDoRV.adapter = adapter

        fetchSharedToDos()
        return v
    }

    inner class ShareToDoHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bindToDo(shareTodo: ToDo) {

            itemView.toDOTitleTV.text = shareTodo.mToDoTitle
            itemView.toDoCHB.isChecked = shareTodo.mIsChecked!!
            itemView.editIB.setOnClickListener {
                val todoRef: DocumentReference = db.collection(ToDoCollection.NAME).document(shareTodo.mToDoId!!)
                var isChecked = toDoCHB.isChecked
                val updatedMap: MutableMap<String, Any> = mutableMapOf(ToDoCollection.Fields.TITLE to "Nency:-Updated ToDo", ToDoCollection.Fields.ISCHECKED to isChecked)
                todoRef.update(updatedMap).addOnSuccessListener(OnSuccessListener<Void>() {

                    Toast.makeText(activity, "updated", Toast.LENGTH_SHORT).show()
                })


            }
            itemView.toDoCHB.setOnClickListener {

                val todoRef: DocumentReference = db.collection(ToDoCollection.NAME).document(shareTodo.mToDoId!!)
                var isChecked: Boolean = toDoCHB.isChecked

                val updatedMap: MutableMap<String, Any> = mutableMapOf(ToDoCollection.Fields.ISCHECKED to isChecked)
                todoRef.update(updatedMap)
            }

        }
    }

    private fun fetchSharedToDos() {

        db.collection(ToDoCollection.NAME)
                .whereEqualTo(ToDoCollection.Fields.SHAREID, uid)
                .addSnapshotListener { querySnapshot, _ ->
                    shareToDoList.clear()
                    if (querySnapshot != null) {
                        for (document in querySnapshot.documents) {
                            val todo = ToDo()
                            todo.mToDoId = document.id
                            todo.mToDoTitle = document.get(ToDoCollection.Fields.TITLE).toString()
                            todo.mIsChecked = (document.get(ToDoCollection.Fields.ISCHECKED) as Boolean?)
                            shareToDoList.add(todo)
                        }
                    }
                    adapter.notifyDataSetChanged()

                }


    }

    inner class ShareToDoAdapter(shareToDos: ArrayList<ToDo>) : RecyclerView.Adapter<ShareToDoHolder>() {

        private var mShareToDos: ArrayList<ToDo> = ArrayList()

        init {
            mShareToDos = shareToDos
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShareToDoHolder {
            val layoutInflater = LayoutInflater.from(activity)
            val view = layoutInflater
                    .inflate(R.layout.list_todo, parent, false)
            return ShareToDoHolder(view)

        }

        override fun getItemCount() = mShareToDos.size


        override fun onBindViewHolder(holder: ShareToDoHolder, position: Int) {

            var todo: ToDo = mShareToDos[position]
            holder.bindToDo(todo)
        }

    }

}
