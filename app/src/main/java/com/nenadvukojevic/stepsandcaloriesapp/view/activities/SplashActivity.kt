package com.nenadvukojevic.stepsandcaloriesapp.view.activities

import android.content.Context
import android.content.Intent
import androidx.databinding.DataBindingUtil
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.nenadvukojevic.stepsandcaloriesapp.R
import com.nenadvukojevic.stepsandcaloriesapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var splashBinding: ActivitySplashBinding

    private lateinit var sharedPrefs: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        splashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        val splashAnimation = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.anim)
        splashBinding.tvAppName.animation = splashAnimation


        splashAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                Handler(Looper.getMainLooper()).postDelayed({


                    sharedPrefs = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
//                    val isFirstRun: SharedPreferences = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    val firstTime: String? = sharedPrefs.getString("FirstLaunch", "Yes")
                    val shared: SharedPreferences? =
                        androidx.preference.PreferenceManager.getDefaultSharedPreferences(
                            applicationContext
                        )

                    val genderDefault: String = "default"

                    val gender: String? = shared?.getString("GENDER", "")

                    if (shared?.getString("GENDER", null) == null) {
                        //                        val editor: SharedPreferences.Editor = sharedPrefs.edit()
//                        editor.putString("FirstLaunch", "No")
//                        editor.apply()
                        val intent = Intent(this@SplashActivity, IntroActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()


                    }


                }, 1000)
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })


    }


}
