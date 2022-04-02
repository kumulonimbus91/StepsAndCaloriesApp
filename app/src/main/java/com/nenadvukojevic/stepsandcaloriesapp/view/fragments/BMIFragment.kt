package com.nenadvukojevic.stepsandcaloriesapp.view.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.nenadvukojevic.stepsandcaloriesapp.R
import com.nenadvukojevic.stepsandcaloriesapp.databinding.FragmentBmiBinding
import com.nenadvukojevic.stepsandcaloriesapp.viewmodel.BMIViewModel
import java.math.BigDecimal
import java.math.RoundingMode

class BMIFragment : Fragment() {

    private var currentVisibleView: String =
        METRIC_UNITS_VIEW // A variable to hold a value to make visible a selected view

    private lateinit var mBinding: FragmentBmiBinding

    private lateinit var notificationsViewModel: BMIViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bmi, container, false)

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        return mBinding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        makeVisibleMetricUnitsView()

        mBinding.rgUnits.setOnCheckedChangeListener { _: RadioGroup, checkedId: Int ->
            if (checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricUnitsView()

            } else {
                makeVisibleUsUnitsView()
            }


        }

        mBinding.btnCalculateUnits.setOnClickListener {
            if (currentVisibleView == METRIC_UNITS_VIEW) {
                if (validateMetricUnits()) {

                    // The height value in converted to float value and divided by 100 to convert it to meter.
                    val heightValue: Float =
                        mBinding.etMetricUnitHeight.text.toString().toFloat() / 100

                    // The weight value in converted to float value
                    val weightValue: Float = mBinding.etMetricUnitWeight.text.toString().toFloat()

                    // BMI value is calculated in METRIC UNITS using the height and weight value.
                    val bmi = weightValue / (heightValue * heightValue)

                    displayBMIResult(bmi)
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Please enter valid values.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }


            } else {
                // The values are validated.
                if (validateUsUnits()) {

                    val usUnitHeightValueFeet: String =
                        mBinding.etUsUnitHeightFeet.text.toString() // Height Feet value entered in EditText component.
                    val usUnitHeightValueInch: String =
                        mBinding.etUsUnitHeightInch.text.toString() // Height Inch value entered in EditText component.
                    val usUnitWeightValue: Float = mBinding.etUsUnitWeight.text.toString()
                        .toFloat() // Weight value entered in EditText component.

                    // Here the Height Feet and Inch values are merged and multiplied by 12 for converting it to inches.
                    val heightValue =
                        usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                    // This is the Formula for US UNITS result.
                    // Reference Link : https://www.cdc.gov/healthyweight/assessing/bmi/childrens_bmi/childrens_bmi_formula.html
                    val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))

                    displayBMIResult(bmi) // Displaying the result into UI
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Please enter valid values.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            }

        }


    }

    private fun makeVisibleMetricUnitsView() {
        currentVisibleView = METRIC_UNITS_VIEW // Current View is updated here.
        mBinding.llMetricUnitsView.visibility = View.VISIBLE // METRIC UNITS VIEW is Visible
        mBinding.llUsUnitsView.visibility = View.GONE // US UNITS VIEW is hidden


        mBinding.etMetricUnitHeight.text!!.clear() // height value is cleared if it is added.
        mBinding.etMetricUnitWeight.text!!.clear() // weight value is cleared if it is added.

        mBinding.tvYourBMI.visibility =
            View.INVISIBLE // Result is cleared and the labels are hidden
        mBinding.tvBMIValue.visibility =
            View.INVISIBLE // Result is cleared and the labels are hidden
        mBinding.tvBMIType.visibility =
            View.INVISIBLE // Result is cleared and the labels are hidden
        mBinding.tvBMIDescription.visibility =
            View.INVISIBLE // Result is cleared and the labels are hidden
    }

    private fun makeVisibleUsUnitsView() {
        currentVisibleView = US_UNITS_VIEW // Current View is updated here.
        mBinding.llMetricUnitsView.visibility = View.GONE // METRIC UNITS VIEW is hidden
        mBinding.llUsUnitsView.visibility = View.VISIBLE // US UNITS VIEW is Visible

        mBinding.etUsUnitWeight.text!!.clear() // weight value is cleared if it is added.
        mBinding.etUsUnitHeightFeet.text!!.clear() // height feet value is cleared if it is added.
        mBinding.etUsUnitHeightInch.text!!.clear() // height inch is cleared if it is added.

        mBinding.tvYourBMI.visibility =
            View.INVISIBLE // Result is cleared and the labels are hidden
        mBinding.tvBMIValue.visibility =
            View.INVISIBLE // Result is cleared and the labels are hidden
        mBinding.tvBMIType.visibility =
            View.INVISIBLE // Result is cleared and the labels are hidden
        mBinding.tvBMIDescription.visibility =
            View.INVISIBLE // Result is cleared and the labels are hidden
    }

    private fun validateMetricUnits(): Boolean {
        var isValid = true

        if (mBinding.etMetricUnitWeight.text.toString().isEmpty()) {
            isValid = false
        } else if (mBinding.etMetricUnitHeight.text.toString().isEmpty()) {
            isValid = false
        }

        return isValid
    }

    private fun validateUsUnits(): Boolean {
        var isValid = true

        if (mBinding.etUsUnitWeight.text.toString().isEmpty()) {
            isValid = false
        } else if (mBinding.etUsUnitHeightFeet.text.toString().isEmpty()) {
            isValid = false
        } else if (mBinding.etUsUnitHeightInch.text.toString().isEmpty()) {
            isValid = false
        }

        return isValid
    }

    private fun displayBMIResult(bmi: Float) {

        val bmiLabel: String
        val bmiDescription: String

        if (java.lang.Float.compare(bmi, 15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 15f) > 0 && java.lang.Float.compare(
                bmi,
                16f
            ) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 16f) > 0 && java.lang.Float.compare(
                bmi,
                18.5f
            ) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 18.5f) > 0 && java.lang.Float.compare(
                bmi,
                25f
            ) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                bmi,
                30f
            ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (java.lang.Float.compare(bmi, 30f) > 0 && java.lang.Float.compare(
                bmi,
                35f
            ) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (java.lang.Float.compare(bmi, 35f) > 0 && java.lang.Float.compare(
                bmi,
                40f
            ) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        mBinding.tvYourBMI.visibility = View.VISIBLE
        mBinding.tvBMIValue.visibility = View.VISIBLE
        mBinding.tvBMIType.visibility = View.VISIBLE
        mBinding.tvBMIDescription.visibility = View.VISIBLE

        // This is used to round of the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        mBinding.tvBMIValue.text = bmiValue // Value is set to TextView
        mBinding.tvBMIType.text = bmiLabel // Label is set to TextView
        mBinding.tvBMIDescription.text = bmiDescription // Description is set to TextView
    }


    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW" // Metric Unit View
        private const val US_UNITS_VIEW = "US_UNIT_VIEW" // US Unit View
    }
}