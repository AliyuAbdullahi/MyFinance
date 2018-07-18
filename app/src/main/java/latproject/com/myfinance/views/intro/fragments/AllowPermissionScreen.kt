package latproject.com.myfinance.views.intro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import latproject.com.myfinance.R
import latproject.com.myfinance.core.view.CoreFragment
import latproject.com.myfinance.databinding.LayoutAllowPermissionBinding

class AllowPermissionScreen: CoreFragment() {
    lateinit var binding: LayoutAllowPermissionBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_allow_permission, container, false)
        binding = LayoutAllowPermissionBinding.bind(view)
        return binding.root
    }
}