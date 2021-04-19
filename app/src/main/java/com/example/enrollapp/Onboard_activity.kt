package com.example.enrollapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.afollestad.materialdialogs.MaterialDialog
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.*


class Onboard_activity : AppCompatActivity() {
    //lateinit var imageView: ImageView
    //private val pickImage = 1
    //private lateinit var yourBitmap: Bitmap
    private lateinit var mPhotoFile: File


    //Our variables
    private var mImageView: ImageButton? = null
    private var mUri: Uri? = null
    private var textFirstName: String = ""
    private var textSurname: String = ""
    private var textEmail: String = ""
    private var textPhoneNumber: String = ""
    private var gender: String = ""


    //Our widgets
    private lateinit var btnCapture: ImageButton
    private lateinit var btnHome: ImageButton
    private lateinit var btnSubmit: Button
    private lateinit var btnFemale: Button
    private lateinit var btnCancel: Button
    private lateinit var btnMale: Button
    private lateinit var etFirstName: EditText
    private lateinit var etSurname: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhoneNumber: EditText
//    private lateinit var toggleButton: ToggleButton


    //Our constants
    private val OPERATION_CAPTURE_PHOTO = 1

    private fun initializeWidgets() {
        btnCapture = findViewById(R.id.image_btn)
       // mImageView = findViewById(R.id.image_btn)
        btnHome = findViewById(R.id.home_btn)
        btnSubmit = findViewById(R.id.submit_btn)
        btnFemale = findViewById(R.id.btn_female)
        btnMale = findViewById(R.id.btn_male)
        etFirstName = findViewById(R.id.fname)
        etSurname = findViewById(R.id.sname)
        etPhoneNumber = findViewById(R.id.phone_number)
        etEmail = findViewById(R.id.email)
        btnCancel = findViewById(R.id.cancel_btn)
//        toggleButton = findViewById(R.id.toggle_button_group)
    }

    private fun showToast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboard_activity)






//        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED)
//            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_PHONE_STATE), 111)
//        else
//            getTelephonyDetail()
        initializeWidgets()
        btnHome.setOnClickListener(listener)
        btnSubmit.setOnClickListener(submitListener)
        btnFemale.setOnClickListener(femaleListener)
        btnMale.setOnClickListener(maleListener)
        btnCapture.setOnClickListener(captureListener)
        btnCancel.setOnClickListener(cancelListener)


        supportActionBar?.apply {
            // documentation source developer.android.com
            // Set whether to include the application home affordance in the
            // action bar. Home is presented as either an activity icon or logo.

            setDisplayShowHomeEnabled(true)

            // Set whether to display the activity logo rather than the
            // activity icon. A logo is often a wider, more detailed image.
            setHomeAsUpIndicator(R.drawable.leftarrow)

            // Set the logo to display in the 'home' section of the action bar.
            setLogo(R.drawable.logo1)

        }
//         this event will enable the back
//         function to the button on press
    }

    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            showToast("$currentPhotoPath")
        }
    }



    private val pickImage = 100

    private fun choosePictureIntent() {

        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        try {
            startActivityForResult(gallery, pickImage)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
            val dialog = MaterialDialog(this)
                .title(text = "Gege")
                .message(text = "Nothing is happening o")
            dialog.show()
        }
    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(packageManager)?.also {
                // Create the File where the photo should go
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    val dialog = MaterialDialog(this)
                        .title(text = "Gege")
                        .message(text = "File not created")
                    dialog.show()
                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    mPhotoFile = photoFile
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }
    private var imageUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //val imageBitmap = data?.extras?.get("data") as Bitmap
            val takenImage = BitmapFactory.decodeFile(mPhotoFile.absolutePath)
//            imageView.setImageBitmap(imageBitmap)
            btnCapture.setImageBitmap(takenImage)
        }
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            btnCapture.setImageURI(imageUri)
        }
        btnCapture.setPadding(0,0,0,0)
        btnCancel.visibility =View.VISIBLE
    }

    val listener = View.OnClickListener { view ->
        when (view.getId()) {
            R.id.home_btn -> {
                val dialog = MaterialDialog(this)
                    .title(text = "Exit")
                    .message(text = "Leaving so soon?")
                dialog.show()

                dialog.positiveButton(text = "Yes") { dialog ->
                    finishAffinity()
                }
                dialog.negativeButton(text = "No") { dialog ->
                    dialog.dismiss()
                }
            }
        }
    }

    val cancelListener = View.OnClickListener { view ->
        when (view.getId()) {
            R.id.cancel_btn -> {
                btnCapture.setImageResource(R.drawable.plus)
                btnCapture.setPadding(80,80,80,80)
                btnCancel.visibility =View.INVISIBLE
            }
        }
    }

    val femaleListener = View.OnClickListener { view ->
        when (view.getId()) {
            R.id.btn_female -> {
                gender = "female"
            }
        }
    }
    private var count: Int = 0
    val captureListener = View.OnClickListener { view ->
        when (view.getId()) {
            R.id.image_btn -> {
                val dialog = MaterialDialog(this)
                    .message(text = "Please select one")
                dialog.show()

                dialog.positiveButton(text = "Select Picture") { dialog ->
                    choosePictureIntent()
                }
                dialog.negativeButton(text = "Take Picture") { dialog ->
                    dispatchTakePictureIntent()
                }

                //dispatchTakePictureIntent()
            }
        }
    }

    val maleListener = View.OnClickListener { view ->
        when (view.getId()) {
            R.id.btn_male -> {
                gender = "male"
            }
        }
    }

    val EMAILPATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    val PHONENUMBERPATTERN = "^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}\$"

    fun validator (pattern: String, textInput: String) {
        if (textInput.isEmpty()) {
            Toast.makeText(applicationContext, "pls input", Toast.LENGTH_SHORT).show()
        }
        else {
            if (!(textInput.trim { it <= ' ' }.matches(pattern.toRegex()))) {
                Toast.makeText(applicationContext, "Invalid input", Toast.LENGTH_SHORT).show()
            }
        }
    }


    val submitListener = View.OnClickListener { view ->
        when (view.getId()) {
            R.id.submit_btn -> {
                textFirstName = etFirstName.text.toString()
                textSurname = etSurname.text.toString()
                textEmail = etEmail.text.toString()
                textPhoneNumber = etPhoneNumber.text.toString()

//TODO: create user data class and user object from that class and make these outputs as properties of each person. Save people object
                try {
                    val fileOutputStream: FileOutputStream =
                        openFileOutput("mytextfile.txt", Context.MODE_PRIVATE)
                    val outputWriter = OutputStreamWriter(fileOutputStream)
                    outputWriter.write(textSurname)
                    outputWriter.close()
                    //display file saved message
                    Toast.makeText(baseContext, "File saved successfully!", Toast.LENGTH_SHORT)
                        .show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val dialog = MaterialDialog(this)
                    .title(text = "Exit")
                    .message(text = "gender $gender \n firstname $textFirstName \n surname $textSurname \n email $textEmail \n phone number $textPhoneNumber")
                dialog.show()
            }
        }
    }



}