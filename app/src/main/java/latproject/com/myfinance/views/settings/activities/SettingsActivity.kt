package latproject.com.myfinance.views.settings.activities

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import latproject.com.myfinance.R
import latproject.com.myfinance.core.globals.Constants
import latproject.com.myfinance.core.view.CoreActivity
import latproject.com.myfinance.databinding.ActivitySettingsBinding
import latproject.com.myfinance.views.selectbank.activities.SelectBankActivity

class SettingsActivity : CoreActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        binding.handler = SettingsHandler()
        setSupportActionBar(binding.toolbar)
        bindStatusBar()
    }

    private fun bindStatusBar() {
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setDisplayShowTitleEnabled(false)
        }
    }

    override fun onStart() {
        super.onStart()
        setStatusBarColor(R.color.white)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home ->
                onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun navigateToSelectBank() {
        val intent = Intent(this, SelectBankActivity::class.java)
        intent.putExtra(Constants.FROM_ANOTHER_ACTIVITY, true)
        startActivity(intent)
    }

    inner class SettingsHandler {
        fun onChangeBankClicked(view: View) {
            navigateToSelectBank()
        }
    }
}
