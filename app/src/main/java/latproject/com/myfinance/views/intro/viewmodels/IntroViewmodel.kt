package latproject.com.myfinance.views.intro.viewmodels

import android.content.Context
import latproject.com.myfinance.core.model.OnboardingStatus
import latproject.com.myfinance.core.view.CoreViewModel

class IntroViewmodel(context: Context): CoreViewModel(context) {

    fun saveIntro(onboardingStatus: OnboardingStatus){
        dataStore.save(onboardingStatus)
    }

    fun onBoardingComplete():Boolean {
       return  dataStore.getOnboarding() != null && dataStore.getOnboarding()!!.isNotEmpty() && dataStore.getOnboarding()!!.size > 0
    }
}