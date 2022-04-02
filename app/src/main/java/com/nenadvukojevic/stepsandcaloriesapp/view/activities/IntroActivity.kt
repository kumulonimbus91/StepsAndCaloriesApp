package com.nenadvukojevic.stepsandcaloriesapp.view.activities

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.nenadvukojevic.stepsandcaloriesapp.R
import com.nenadvukojevic.stepsandcaloriesapp.databinding.ActivityIntroBinding
import com.nenadvukojevic.stepsandcaloriesapp.view.fragments.BMIFragment
import com.nenadvukojevic.stepsandcaloriesapp.view.fragments.HomeFragment
import kotlin.properties.Delegates

class IntroActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityIntroBinding

    private lateinit var sharedPrefs: SharedPreferences




    var currentVisibleView: String =
        METRIC_UNITS_VIEW // A variable to hold a value to make visible a selected view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_intro)







        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        makeVisibleMetricUnitsView()

        var weightValueMetric: Int
        var heightValueMetric: Int
        var age: Int
        var heightValueUs: Int
        var weightValueUs: Int

        val rgGroupGender = mBinding.rgGender
        val rgGroupGoal = mBinding.rgGoal
        val rgUnits = mBinding.rgUnits



        var extraGender: String
        var extraGoal: String

        var extraUnits: String


        mBinding.rgUnits.setOnCheckedChangeListener { _: RadioGroup, checkedId: Int ->
            if (checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricUnitsView()

            } else {
                makeVisibleUsUnitsView()
            }


        }
        mBinding.btnSubmit.setOnClickListener {

            val intent = Intent(this@IntroActivity, MainActivity::class.java)

            val shared: SharedPreferences? =
                androidx.preference.PreferenceManager.getDefaultSharedPreferences(applicationContext)
            val sharedEditor: SharedPreferences.Editor? = shared!!.edit()


            sharedPrefs = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPrefs.edit()
            editor.putString("FirstLaunch", "No")
            editor.apply()

            when (rgGroupGender.checkedRadioButtonId) {

                mBinding.rbMale.id -> {
                    extraGender = "Male"
                    sharedEditor?.putString("GENDER", extraGender)


                }
                mBinding.rbFemale.id -> {
                    extraGender = "Female"
                    sharedEditor?.putString("GENDER", extraGender)


                }

            }

            when (rgGroupGoal.checkedRadioButtonId) {
                mBinding.rbLose.id -> {
                    extraGoal = "LOSE"
                    sharedEditor?.putString("GOAL", extraGoal)


                }
                mBinding.rbLoseHeavy.id -> {
                    extraGoal = "LOSEHEAVY"
                    sharedEditor?.putString("GOAL", extraGoal)


                }
                mBinding.rbMaintain.id -> {
                    extraGoal = "MAINTAIN"
                    sharedEditor?.putString("GOAL", extraGoal)


                }


            }


            when (rgUnits.checkedRadioButtonId) {
                mBinding.rbMetricUnits.id -> {
                    extraUnits = "METRIC"
                    sharedEditor?.putString("UNITS", extraUnits)


                }
                mBinding.rbUsUnits.id -> {
                    extraUnits = "USUNITS"
                    sharedEditor?.putString("UNITS", extraUnits)


                }
            }



            if (currentVisibleView == METRIC_UNITS_VIEW) {
                if (validateMetricUnits()) {

                    heightValueMetric = mBinding.etMetricUnitHeight.text.toString().toInt()

                    sharedEditor?.putInt("HeightM", heightValueMetric)




                    weightValueMetric = mBinding.etMetricUnitWeight.text.toString().toInt()

                    sharedEditor?.putInt("WeightM", weightValueMetric)




                    age = mBinding.etAge.text.toString().toInt()

                    sharedEditor?.putInt("Age", age)

                    if (checkGender() && checkGoal()) {
                        sharedEditor?.apply()
                        startActivity(intent)
                        finish()

                    } else {
                        Toast.makeText(
                            this@IntroActivity,
                            "Please enter valid values.",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    }


                } else {
                    Toast.makeText(
                        this@IntroActivity,
                        "Please enter valid values.",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            } else if (currentVisibleView == US_UNITS_VIEW) {
                if (validateUsUnits()) {
                    val usUnitHeightValueFeet: Int =
                        mBinding.etUsUnitHeightFeet.text.toString()
                            .toInt() // Height Feet value entered in EditText component.
                    val usUnitHeightValueInch: Int =
                        mBinding.etUsUnitHeightInch.text.toString()
                            .toInt() // Height Inch value entered in EditText component.


                    weightValueUs = mBinding.etUsUnitWeight.text.toString()
                        .toInt() // Weight value entered in EditText component.

                    sharedEditor?.putInt("WeightUs", weightValueUs)


                    // Here the Height Feet and Inch values are merged and multiplied by 12 for converting it to inches.
                    heightValueUs =
                        usUnitHeightValueInch + usUnitHeightValueFeet * 12

                    sharedEditor?.putInt("HeightUs", heightValueUs)


                    age = mBinding.etAge.text.toString().toInt()

                    sharedEditor?.putInt("Age", age)

                    if (checkGender() && checkGoal()) {
                        sharedEditor?.apply()
                        startActivity(intent)
                        finish()


                    }


                } else {
                    Toast.makeText(
                        this@IntroActivity,
                        "Please enter valid values.",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                }
            }










        }






    }



    private fun validateMetricUnits(): Boolean {
        var isValid = true

        if (mBinding.etMetricUnitWeight.text.toString().isEmpty()) {
            isValid = false
        } else if (mBinding.etMetricUnitHeight.text.toString().isEmpty()) {
            isValid = false
        } else if (mBinding.etAge.text.toString().isEmpty()) {
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
        } else if (mBinding.etAge.text.toString().isEmpty()) {
            isValid = false
        }

        return isValid
    }

    private fun checkGender(): Boolean {
        val id: Int = mBinding.rgGender.checkedRadioButtonId

        return id == R.id.rb_male || id == R.id.rb_female

    }
    private fun checkGoal(): Boolean {
        val id: Int = mBinding.rgGoal.checkedRadioButtonId
        return id == R.id.rb_lose || id == R.id.rb_lose_heavy || id == R.id.rb_maintain

    }

    private fun makeVisibleUsUnitsView() {
        currentVisibleView = US_UNITS_VIEW // Current View is updated here.
        mBinding.llMetricUnitsView.visibility = View.GONE // METRIC UNITS VIEW is hidden
        mBinding.llUsUnitsView.visibility = View.VISIBLE // US UNITS VIEW is Visible

        mBinding.etUsUnitWeight.text!!.clear() // weight value is cleared if it is added.
        mBinding.etUsUnitHeightFeet.text!!.clear() // height feet value is cleared if it is added.
        mBinding.etUsUnitHeightInch.text!!.clear() // height inch is cleared if it is added.


    }

    private fun makeVisibleMetricUnitsView() {
        currentVisibleView = METRIC_UNITS_VIEW // Current View is updated here.
        mBinding.llMetricUnitsView.visibility = View.VISIBLE // METRIC UNITS VIEW is Visible
        mBinding.llUsUnitsView.visibility = View.GONE // US UNITS VIEW is hidden

        mBinding.etMetricUnitHeight.text!!.clear() // height value is cleared if it is added.
        mBinding.etMetricUnitWeight.text!!.clear() // weight value is cleared if it is added.


    }

    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW" // Metric Unit View
        private const val US_UNITS_VIEW = "US_UNIT_VIEW" // US Unit View


    }











}