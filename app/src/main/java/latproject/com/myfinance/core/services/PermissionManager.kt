package latproject.com.myfinance.core.services

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast

class PermissionManager(val activity: Activity) {

    fun checkPermissionForSmsSend(): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForSmsRead(): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionForSmsReceive(): Boolean {
        val result = ContextCompat.checkSelfPermission(activity, Manifest.permission.RECEIVE_SMS)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissionForSmsReceive() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.RECEIVE_SMS)) {
            Toast.makeText(activity, "Sms permission is required", Toast.LENGTH_LONG).show()
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.RECEIVE_SMS), SMS_RECEIVE_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.RECEIVE_SMS), SMS_RECEIVE_REQUEST_CODE)
        }
    }

    fun requestPermissionForSmsSend() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.SEND_SMS)) {
            Toast.makeText(activity, "Sms permission is required", Toast.LENGTH_LONG).show()
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.SEND_SMS), SMS_SEND_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.SEND_SMS), SMS_SEND_REQUEST_CODE)
        }
    }

    fun requestPermissionToReadSms() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_SMS)) {
            Toast.makeText(activity, "Sms permission is required", Toast.LENGTH_LONG).show()
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_SMS), SMS_READ_REQUEST_CODE)
        } else {
            ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.READ_SMS), SMS_READ_REQUEST_CODE)
        }
    }

    companion object {
        val SMS_RECEIVE_REQUEST_CODE = 1
        val SMS_SEND_REQUEST_CODE = 2
        val SMS_READ_REQUEST_CODE = 3
    }
}
