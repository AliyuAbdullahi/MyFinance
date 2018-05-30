package latproject.com.myfinance.views.selectbank.activities

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.widget.Toast
import com.google.gson.Gson
import latproject.com.myfinance.R
import latproject.com.myfinance.core.model.Bank
import latproject.com.myfinance.core.model.BankList
import latproject.com.myfinance.core.utils.JsonProcessorFromAsset
import latproject.com.myfinance.core.view.CoreActivity
import latproject.com.myfinance.databinding.ActivitySelectBankBinding
import latproject.com.myfinance.views.selectbank.BankListAdapter

class SelectBank : CoreActivity(), BankListAdapter.OnBankSelectedListener {

    lateinit var binding: ActivitySelectBankBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_bank)
        setSupportActionBar(binding.toolbar)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val layoutManager = GridLayoutManager(this, 2)
        binding.bankList.layoutManager = layoutManager

        val adapter = BankListAdapter()
        adapter.setOnBankSelectedListener(this)
        binding.bankList.adapter = adapter

        getBanks {
            adapter.addBanks(it)
        }
    }

    private fun getBanks(onBanksObtained: (banks: List<Bank>)->Unit) {
        val jasonProcessorFromAsset = JsonProcessorFromAsset()
        val json = jasonProcessorFromAsset.loadJSONFromAsset(this, "banks.json")

        val gson = Gson()
        val banks = gson.fromJson<BankList>(json, BankList::class.java)
        if(banks != null) {
            onBanksObtained(banks.banks!!)
        }
    }

    override fun onBankSelected(bank: Bank) {
        Toast.makeText(this@SelectBank, bank.name, Toast.LENGTH_SHORT).show()
    }
}
