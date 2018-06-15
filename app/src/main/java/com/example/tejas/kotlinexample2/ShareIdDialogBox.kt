package com.example.tejas.kotlinexample2

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.dialog.view.*

class ShareIdDialogBox : DialogFragment() {






    companion object {
        val TAG = ShareIdDialogBox::class.java.simpleName
        var ARGS_TODO_ID: String = "ARGS_TODO_ID"
        var ARGS_SHARED_USER_ID: String = "ARGS_TODO_ID"

        fun newInstance(todoId: String): ShareIdDialogBox {
            val args = Bundle()
            args.putString("todoId", todoId)
            val fragment = ShareIdDialogBox()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val v = LayoutInflater.from(activity)
                .inflate(R.layout.dialog, null)

        return AlertDialog.Builder(activity!!)
                .setTitle("Enter userId")
                .setView(v)
                .setPositiveButton("Ok", DialogInterface.OnClickListener { dialog, which ->
                    var shareUserId = v.dialogShareUserIdET.text.toString()
                    sendResult(Activity.RESULT_OK, shareUserId)
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                    sendResult(Activity.RESULT_CANCELED, "")

                })
                .create()
    }

    private fun sendResult(resultCode: Int, shareUserId: String) {
        if (targetFragment == null) {
            return
        }
        val intent = Intent()
        intent.putExtra(ARGS_SHARED_USER_ID, shareUserId)

        val todoId = arguments!!.getString("todoId") as String

        intent.putExtra(ARGS_TODO_ID, todoId)
        targetFragment!!
                .onActivityResult(targetRequestCode, resultCode, intent)
    }

}