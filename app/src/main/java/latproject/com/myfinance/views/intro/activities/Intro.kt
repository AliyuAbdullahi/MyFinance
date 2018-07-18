package latproject.com.myfinance.views.intro.activities

import android.Manifest
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.WindowManager
import com.github.paolorotolo.appintro.AppIntro
import com.github.paolorotolo.appintro.AppIntroFragment
import latproject.com.myfinance.R
import latproject.com.myfinance.core.globals.navigateTo
import latproject.com.myfinance.core.model.OnboardingStatus
import latproject.com.myfinance.core.services.PermissionManager
import latproject.com.myfinance.views.homescreen.activities.HomeActivity
import latproject.com.myfinance.views.intro.fragments.AllowPermissionScreen
import latproject.com.myfinance.views.intro.fragments.TermsAndConditionFragment
import latproject.com.myfinance.views.intro.viewmodels.IntroViewmodel
import latproject.com.myfinance.views.selectbank.activities.SelectBankActivity
import java.util.*

class Intro : AppIntro() {
    private lateinit var permissionManager: PermissionManager
    lateinit var viewModel: IntroViewmodel
    internal var hasClickedDone: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionManager = PermissionManager(this)
        viewModel = IntroViewmodel(this)
        checkOnBoardingAndNavigateAccordingly()
    }

    private fun checkOnBoardingAndNavigateAccordingly() {
        if(viewModel.onBoardingComplete()) {
            navigateTo<HomeActivity>()
            finish()
        } else {
            setFadeAnimation()
            checkPermissions()
            addSlide(AppIntroFragment.newInstance("Welcome to MyFinance", "fonts/ProximaNovaSoft_medium.otf", "Keep track of your transactions.\nCreate and manage your budgets.", "fonts/ProximaNovaSoft_medium.otf",
                    R.drawable.app_logo_big, ContextCompat.getColor(this, R.color.white), ContextCompat.getColor(this, R.color.colorAccent), ContextCompat.getColor(this, R.color.colorAccent)))
            addSlide(TermsAndConditionFragment())
            addSlide(AllowPermissionScreen())

            setBarColor(ContextCompat.getColor(this, R.color.colorAccent))
            setSeparatorColor(Color.parseColor("#FFFFFF"))

            showSkipButton(false)
            isProgressButtonEnabled = true

            setVibrate(true)
            setVibrateIntensity(30)
        }
    }

    private fun checkPermissions() {
        if (!permissionManager.checkPermissionForSmsRead()) {
            askForPermissions(arrayOf(Manifest.permission.CAMERA), 2)
        }
    }

    override fun onStart() {
        super.onStart()
        setStatusBarColor(R.color.white)
    }

    fun setStatusBarColor(color: Int) {
        runOnUiThread {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this@Intro, color)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.statusBarColor = getColor(color)
                }
            }
        }
    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        hasClickedDone = true
        if (!permissionManager.checkPermissionForSmsRead()) {
            AlertDialog.Builder(this).setTitle("Permission for SMS Required").setMessage("My finance will use messages from your bank to process your information, please allow permission").setPositiveButton("Ok") { dialogInterface, i -> permissionManager.requestPermissionToReadSms() }.setCancelable(false).setNegativeButton("No", null).create().show()
        } else {
            markOnboardingComplete()
        }
    }

    override fun onSlideChanged(oldFragment: Fragment?, newFragment: Fragment?) {
        super.onSlideChanged(oldFragment, newFragment)
        if (newFragment is AllowPermissionScreen) {
            if (permissionManager.checkPermissionForSmsRead().not()) {
                permissionManager.requestPermissionToReadSms()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (hasClickedDone) {
            markOnboardingComplete()
        }
    }

    private fun markOnboardingComplete() {
        val onboarding = OnboardingStatus()
        onboarding.id = UUID.randomUUID().toString()
        onboarding.status = true

        viewModel.saveIntro(onboarding)
        navigateTo<SelectBankActivity>()
        finish()
    }
}
