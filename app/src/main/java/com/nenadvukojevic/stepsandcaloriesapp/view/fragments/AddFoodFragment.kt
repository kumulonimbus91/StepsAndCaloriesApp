package com.nenadvukojevic.stepsandcaloriesapp.view.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import androidx.navigation.fragment.findNavController
import com.nenadvukojevic.stepsandcaloriesapp.model.database.FoodDatabase
import com.nenadvukojevic.stepsandcaloriesapp.R
import com.nenadvukojevic.stepsandcaloriesapp.databinding.FragmentAddFoodBinding
import com.nenadvukojevic.stepsandcaloriesapp.utils.AddFoodViewModelFactory
import com.nenadvukojevic.stepsandcaloriesapp.view.activities.MainActivity
import com.nenadvukojevic.stepsandcaloriesapp.viewmodel.AddFoodViewModel
import com.nenadvukojevic.stepsandcaloriesapp.viewmodel.CaloriesViewModel


class AddFoodFragment : Fragment() {

    private lateinit var binding: FragmentAddFoodBinding


    val bundle = Bundle()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_food,
            container,
            false
        )
        binding.lifecycleOwner = this

        //binding.llyChart.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))






        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val application = requireNotNull(activity).application
        val food = AddFoodFragmentArgs.fromBundle(requireArguments()).selectedFood
        val dataSource = FoodDatabase.getInstance(application).foodDatabaseDao

        val viewModelFactory = AddFoodViewModelFactory(food, dataSource, application)

        val viewModel =  ViewModelProvider(this, viewModelFactory).get(AddFoodViewModel::class.java)


        binding.viewModel = viewModel











        
    }




}