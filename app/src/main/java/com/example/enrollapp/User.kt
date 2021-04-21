package com.example.enrollapp

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import androidx.core.content.FileProvider


class User {
    private var mUri: Uri? = null
    private var textFirstName: String = ""
    private var textSurname: String = ""
    private var textEmail: String = ""
    private var textPhoneNumber: String = ""
    private var gender: String = ""
    private var takenImage: Bitmap? = null



    constructor(
        mUri: Uri? = null,
        takenImage: Bitmap? = null,
        textFirstName: String,
        textSurname: String,
        textEmail: String,
        textPhoneNumber: String,
        gender: String
    ) {
        this.takenImage = takenImage
        this.mUri = mUri
        this.textFirstName = textFirstName
        this.textSurname = textSurname
        this.textEmail = textEmail
        this.textPhoneNumber = textPhoneNumber
        this.gender = gender


    }

}