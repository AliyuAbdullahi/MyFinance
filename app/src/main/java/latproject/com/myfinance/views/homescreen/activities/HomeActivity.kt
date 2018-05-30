package latproject.com.myfinance.views.homescreen.activities

import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*
import latproject.com.myfinance.R
import latproject.com.myfinance.core.services.PermissionManager
import latproject.com.myfinance.core.services.sms.SmsListener
import latproject.com.myfinance.core.services.sms.SmsReader
import latproject.com.myfinance.core.services.sms.SmsReceiver
import latproject.com.myfinance.core.view.CoreActivity

class HomeActivity : CoreActivity(), SmsListener {
    override fun onMessageReceived(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        checkPermissionForSMS()
    }

    private fun checkPermissionForSMS() {
        val permissionManager = PermissionManager(this)

        if(!permissionManager.checkPermissionForSmsReceive()) {
            permissionManager.requestPermissionForSmsReceive()
        } else {
            initSms()
        }
    }

    fun initSms() {
        SmsReceiver.bindListener(this)

        val smsReader = SmsReader(this)
        smsReader.getMessage().forEach {
            Toast.makeText(this, it.toString(), Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
