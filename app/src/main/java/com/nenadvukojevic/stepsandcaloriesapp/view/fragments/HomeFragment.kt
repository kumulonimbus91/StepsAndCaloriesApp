package com.nenadvukojevic.stepsandcaloriesapp.view.fragments

import android.Manifest
import android.app.*
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nenadvukojevic.stepsandcaloriesapp.R
import com.nenadvukojevic.stepsandcaloriesapp.databinding.FragmentHomeBinding
import com.nenadvukojevic.stepsandcaloriesapp.model.database.FoodDatabase
import com.nenadvukojevic.stepsandcaloriesapp.utils.SharedViewModelFactory
import com.nenadvukojevic.stepsandcaloriesapp.view.activities.IntroActivity
import com.nenadvukojevic.stepsandcaloriesapp.viewmodel.SharedViewModel


@Suppress("DEPRECATION")
class HomeFragment : Fragment(), SensorEventListener {

    private lateinit var mBinding: FragmentHomeBinding

    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var notificationManager: NotificationManager

    private var sensorManager: SensorManager? = null

    private var running: Boolean = false



    private var previousTotalSteps = 0f

    private var txtSteps: String? = null

    private var txtKcal: String? = null

    companion object {
        var totalSteps = 0f

    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        mBinding.lifecycleOwner = this


        // ___ instantiate intent ___ \\
        //  Instantiate the intent declared globally - which will be passed to startService and stopService.
















        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager






        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        return mBinding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(activity).application
        val dataSource = FoodDatabase.getInstance(application).foodDatabaseDao
        val shareViewModelFactory = SharedViewModelFactory(dataSource, application)

//        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACTIVITY_RECOGNITION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            showRationalDialogForPermissions()
        } else {

            val MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 1

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION
            )


        }
        loadData()
        resetStepsManually()
















        sharedViewModel =
            ViewModelProvider(this, shareViewModelFactory).get(SharedViewModel::class.java)


        val shared: SharedPreferences? =
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)

        val weightMetric = shared?.getInt("WeightM", 0)
        val heightMetric = shared?.getInt("HeightM", 0)
        val heightUs = shared?.getInt("HeightUs", 0)
        val weightUs = shared?.getInt("WeightUs", 0)
        val age = shared?.getInt("Age", 0)
        val units = shared?.getString("UNITS", "DEFAULT")
        val gender = shared?.getString("GENDER", "DEFAULT")
        val goal = shared?.getString("GOAL", "DEFAULT")

        if (units == "METRIC") {

            txtKcal = sharedViewModel.calculateCaloriesMetric(
                gender,
                weightMetric,
                heightMetric,
                age,
                goal
            ).toString()

            txtSteps = sharedViewModel.calculateSteps(goal).toString()

            mBinding.numberOfCalories.text = txtKcal
            mBinding.numberOfSteps.text = txtSteps
            mBinding.progressBarCalories.progressMax = txtKcal!!.toFloat()
            mBinding.progressBarSteps.progressMax = txtSteps!!.toFloat()

            val value = 0

           val percentageSteps = percentageOfGoalSteps(totalSteps.toInt(), txtSteps!!.toInt())
            mBinding.stepsPercentageOfGoal.text = percentageSteps.toString() + "% of Goal"
            mBinding.tvCaloriesBurned.text = burnedCalories(totalSteps.toInt()).toString()



            sharedViewModel.foodTotal.observe(viewLifecycleOwner, {


                mBinding.progressBarCalories.setProgressWithAnimation(it.kcal.toFloat())

                mBinding.tvTotalCalories.text = it.kcal.toString()


                val percentageCalories = percentageOfGoalCalories(it.kcal, txtKcal!!.toInt())
                mBinding.caloriesPercentageOfGoal.text = percentageCalories.toString() + "% of Goal"


            })


        } else if (units == "USUNITS") {
            txtKcal =
                sharedViewModel.calculateCaloriesUs(gender, weightUs, heightUs, age, goal)
                    .toString()

            txtSteps = sharedViewModel.calculateSteps(goal).toString()



            mBinding.numberOfCalories.text = txtKcal
            mBinding.numberOfSteps.text = txtSteps
            mBinding.progressBarCalories.progressMax = txtKcal!!.toFloat()
            mBinding.progressBarSteps.progressMax = txtSteps!!.toFloat()

            val percentageSteps = percentageOfGoalSteps(totalSteps.toInt(), txtSteps!!.toInt())
            mBinding.stepsPercentageOfGoal.text = percentageSteps.toString() + "% of Goal"
            mBinding.tvCaloriesBurned.text = burnedCalories(totalSteps.toInt()).toString()




            sharedViewModel.foodTotal.observe(viewLifecycleOwner, {


                mBinding.progressBarCalories.setProgressWithAnimation(it.kcal.toFloat()) //not int when kcal

                mBinding.tvTotalCalories.text = it.kcal.toString()


                val percentageCalories = percentageOfGoalCalories(it.kcal, txtKcal!!.toInt())
                mBinding.caloriesPercentageOfGoal.text = percentageCalories.toString() + "% of Goal"


            })


        }

        mBinding.progressBarCalories.setOnClickListener {
            val fragment = BellyFragment()
            val transaction = requireFragmentManager().beginTransaction()

            transaction.replace(R.id.nav_host_fragment, fragment)
            transaction.addToBackStack(null)
            transaction.commit()

        }
        mBinding.backBtn.setOnClickListener {
            backToIntro()
        }


    }




    fun backToIntro() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.back))
        builder.setMessage(resources.getString(R.string.are_you_sure))
        builder.setIcon(R.drawable.ic_baseline_warning_24)

        builder.setPositiveButton(resources.getString(R.string.lbl_yes)) { dialogInterface, _ ->
            val intent = Intent(requireActivity(), IntroActivity::class.java)

            val shared: SharedPreferences? =
                androidx.preference.PreferenceManager.getDefaultSharedPreferences(activity?.applicationContext)

            val editor = shared?.edit()
            editor?.clear()
            editor?.apply()




            startActivity(intent)
            activity?.finish()






            dialogInterface.dismiss() // Dialog will be dismissed

        }
        //performing negative action
        builder.setNegativeButton(resources.getString(R.string.lbl_no)) { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }

        // Create the AlertDialog
        val alertDialog: androidx.appcompat.app.AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
        





    }

    override fun onResume() {
        super.onResume()
        running = true

        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        if (stepSensor == null) {
            Toast.makeText(
                requireActivity(),
                "No sensor for step counter detected on this device",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }

    }




    override fun onSensorChanged(event: SensorEvent?) {
        val sensor: Sensor = event!!.sensor

        if (sensor.type == Sensor.TYPE_STEP_DETECTOR) {
            totalSteps++
            saveData(totalSteps)
            val currentSteps = totalSteps.toInt()
            mBinding.tvTotalSteps.text = ("$currentSteps")
            mBinding.tvCaloriesBurned.text = burnedCalories(totalSteps.toInt()).toString()
            val percentageSteps = percentageOfGoalSteps(totalSteps.toInt(), txtSteps!!.toInt())
            mBinding.stepsPercentageOfGoal.text = percentageSteps.toString() + "% of Goal"

            mBinding.progressBarSteps.apply {
                setProgressWithAnimation(currentSteps.toFloat())


            }

        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
    fun resetStepsManually() {
        mBinding.numberOfSteps.setOnClickListener {
            Toast.makeText(requireActivity(), "Long press to reset steps", Toast.LENGTH_SHORT)
                .show()
        }
        mBinding.numberOfSteps.setOnLongClickListener {
            val sharedPrefs =
               activity?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            val editor = sharedPrefs?.edit()
            editor?.putString("walkingSteps", "0")
            editor?.apply()
            mBinding.tvTotalSteps.text = 0.toString()
            totalSteps = 0F
            mBinding.progressBarSteps.progress = 0F
            mBinding.tvCaloriesBurned.text = burnedCalories(totalSteps.toInt()).toString()
            mBinding.stepsPercentageOfGoal.text = "0% of Goal"




            saveData(totalSteps)

            true
        }
    }
    fun percentageOfGoalSteps(steps: Int, goal: Int): Int {
       val percentage: Int = (100 * steps) / goal


        return percentage


    }
    fun percentageOfGoalCalories(calories: Int, goal: Int): Int {
        val percentage: Int = (100 * calories) / goal


        return percentage


    }



    fun saveData(steps: Float) {
        val sharedPrefs =
            activity?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs?.edit()
        editor?.putString("walkingSteps", steps.toString())
        editor?.apply()
    }

    private fun loadData() {
        val sharedPrefs =
            activity?.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNum: String? = sharedPrefs!!.getString("walkingSteps", "0")
        Log.d("MainActivity", "$savedNum")
        previousTotalSteps = savedNum!!.toFloat()
        totalSteps = previousTotalSteps
        mBinding.tvTotalSteps.text = totalSteps.toInt().toString()



        mBinding.tvCaloriesBurned.text = burnedCalories(totalSteps.toInt()).toString()

        mBinding.progressBarSteps.apply {
            setProgressWithAnimation(totalSteps)

        }



    }


    fun burnedCalories(steps: Int) : Int {
        val burnedKcal: Double = steps * 0.045
        val result = burnedKcal.toInt()
        return result
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(requireActivity())
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", activity?.packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog,
                                           _ ->
                dialog.dismiss()
            }.show()
    }








}

