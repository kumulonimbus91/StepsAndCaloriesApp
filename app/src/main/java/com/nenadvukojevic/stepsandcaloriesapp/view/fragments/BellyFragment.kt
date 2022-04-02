package com.nenadvukojevic.stepsandcaloriesapp.view.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.nenadvukojevic.stepsandcaloriesapp.R
import com.nenadvukojevic.stepsandcaloriesapp.adapters.BellyAdapter
import com.nenadvukojevic.stepsandcaloriesapp.databinding.FragmentBellyBinding
import com.nenadvukojevic.stepsandcaloriesapp.model.database.FoodDatabase
import com.nenadvukojevic.stepsandcaloriesapp.utils.SharedViewModelFactory
import com.nenadvukojevic.stepsandcaloriesapp.viewmodel.SharedViewModel


class BellyFragment : Fragment() {

    private lateinit var binding: FragmentBellyBinding
    private lateinit var sharedViewModel: SharedViewModel







    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate<FragmentBellyBinding>(inflater, R.layout.fragment_belly, container, false)
        binding.lifecycleOwner = this






        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val application = requireNotNull(activity).application
        val dataSource = FoodDatabase.getInstance(application).foodDatabaseDao


        val sharedViewModelFactory = SharedViewModelFactory(dataSource, application)


        sharedViewModel = ViewModelProvider(this, sharedViewModelFactory).get(SharedViewModel::class.java)









        val adapter = BellyAdapter(BellyAdapter.OnBtnDeleteListener {
           sharedViewModel.onDeleteChoosedFood(it)
        })

        sharedViewModel.foodTotal.observe(viewLifecycleOwner,{

            val percent: String = "%"

            binding.tvTotalKcal.setText(it.kcal.toString())
            binding.tvCurrentCarbs.setText(it.carbsPercent.toString() + percent)
            binding.tvCurrentProteins.setText(it.proteinPercent.toString() + "%")
            binding.tvCurrentFats.setText(it.fatPercent.toString() + "%")





        })

        binding.rvBelly.adapter = adapter

       sharedViewModel.foods.observe(viewLifecycleOwner, {
            it?.let {
                adapter.data = it


            }
        })
    }






}