package latproject.com.myfinance.views.selectbank

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import latproject.com.myfinance.R
import latproject.com.myfinance.core.model.Bank
import latproject.com.myfinance.databinding.LayoutBankCardBinding

class BankListAdapter : RecyclerView.Adapter<BankListAdapter.BankListViewHolder>() {
    val banks: MutableList<Bank> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_bank_card, parent, false)

        return BankListViewHolder(view)
    }

    fun addBanks(bankList: List<Bank>) {
        banks.addAll(bankList)
        notifyDataSetChanged()
    }

    private var onBankSelectedListener: OnBankSelectedListener? = null

    fun setOnBankSelectedListener(onBankSelectedListener: OnBankSelectedListener) {
        this.onBankSelectedListener = onBankSelectedListener
    }

    override fun getItemCount(): Int {
        return banks.count()
    }

    override fun onBindViewHolder(holder: BankListViewHolder, position: Int) {
        holder.onBindTo(banks[position])
    }

    inner class BankListViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        var bank: Bank? = null
        var binding: LayoutBankCardBinding

        override fun onClick(p0: View?) {
            if (onBankSelectedListener != null && bank != null) {
                onBankSelectedListener!!.onBankSelected(bank!!)
            }
        }

        init {
            binding = LayoutBankCardBinding.bind(view)
            binding.root.setOnClickListener(this)
            binding.bankLog.setOnClickListener(this)
            binding.bankName.setOnClickListener(this)
        }

        fun onBindTo(bank: Bank) {
            this.bank = bank
            binding.bankName.text = bank.name
            BankParser.parse(bank, binding.bankLog)
            binding.root.setBackgroundColor(Color.parseColor(bank.backgroundColor))
        }
    }

    interface OnBankSelectedListener {
        fun onBankSelected(bank: Bank)
    }
}