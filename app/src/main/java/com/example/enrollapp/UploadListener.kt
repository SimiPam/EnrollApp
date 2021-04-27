package com.example.enrollapp

import android.widget.Toast
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState
import timber.log.Timber
import java.lang.ref.WeakReference

class UploadListener(context: Onboard_activity) : TransferListener {
    private val activityReference: WeakReference<Onboard_activity> = WeakReference(context)
    val activity = activityReference.get()


    private fun showToast(str: String) {
        Toast.makeText(activity, str, Toast.LENGTH_SHORT).show()
    }

    override fun onStateChanged(id: Int, state: TransferState?) {

        Timber.d( "onStateChanged: $id, $state")



        if (state == TransferState.COMPLETED) {
            Timber.d("UPLOAD SUCCESS")
            activity?.loading()



        }  // If upload error, failed or network disconnect
        if (state == TransferState.CANCELED || state == TransferState.FAILED || state == TransferState.WAITING_FOR_NETWORK) {
            Timber.e("UPLOAD FAILED")
        }

    }

    override fun onError(id: Int, ex: Exception) {
        Timber.e("UPLOAD FAILED ${ex}")
    }

    override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {

    }

}
