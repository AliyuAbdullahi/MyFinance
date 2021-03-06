package latproject.com.myfinance.views.budgets.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import latproject.com.myfinance.R
import latproject.com.myfinance.core.globals.makeToast
import latproject.com.myfinance.core.globals.navigateTo
import latproject.com.myfinance.core.room.Budget
import latproject.com.myfinance.core.view.CoreActivity
import latproject.com.myfinance.databinding.ActivityBudgetsBinding
import latproject.com.myfinance.views.budgets.adapters.BudgetListAdapter
import latproject.com.myfinance.views.budgets.viewmodels.BudgetListViewModel

class BudgetsListActivity : CoreActivity(), BudgetListAdapter.OnBudgetActivatedListener {

    override fun onBudgetActivated(budget: Budget, activated: Boolean, position: Int) {

        if (budget.active) {
            AlertDialog.Builder(this).setTitle("Delete Budget")
                    .setMessage("Are you sure you want to delete this budget?")
                    .setPositiveButton("Yes", { a, b ->
                        adapter.removeBudget(budget)
                        viewModel.deleteBudget(budget)
                    })
                    .setNegativeButton("No", null)
                    .create().show()
            adapter.setChecked(0)
        } else {
            val activeBudget = adapter.getItem(0)
            if(activeBudget.active) {
                viewModel.getRealm().executeTransaction({
                    activeBudget.active = false
                    budget.active = true
                })

                viewModel.saveBudget(activeBudget)
                viewModel.saveBudget(budget)

                adapter.removeBudget(activeBudget)
                adapter.removeBudget(budget)
                adapter.addBudget(activeBudget)
                adapter.addBudgetAt(budget, 0)
            }else {
                viewModel.getRealm().executeTransaction({
                    budget.active = true
                })

                viewModel.saveBudget(budget)

                adapter.setChecked(0)
            }
        }
    }
    
    var activeBudget: Budget? = null

    lateinit var binding: ActivityBudgetsBinding
    lateinit var viewModel: BudgetListViewModel
    var adapter = BudgetListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_budgets)
        setSupportActionBar(binding.toolbar)
        bindStatusBar()
        viewModel = BudgetListViewModel(this)
        binding.handler = BudgetListHandler()
        prepList()
    }

    fun prepList() {
        adapter.onBudgetActivatedLister = this
        binding.budgetList.layoutManager = LinearLayoutManager(this)
        binding.budgetList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        prepList()
        loadBudgets()
    }

    private fun loadBudgets() {
        var listOf = mutableListOf<Budget>()

        listOf.clear()
        adapter.budgets.clear()

        var activeBudget = viewModel.getBudgets()?.find { it.active }

        var list = viewModel.getBudgets()?.filter { !it.active }!!.sortedBy { it.dateSet }
        listOf.addAll(list)

        if (activeBudget != null) {
            listOf.add(0, activeBudget)
        }

        if (listOf.size > 0) {
            binding.inActiveBudgetsEmptyState.visibility = View.GONE
        } else {
            binding.inActiveBudgetsEmptyState.visibility = View.VISIBLE
        }

        adapter.addBudgets(listOf)

        checkEmptyState(listOf)
    }

    private fun bindStatusBar() {
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setDisplayShowTitleEnabled(false)
        }
    }

    private fun checkEmptyState(list: List<Budget>?) {
        if (list == null || list.isEmpty()) {
            //showEmptyState
            binding.inActiveBudgetsEmptyState.visibility = View.VISIBLE
        } else {
            binding.inActiveBudgetsEmptyState.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home ->
                onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        setStatusBarColor(R.color.white)
    }

    inner class BudgetListHandler {
        fun createNewBudget(view: View) {
            navigateTo<CreateBudgetActivity>()
        }
    }
}
