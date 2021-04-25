package com.example.enrollapp

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.example.enrollapp.uitel.LoadingBar
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.*


class Onboard_activity : AppCompatActivity() {


    // variables
    private var mUri: Uri? = null
    private var textFirstName: String = ""
    private var textSurname: String = ""
    private var textEmail: String = ""
    private var textPhoneNumber: String = ""
    private var gender: String = ""
    private var imageUri: Uri? = null
    private var takenImage: Bitmap? = null
    private lateinit var mPhotoFile: File
    private lateinit var currentPhotoPath: String


    // widgets
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
    private lateinit var warningText: TextView


    //constants
    private val PICK_IMAGE = 100
    private val REQUEST_IMAGE_CAPTURE = 1


    private fun initializeWidgets() {
        btnCapture = findViewById(R.id.image_btn)
        warningText = findViewById(R.id.warning_log)
        btnHome = findViewById(R.id.home_btn)
        btnSubmit = findViewById(R.id.submit_btn)
        btnFemale = findViewById(R.id.btn_female)
        btnMale = findViewById(R.id.btn_male)
        etFirstName = findViewById(R.id.fname)
        etSurname = findViewById(R.id.sname)
        etPhoneNumber = findViewById(R.id.phone_number)
        etEmail = findViewById(R.id.email)
        btnCancel = findViewById(R.id.cancel_btn)
    }

    fun showToast(str: String) {
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
            // Set whether to include the application home affordance in the action bar.
            setDisplayShowHomeEnabled(true)
            // Set whether to display the activity logo rather than the activity icon. A logo is often a wider, more detailed image.
            setHomeAsUpIndicator(R.drawable.leftarrow)
            // Set the logo to display in the 'home' section of the action bar.
            setLogo(R.drawable.logo1)
        }
    }

    inner class MyAsyncTask : AsyncTask<String, Int, Boolean>() {

        override fun onPreExecute() {
            super.onPreExecute()
            showToast("$textFirstName")
            LoadingBar(this@Onboard_activity).startLoading()
        }

        override fun doInBackground(vararg params: String?): Boolean {
            try {
                val fileOutputStream: FileOutputStream =
                    openFileOutput("$params.txt", Context.MODE_PRIVATE)
                val outputWriter = OutputStreamWriter(fileOutputStream)
                outputWriter.write("takenImage: $takenImage")
                outputWriter.append("\n mUri: $mUri")
                outputWriter.append("\n textFirstName: $textFirstName")
                outputWriter.append("\n textSurname: $textSurname")
                outputWriter.append("\n textEmail: $textEmail")
                outputWriter.append("\n textPhoneNumber: $textPhoneNumber")
                outputWriter.append("\n gender: $gender")
                outputWriter.close()

                return true
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)

            val loading = LoadingBar(this@Onboard_activity)
            loading.isSuccess()
            val handler = Handler()
            handler.postDelayed({
                loading.finalScreen()
                val inflater: LayoutInflater = getLayoutInflater()
                val view: View = inflater.inflate(R.layout.success_screen, null)
                var  proceedBtn: Button = view.findViewById(R.id.proced_btn)
                proceedBtn.setOnClickListener {
                    setEditText()
                    loading.isDismiss()
                }
            }, 5000)
        }
    }//end inner class


        private fun captureButtonStateChange(padding: Int, view: Int) {
        btnCapture.setPadding(padding, padding, padding, padding)
        btnCancel.visibility = view
    }

    private fun setEditText() {
        etFirstName.text.clear()
        etSurname.text.clear()
        etEmail.text.clear()
        etPhoneNumber.text.clear()
        gender = "gender"
        btnCapture.setImageResource(R.drawable.plus)
        captureButtonStateChange(80, 4)
        setVariables()
    }

    private fun setVariables() {
        textFirstName = etFirstName.text.toString()
        textSurname = etSurname.text.toString()
        textEmail = etEmail.text.toString()
        textPhoneNumber = etPhoneNumber.text.toString()
        gender = gender
    }

    private fun validatorCheck(): Boolean {
        setVariables()
        var validator = Validator()
        return (validator.isNameValid(textFirstName)
                && validator.isNameValid(textSurname)
                && validator.isEmailValid(textEmail)
                && validator.isGenderValid(gender)
                && validator.isNumberValid(textPhoneNumber))

    }


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

    private fun choosePictureIntent() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        try {
            startActivityForResult(gallery, PICK_IMAGE)
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                takenImage = BitmapFactory.decodeFile(mPhotoFile.absolutePath)
                imageUri = null
                btnCapture.setImageBitmap(takenImage)
            } else if (requestCode == PICK_IMAGE) {
                takenImage = null
                imageUri = data?.data
                btnCapture.setImageURI(imageUri)
            }
            captureButtonStateChange(0, 0)
        }

    }

    private val listener = View.OnClickListener { view ->
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

    private val cancelListener = View.OnClickListener { view ->
        when (view.getId()) {
            R.id.cancel_btn -> {
                btnCapture.setImageResource(R.drawable.plus)
                captureButtonStateChange(80, 4)
            }
        }
    }

    private val femaleListener = View.OnClickListener { view ->
        when (view.getId()) {
            R.id.btn_female -> {
                gender = "female"
            }
        }
    }

    private val captureListener = View.OnClickListener { view ->
        when (view.getId()) {
            R.id.image_btn -> {
                val dialog = MaterialDialog(this).message(text = "Please select one")
                dialog.show()

                dialog.positiveButton(text = "Select Picture") { dialog ->
                    choosePictureIntent()
                }
                dialog.negativeButton(text = "Take Picture") { dialog ->
                    dispatchTakePictureIntent()
                }
            }
        }
    }

    private val maleListener = View.OnClickListener { view ->
        when (view.getId()) {
            R.id.btn_male -> {
                gender = "male"
            }
        }
    }


    private val submitListener = View.OnClickListener { view ->
        when (view.getId()) {
            R.id.submit_btn -> {
                if (validatorCheck()) {
                    var user = User(
                        mUri = mUri, takenImage = takenImage,
                        textFirstName = textFirstName, textSurname = textSurname,
                        textEmail = textEmail, textPhoneNumber = textPhoneNumber, gender = gender
                    )

                    val task = MyAsyncTask()
                    task.execute("myEnrollFile")
                } else {
                    val dialog = MaterialDialog(this).title(text = "Error")
                        .message(text = "All fields required! Please recheck entries")
                }
            }
        }
    }
}// onboard class end