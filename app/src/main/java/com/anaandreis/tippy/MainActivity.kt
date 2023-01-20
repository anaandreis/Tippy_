package com.anaandreis.tippy

import android.animation.ArgbEvaluator
import com.anaandreis.tippy.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.content.ContextCompat

// declaring the variables that will be changed by log and are dynamic
private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15
private const val INITIAL_DIVISION_PERCENT = "0"

class MainActivity : AppCompatActivity() {

// declaring the views as variables and their expected types
    private lateinit var etBaseAmount: EditText
    private lateinit var seekBarTip: SeekBar
    private lateinit var tvTipPercentLabel: TextView
    private lateinit var tvTipAmount: TextView
    private lateinit var tvTotalAmount: TextView
    private lateinit var etNumberofPeople : EditText
    private lateinit var tvPerPerson: TextView
    private lateinit var tvTipDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

 //linking the variables to which views in the xml
        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        etNumberofPeople = findViewById(R.id.etNumberofPeople)
        tvPerPerson = findViewById(R.id.tvPerPerson)
        tvTipDescription = findViewById(R.id.tvTipDescription)

    // setting the initial screen values
        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvPerPerson.text = INITIAL_DIVISION_PERCENT
        updateTipDescription(INITIAL_TIP_PERCENT)
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"

   //tracking the seekbar and showcasing it in the text bellow
        seekBarTip.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "onProgressChanged $progress")
                tvTipPercentLabel.text = "$progress%"
                computeTipandTotal()
                updateTipDescription(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })


        etBaseAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipandTotal()
            }
        })

        etNumberofPeople.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "AfterTextChanged $s")
                computeTipandTotal()
            }
        })
    }

    open fun computeTipandTotal() {


        if (etBaseAmount.text.isEmpty()) {
            tvTipAmount.text = ""
            tvTotalAmount.text = ""
            tvPerPerson.text = ""
            return
        }

        if (etNumberofPeople.text.isEmpty()){
            tvPerPerson.text = "depends"
            return
        }

        // 1. Get the value of the base and tip percent
        val baseAmount = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress

        // 2. Compute the tip and total
        val tipAmount = baseAmount * tipPercent / 100
        val totalAmount = baseAmount + tipAmount

        // 3. Update the UI
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)

        //4. get number of people
        val numberofPeople = etNumberofPeople.text.toString().toDouble()

        //5.calculate division
        val totalDivision = totalAmount / numberofPeople

        //6. change ui
        tvPerPerson.text = "%.2f".format(totalDivision)
    }

    private fun updateTipDescription(tipPercent: Int){
        val tipDescription = when (tipPercent){
            in 0..9 -> "Poor"
            in 10..14 -> "Acceptable"
            in 15..19 -> "Good"
            in 20..24 -> "Great"
            else -> "Amazing"
        }
        tvTipDescription.text = tipDescription

        //update de color based on Tip Percent
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() /seekBarTip.max,
            ContextCompat.getColor(this, R.color.color_worst_tip),
            ContextCompat.getColor(this,R.color.color_best_tip)
        ) as Int
        tvTipDescription.setTextColor(color)
    }

}


