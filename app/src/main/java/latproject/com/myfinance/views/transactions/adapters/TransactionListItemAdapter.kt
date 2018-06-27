package latproject.com.myfinance.views.transactions.adapters

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import latproject.com.myfinance.R
import latproject.com.myfinance.core.room.RealmBankTransaction
import latproject.com.myfinance.core.utils.FormattingUtility
import latproject.com.myfinance.databinding.LayoutTransactionListItemBinding

class TransactionListItemAdapter : RecyclerView.Adapter<TransactionListItemAdapter.TransactionItemViewHolder>() {
    var bankTransactions = mutableListOf<RealmBankTransaction?>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    lateinit var onTransactionClickedListener: OnTransactionClickedListener


    fun setTransactions(bankTransaction: MutableList<RealmBankTransaction?>) {
        this.bankTransactions.addAll(bankTransaction)
        notifyDataSetChanged()
    }

    fun addTransaction(bankTransaction: RealmBankTransaction) {
        this.bankTransactions.add(bankTransaction)
        notifyItemInserted(bankTransactions.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_transaction_list_item, parent, false)

        return TransactionItemViewHolder(view)
    }


    override fun getItemCount(): Int {
        return bankTransactions.size
    }

    override fun onBindViewHolder(holder: TransactionItemViewHolder, position: Int) {
        if (bankTransactions[position] != null) {
            holder.bindTo(bankTransactions[position]!!)
        }
    }

    inner class TransactionItemViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var bankTransaction: RealmBankTransaction

        override fun onClick(p0: View?) {
            onTransactionClickedListener.onTransactionClicked(bankTransaction)
        }

        var binding: LayoutTransactionListItemBinding = LayoutTransactionListItemBinding.bind(view)

        init {
            binding.root.setOnClickListener(this)
            binding.transactionDate.setOnClickListener(this)
            binding.rowItemsContainer.setOnClickListener(this)
        }

        fun bindTo(bankTransaction: RealmBankTransaction) {
            this.bankTransaction = bankTransaction
            parseDebitOrCredit(bankTransaction.isCredit)
            binding.transactionAmount.text = binding.root.context.resources.getString(R.string.amount, bankTransaction.amount)
            binding.transactionSummary.text = bankTransaction.details
            binding.transactionDate.text = FormattingUtility.getDateTime(bankTransaction.date)
        }

        private fun parseDebitOrCredit(credit: Boolean) {
            if (credit) {
                binding.creditOrDebit.text = binding.root.context.resources.getString(R.string.credit)
                binding.creditOrDebit.setTextColor(ContextCompat.getColor(binding.root.context, R.color.color_bank_credit))
            } else {
                binding.creditOrDebit.text = binding.root.context.resources.getString(R.string.debit)
                binding.creditOrDebit.setTextColor(ContextCompat.getColor(binding.root.context, R.color.negative_red))
            }
        }
    }

    interface OnTransactionClickedListener {
        fun onTransactionClicked(bankTransaction: RealmBankTransaction)
    }
}