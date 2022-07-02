package com.nenadvukojevic.stepsandcaloriesapp.view.fragments

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.nenadvukojevic.stepsandcaloriesapp.R
import com.nenadvukojevic.stepsandcaloriesapp.adapters.FoodAdapter
import com.nenadvukojevic.stepsandcaloriesapp.databinding.FragmentCaloriesBinding
import com.nenadvukojevic.stepsandcaloriesapp.model.entities.Food
import com.nenadvukojevic.stepsandcaloriesapp.view.activities.MainActivity
import com.nenadvukojevic.stepsandcaloriesapp.view.activities.SplashActivity
import com.nenadvukojevic.stepsandcaloriesapp.viewmodel.CaloriesViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.lang.Exception
import java.lang.RuntimeException


class CaloriesFragment : Fragment() {


    private lateinit var binding: FragmentCaloriesBinding

    private val MainActivity = com.nenadvukojevic.stepsandcaloriesapp.view.activities.MainActivity()

    private var mInterstitialAd: InterstitialAd? = null


    private val caloriesViewModel: CaloriesViewModel by lazy {
        ViewModelProvider(this).get(CaloriesViewModel::class.java)
    }

    val bundle: Bundle? = null


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_calories, container, false)


        // Giving the binding access to the viewModel
        binding.viewModel = caloriesViewModel

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this


        val pb: ProgressBar = binding.searchProgressbar

        MobileAds.initialize(requireActivity())

        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)


        InterstitialAd.load(
            requireActivity(),
            "ca-app-pub-4256849898367595/9826413857",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    Log.d(TAG, p0?.message)
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    Log.d(TAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                }


            })

        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                Log.d(TAG, "Ad was dismissed.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d(TAG, "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.")
                mInterstitialAd = null;
            }
        }










        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FoodAdapter(this@CaloriesFragment, FoodAdapter.OnClickListener {
            caloriesViewModel.displayAddFood(it)
        })


        binding.searchRecyclerview.adapter = adapter



        caloriesViewModel.searchListFood.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })


        // Observe navigateToSelectedFood data and navigate if it isn't null
        // After navigate, set the selectedFood to null so that ViewModel is rdy for another navigation


        caloriesViewModel.navigateToSelectedFood.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController()
                    .navigate(CaloriesFragmentDirections.actionCaloriesFragmentToAddFoodFr(it))
                // tell the viewModel we've made the navigate call
                caloriesViewModel.displayAddFoodIsComplete()
                showFullAd()
            }
        })


        // ProgressBar's Visibility
        caloriesViewModel.searchInProgress.observe(viewLifecycleOwner, Observer {
            if (it == false) {
                binding.searchProgressbar.visibility = View.INVISIBLE
            } else {
                binding.searchProgressbar.visibility = View.VISIBLE
            }
        })

        // Food Not Found TV visibility
        caloriesViewModel.showFoodNotFound.observe(viewLifecycleOwner, Observer {
            if (it == false) {
                binding.searchRecyclerview.visibility = View.VISIBLE
                binding.tvNotFound.visibility = View.INVISIBLE
            } else {
                binding.searchRecyclerview.visibility = View.INVISIBLE
                binding.tvNotFound.visibility = View.VISIBLE
            }
        })


    }

    fun showFullAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(requireActivity())
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }


}
