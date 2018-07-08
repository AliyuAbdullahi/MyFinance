package latproject.com.myfinance.views.money_spend_overview.activities

import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import latproject.com.myfinance.R
import latproject.com.myfinance.core.globals.clear
import latproject.com.myfinance.core.view.CoreActivity
import latproject.com.myfinance.databinding.ActivityMoneySpendingStatBinding
import latproject.com.myfinance.views.money_spend_overview.viewmodels.MoneySpentOverviewViewModel

class MoneySpendingStatActivity : CoreActivity() {
    lateinit var binding: ActivityMoneySpendingStatBinding
    lateinit var viewModel: MoneySpentOverviewViewModel
    var numberOfDays: Int = 30
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_money_spending_stat)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_money_spending_stat)
        viewModel = MoneySpentOverviewViewModel(this)
        setSupportActionBar(binding.toolbar)
        bindStatusBar()
        binding.numberOfDaysInput.addTextChangedListener(NumberOfDaysWatcher())
        initChart()
    }

    private fun bindStatusBar() {
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setDisplayShowTitleEnabled(false)
        }
    }

    fun getTypeface() = Typeface.createFromAsset(assets, "fonts/SofiaPro_Light.otf")

    private fun initChart() {
        binding.transactionChart.setUsePercentValues(true)
        binding.transactionChart.description.isEnabled = false
        binding.transactionChart.setExtraOffsets(5F, 10F, 5F, 5F)
        binding.transactionChart.dragDecelerationFrictionCoef = 0.95F
        binding.transactionChart.setCenterTextTypeface(getTypeface())
        binding.transactionChart.centerText = "Expenditure Summary"
        binding.transactionChart.isDrawHoleEnabled = true
        binding.transactionChart.setHoleColor(Color.WHITE)
        binding.transactionChart.setTransparentCircleColor(Color.WHITE)
        binding.transactionChart.setTransparentCircleAlpha(110)
        binding.transactionChart.holeRadius = 58f
        binding.transactionChart.transparentCircleRadius = 61f
        binding.transactionChart.setDrawCenterText(true)
        binding.transactionChart.isRotationEnabled = true
        binding.transactionChart.isHighlightPerTapEnabled = true
//        binding.transactionChart.setOnChartValueSelectedListener(this)

        bindChatEntries()
    }

    override fun onStart() {
        super.onStart()
        setStatusBarColor(R.color.white)
    }

    fun bindChatEntries() {
        val transactions = viewModel.getTransactionsFrom(numberOfDays)
        if (transactions != null && transactions.isNotEmpty()) {
            val dataEntries = viewModel.getPieChatData(transactions)
            val pieEntries = ArrayList<PieEntry>()

            dataEntries.forEach {
                if (it.amount > 0) {
                    pieEntries.add(PieEntry(it.amount.toFloat(), it.name))
                }
            }

            val pieDataSet = PieDataSet(pieEntries, "Money Spent Destination")
            pieDataSet.setDrawIcons(false)
            pieDataSet.sliceSpace = 3f
            pieDataSet.iconsOffset = MPPointF(0f, 40f)
            pieDataSet.selectionShift = 5f

            val colors = ArrayList<Int>()

//            for (c in ColorTemplate.VORDIPLOM_COLORS)
//                colors.add(c)

            for (c in ColorTemplate.JOYFUL_COLORS)
                colors.add(c)

            for (c in ColorTemplate.COLORFUL_COLORS)
                colors.add(c)

            for (c in ColorTemplate.LIBERTY_COLORS)
                colors.add(c)

            for (c in ColorTemplate.PASTEL_COLORS)
                colors.add(c)

            colors.add(ColorTemplate.getHoloBlue())

            pieDataSet.colors = colors

            val data = PieData(pieDataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(10f)
            data.setValueTextColor(Color.WHITE)
            data.setValueTypeface(getTypeface())
            binding.transactionChart.data = data
            binding.transactionChart.setDrawEntryLabels(false)
            binding.transactionChart.legend.orientation = Legend.LegendOrientation.VERTICAL
            binding.transactionChart.extraBottomOffset = 20f
            binding.transactionChart.animateY(1400, Easing.EasingOption.EaseInQuad)
            binding.transactionChart.spin(2000, 0F, 360F, Easing.EasingOption.EaseInQuad)
            binding.transactionChart.invalidate()
        }
    }

    inner class NumberOfDaysWatcher : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(input: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (input.isNullOrEmpty()) {
                numberOfDays = 30
            } else {

                if (input!!.length >= 2) {
                    var days = input.toString().toInt()
                    if (days < 15) {
                        binding.numberOfDaysInput.error = "Minimum of 15 days interval is expected"
                        binding.numberOfDaysInput.clear()
                    } else {
                        numberOfDays = binding.numberOfDaysInput.text.toString().toInt()
                        bindChatEntries()
                    }
                } else if (input.length >= 2) {
                    numberOfDays = binding.numberOfDaysInput.text.toString().toInt()
                    bindChatEntries()
                }
            }
        }
    }
}
