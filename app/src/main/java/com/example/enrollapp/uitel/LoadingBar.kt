package com.example.enrollapp.uitel

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.core.content.ContextCompat.startActivity
import com.example.enrollapp.Onboard_activity
import com.example.enrollapp.R

class LoadingBar(val mActivity: Activity) {
    private lateinit var isdialog:AlertDialog
    val inflater = mActivity.layoutInflater
    fun startLoading(){
        //set loading view
        val dialogView =  inflater.inflate(R.layout.loading_item, null)
        //set dialog
        setDialog(dialogView, false)
    }
    fun isDismiss(){
        //remove alert dialog

            isdialog.dismiss()

    }

    fun isSuccess(){
        //set success bar view
        val dialogView =  inflater.inflate(R.layout.success_bar, null)
        //set dialog
        setDialog(dialogView, false)
    }

    fun finalScreen(){
        //set final success view
        val dialogView =  inflater.inflate(R.layout.success_screen, null)
        //set dialog
        setDialog(dialogView, false)
    }



    fun setDialog(dialogView: View, cancelable: Boolean){
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(cancelable)
        isdialog = builder.create()
        isdialog.show()
    }
}