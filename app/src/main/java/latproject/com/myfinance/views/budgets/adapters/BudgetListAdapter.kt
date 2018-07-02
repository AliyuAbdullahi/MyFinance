package latproject.com.myfinance.views.budgets.adapters

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import latproject.com.myfinance.R
import latproject.com.myfinance.core.room.Budget
import latproject.com.myfinance.core.utils.FormattingUtility
import latproject.com.myfinance.databinding.LayoutBudgetListBinding

class BudgetListAdapter : RecyclerView.Adapter<BudgetListAdapter.BudgetListViewHolder>() {
    var budgets = mutableListOf<Budget>()
    var onBudgetActivatedLister: OnBudgetActivatedListener? = null

    fun setBudgetActivationListener(listener: OnBudgetActivatedListener) {
        this.onBudgetActivatedLister = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_budget_list, parent, false)
        return BudgetListViewHolder(view)
    }

    fun removeBudget(budget: Budget) {
        val budgetPosition = budgets.indexOf(budget)
        this.budgets.remove(budget)
        notifyItemRemoved(budgetPosition)
    }

    fun addBudget(budget: Budget) {
        budgets.add(budget)
        notifyItemInserted(budgets.size)
    }

    fun addBudgetAt(budget: Budget, position: Int) {
        budgets.add(position, budget)
        notifyItemInserted(position)
        notifyItemRangeChanged(0, 1)
    }

    fun addBudgets(budgets: List<Budget>) {
        for (budget in budgets) {
            if (this.budgets.contains(budget).not()) {
                this.budgets.add(budget)
                notifyItemInserted(budgets.size)
            }
        }
    }

    override fun getItemCount(): Int {
        return budgets.size
    }

    override fun onBindViewHolder(holder: BudgetListViewHolder, position: Int) {
        holder.bindTo(budgets[position])
    }

    inner class BudgetListViewHolder(view: View) : RecyclerView.ViewHolder(view), CompoundButton.OnCheckedChangeListener {

        lateinit var budgetListBinding: LayoutBudgetListBinding
        lateinit var budget: Budget
        override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
            if (p1) {
                if (onBudgetActivatedLister != null) {
                    onBudgetActivatedLister!!.onBudgetActivated(budget)
                }
            }
            updateSwitchBackground(p1)
        }

        init {
            budgetListBinding = LayoutBudgetListBinding.bind(view)
            budgetListBinding.activateSwitch.setOnCheckedChangeListener(this)
        }

        fun bindTo(budget: Budget) {
            this.budget = budget
            budgetListBinding.createdAt.text = "Created on\n${FormattingUtility.getDateTime(budget.dateSet)}"
            budgetListBinding.expireOn.text = "Expire on\n${FormattingUtility.getDateTime(budget.dateFinished)}"
            budgetListBinding.baseBalance.text = "${String.format("%.0f", budget.baseAmount)}"
            budgetListBinding.budgetAmount.text = "${String.format("%.0f", budget.amount)}"

            if (budget.getCurrentStatus().toLowerCase().contains("expired")) {
                budgetListBinding.activateSwitchContainer.visibility = View.GONE
            } else {

                budgetListBinding.activateSwitchContainer.visibility = View.VISIBLE
            }

            updateSwitchBackground(budget.active)

            budgetListBinding.status.text = budget.getCurrentStatus()
        }

        private fun updateSwitchBackground(isChecked: Boolean) {
            if (isChecked) {
                budgetListBinding.activateSwitch.backDrawable = ContextCompat.getDrawable(budgetListBinding.root.context, R.drawable.background_on)
            } else {
                budgetListBinding.activateSwitch.backDrawable = ContextCompat.getDrawable(budgetListBinding.root.context, R.drawable.background_off)
            }
        }
    }

    interface OnBudgetActivatedListener {
        fun onBudgetActivated(budget: Budget)
    }
}