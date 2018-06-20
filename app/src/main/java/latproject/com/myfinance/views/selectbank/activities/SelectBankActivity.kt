package latproject.com.myfinance.views.selectbank.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import latproject.com.myfinance.R
import latproject.com.myfinance.core.globals.navigateTo
import latproject.com.myfinance.core.model.Bank
import latproject.com.myfinance.core.room.User
import latproject.com.myfinance.core.view.CoreActivity
import latproject.com.myfinance.databinding.ActivitySelectBankBinding
import latproject.com.myfinance.views.homescreen.activities.HomeActivity
import latproject.com.myfinance.views.selectbank.adapters.BankListAdapter
import latproject.com.myfinance.views.selectbank.viewmodels.SelectBankVIewModel
import java.util.*

class SelectBankActivity : CoreActivity(), BankListAdapter.OnBankSelectedListener {

    lateinit var binding: ActivitySelectBankBinding
    lateinit var viewModel: SelectBankVIewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_bank)
        binding.handler = SelectBankHandler()
        viewModel = SelectBankVIewModel(this)
        binding.viewModel = viewModel

        if (bankSelected()) {
            openHome()
        } else {
            setSupportActionBar(binding.toolbar)
            setUpRecyclerView()
        }
    }

    private fun setUpRecyclerView() {
        val layoutManager = GridLayoutManager(this, 2)
        binding.bankList.layoutManager = layoutManager

        val adapter = BankListAdapter()
        adapter.setOnBankSelectedListener(this)
        binding.bankList.adapter = adapter

        viewModel.getAllBanks(this) {
            adapter.addBanks(it)
        }
    }

    private fun bankSelected(): Boolean {
        return viewModel.dataStore.getUser() != null && viewModel.dataStore.getUser()!!.bank.isNotEmpty()
    }

    private var bank: Bank? = null
    val user = User()

    override fun onBankSelected(bank: Bank) {
        this.bank = bank

        if (bank.name != null) {
            val bankName = bank.name
            user.bank = bankName!!
            user.id = "_User_${UUID.randomUUID()}"
            if (bank.textColor != null)
                user.bankTextColor = bank.textColor!!
            if (bank.backgroundColor != null)
                user.bankBackGroundColor = bank.backgroundColor!!
        }

        val dialogTitle = getString(R.string.bank_selection_confirmation)
        val dialogMessage = getString(R.string.bank_name_confirmation, bank.name)
        showDialog(dialogTitle, dialogMessage)
    }

    fun showDialog(title: String, message: String) {
        viewModel.dialogShowing = true
        binding.confirmDialogLink?.dialogTitle?.text = title
        binding.confirmDialogLink?.dialogMessage?.text = message
    }

    fun hideDialog() {
        viewModel.dialogShowing = false
    }

    fun openHome() {
        navigateTo<HomeActivity>()
        finish()
    }

    inner class SelectBankHandler {
        fun onDialogOkayClicked(view: View) {
            hideDialog()
            viewModel.dataStore.saveUser(user, {
                openHome()
            })
        }

        fun onDialogCancelClicked(view: View) {
            hideDialog()
        }
    }
}
