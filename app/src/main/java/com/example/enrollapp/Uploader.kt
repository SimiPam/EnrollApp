package com.example.enrollapp

import android.content.Context
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import java.io.File
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import timber.log.Timber


interface Uploader {
    fun uploadImage(uploadFile: File, uploadFileName:String, observer:SingleLiveEvent<UploadResult>)
}
class UploaderImpl(private val basicAWSCredentials: BasicAWSCredentials, private val context: Context):Uploader{
    override fun uploadImage(uploadFile: File,uploadFileName:String,observer:SingleLiveEvent<UploadResult>) {
        observer.value = UploadResult.Loading(true)
        S3Uploader.upload(context,basicAWSCredentials,"BUCKETNAME",uploadFile,"${uploadFileName}.txt").setTransferListener(object :
            TransferListener {
            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
            }
            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state == TransferState.COMPLETED) {
                    Timber.d("UPLOAD SUCCESS")
                    observer.value =  UploadResult.Success("uploadFileName.png")
                } else if (state == TransferState.FAILED) {
                    Timber.e("UPLOAD FAILED")
                    observer.value = UploadResult.Loading(false)
                    observer.value =   UploadResult.Failed("UPLOAD FAILED")
                }
            }
            override fun onError(id: Int, ex: Exception) {
                Timber.e("UPLOAD FAILED ${ex}")
                observer.value = UploadResult.Loading(false)
                observer.value =  UploadResult.Error(ex)
            }
        })
    }
}
sealed class UploadResult {
    class Success(val imageURL: String) : UploadResult()
    class Failed(val errorMessage: String) : UploadResult()
    class Error(val exception: Throwable) : UploadResult()
    class Loading(val showLoading: Boolean) : UploadResult()
}

