package latproject.com.myfinance.views.transactions

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import latproject.com.myfinance.R
import latproject.com.myfinance.databinding.LayoutTransactionListItemBinding
import latproject.com.myfinance.core.room.BankTransaction

class TransactionListItemAdapter: RecyclerView.Adapter<TransactionListItemAdapter.TransactionItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_transaction_list_item, parent, false)

        return TransactionItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: TransactionItemViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    inner class TransactionItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var binding: LayoutTransactionListItemBinding = LayoutTransactionListItemBinding.bind(view)

        fun bindTo(bankTransaction: BankTransaction) {

        }
    }
}