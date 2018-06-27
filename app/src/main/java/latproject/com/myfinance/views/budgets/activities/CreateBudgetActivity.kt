package latproject.com.myfinance.views.budgets.activities

import android.content.DialogInterface
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import latproject.com.myfinance.R
import latproject.com.myfinance.core.globals.clear
import latproject.com.myfinance.core.globals.makeToast
import latproject.com.myfinance.core.room.Budget
import latproject.com.myfinance.core.room.RealmBankTransaction
import latproject.com.myfinance.core.utils.WeekDay
import latproject.com.myfinance.databinding.ActivityCreateBudgetBinding
import latproject.com.myfinance.views.budgets.viewmodels.CreateBudgetViewModel
import java.util.*

class CreateBudgetActivity : AppCompatActivity() {
    private lateinit var viewModel: CreateBudgetViewModel
    lateinit var binding: ActivityCreateBudgetBinding
    var budgetAmount: Double = 0.0
    var currentBudgetDuration = 0
    var base: Double = 0.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = CreateBudgetViewModel(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_budget)
        binding.viewModel = viewModel
        binding.budgetAmount.addTextChangedListener(AmountWatcher())
        showBaseBalanceOption()
    }

    fun createBudget() {
        val budget = Budget()
        budget.amount = budgetAmount
        budget.dateSet = System.currentTimeMillis()
        budget.id = "${System.currentTimeMillis()}$budgetAmount${viewModel.getBank()}"
        budget.active = true
        budget.fulfilled = false
        budget.amountSpent = 0.0
        budget.bank = viewModel.getBank() ?: ""
        budget.dateFinished = WeekDay.getInstance(System.currentTimeMillis()).rangeTo(currentBudgetDuration)
        budget.durationInDays = currentBudgetDuration
        budget.baseAmount = viewModel.base

        viewModel.createBudget(budget, { createdBudget ->
            makeToast("Budget created")
            this.finish()
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

    inner class CreateBudgetHandler {
        fun createBudget(vie: View) {
            checkForFields({ valid ->
                if (valid) {
                    createBudget()
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        loadTransactions()
    }

    private fun loadTransactions() {
        val bankName = viewModel.getBankName()
        val all = ArrayList<RealmBankTransaction>()

        for (loadTransaction in viewModel.loadTransactions(bankName!!)) {
            if(loadTransaction != null) {
                all.add(loadTransaction)
            }
        }

        all.forEach { viewModel.saveRealmTransaction(it) }

        Handler().postDelayed({
            val allSavedTransactions = viewModel.getTransactionsForBank(bankName)
        }, 10000)
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
