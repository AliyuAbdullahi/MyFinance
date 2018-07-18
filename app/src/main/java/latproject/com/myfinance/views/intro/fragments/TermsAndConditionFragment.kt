package latproject.com.myfinance.views.intro.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import latproject.com.myfinance.R
import latproject.com.myfinance.core.view.CoreFragment
import latproject.com.myfinance.databinding.LayoutTermsAndConditionBinding

class TermsAndConditionFragment: CoreFragment() {
    lateinit var binding: LayoutTermsAndConditionBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_terms_and_condition, container, false)
        binding = LayoutTermsAndConditionBinding.bind(view)
        return binding.root
    }
}