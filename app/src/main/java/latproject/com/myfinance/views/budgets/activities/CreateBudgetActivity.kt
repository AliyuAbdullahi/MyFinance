package latproject.com.myfinance.views.budgets.activities

import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import latproject.com.myfinance.R
import latproject.com.myfinance.core.globals.Constants
import latproject.com.myfinance.core.globals.clear
import latproject.com.myfinance.core.globals.makeToast
import latproject.com.myfinance.core.room.Budget
import latproject.com.myfinance.core.view.CoreActivity
import latproject.com.myfinance.databinding.ActivityCreateBudgetBinding
import latproject.com.myfinance.views.budgets.viewmodels.CreateBudgetViewModel
import latproject.com.myfinance.views.selectbank.activities.SelectBankActivity

class CreateBudgetActivity : CoreActivity() {
    private lateinit var viewModel: CreateBudgetViewModel
    lateinit var binding: ActivityCreateBudgetBinding
    var budgetAmount: Double = 0.0
    var currentBudgetDuration = 0
    var base: Double = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = CreateBudgetViewModel(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_budget)
        setSupportActionBar(binding.toolbar)
        bindStatusBar()
        binding.viewModel = viewModel
        binding.handler = CreateBudgetHandler()
        binding.budgetAmount.addTextChangedListener(AmountWatcher())
        binding.numberOfDays.addTextChangedListener(AmountWatcher())
        showBaseBalanceOption()
    }

    override fun onResume() {
        super.onResume()
        bindBank()
    }

    override fun onStart() {
        super.onStart()
        setStatusBarColor(R.color.white)
    }

    private fun bindBank() {
        binding.bankName.text = viewModel.getBank()
    }

    private fun bindStatusBar() {
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setDisplayShowTitleEnabled(false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home ->
                onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private val oneDay = 86400000L

    fun createBudget() {
        val budget = Budget()
        budget.amount = budgetAmount
        budget.dateSet = System.currentTimeMillis()
        budget.id = "${System.currentTimeMillis()}$budgetAmount${viewModel.getBank()}"
        budget.active = true
        budget.fulfilled = false
        budget.amountSpent = 0.0
        budget.bank = viewModel.getBank() ?: ""
        val numberOfDays = binding.numberOfDays.text.toString().toLong()
        budget.dateFinished = System.currentTimeMillis() + (oneDay * numberOfDays)
        budget.durationInDays = currentBudgetDuration
        budget.baseAmount = viewModel.base

        viewModel.createBudget(budget, { createdBudget ->
            if(createdBudget != null) {
                makeToast(getString(R.string.budget_created))
                this.finish()
            }else{
                makeToast(getString(R.string.error_in_budget_creation))
            }
        })
    }

    inner class AmountWatcher : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            checkIfAmountIsMoreThanAvailable()
            toggleCreateBudgetButton()
        }
    }

    private fun toggleCreateBudgetButton() {
        if (binding.numberOfDays.text.isEmpty().not() && binding.budgetAmount.text.isEmpty().not()) {
            binding.createBudget.background = ContextCompat.getDrawable(this, R.drawable.create_button_active)
        } else {
            binding.createBudget.background = ContextCompat.getDrawable(this, R.drawable.create_button_not_active)
        }
    }

    private fun checkIfAmountIsMoreThanAvailable() {
        val amount = binding.budgetAmount.text.toString()
        if (amount.isEmpty().not()) {
            val doubleAmount = amount.toDouble()
            if (doubleAmount >= viewModel.base) {
                showOverBudgetDialog()
                binding.budgetAmount.clear()
            } else {
                budgetAmount = amount.toDouble()
            }
        }
    }

    private fun showOverBudgetDialog() {
        AlertDialog.Builder(this).setTitle(getString(R.string.over_budget))
                .setMessage(getString(R.string.balance_too_much))
                .setPositiveButton(getString(R.string.change_base), { dialogInterface, i ->
                    Handler().postDelayed({
                        showBaseBalanceOption()
                    }, 50)

                    dialogInterface.dismiss()
                }).setNegativeButton(getString(R.string.okay), null).create().show()
    }

    private fun suggestAmount() {
        //suggest 50% of income 50/30/20
    }

    val LAST_CREDIT = 0
    val MAIN_BALANCE = 1
    private fun showBaseBalanceOption() {
        val balance = arrayOf<CharSequence>(getString(R.string.last_credit), getString(R.string.main_balance))

        AlertDialog.Builder(this).setTitle(getString(R.string.budget_on)).setItems(balance,
                { dialogInterface: DialogInterface, item: Int ->
                    when (item) {
                        LAST_CREDIT -> viewModel.base = viewModel.getLastCredit()
                        MAIN_BALANCE -> viewModel.base = viewModel.getBaseBalance()
                    }
                    suggestAmount()
                    dialogInterface.dismiss()
                }).create().show()
    }

    private fun navigateToSelectBank() {
        val intent = Intent(this, SelectBankActivity::class.java)
        intent.putExtra(Constants.FROM_ANOTHER_ACTIVITY, true)
        startActivity(intent)
    }

    inner class CreateBudgetHandler {
        fun createBudget(view: View) {
            checkForFields({ valid ->
                if (valid) {
                    createBudget()
                }
            })
        }

        fun navigateToSelectBank(view: View) {
            navigateToSelectBank()
        }
    }

    fun checkForFields(fieldsValid: (valid: Boolean) -> Unit) {
        if (binding.budgetAmount.text.isEmpty()) {
            binding.budgetAmount.error = getString(R.string.enter_valid_budget)
        }

        if (binding.numberOfDays.text.isEmpty()) {
            binding.numberOfDays.error = getString(R.string.enter_valid_number_of_days)
        }

        fieldsValid(binding.budgetAmount.text.isEmpty().not() && binding.numberOfDays.text.isEmpty().not())
    }
}
