package com.gaushala

import addFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.gaushala.fragments.SplashFragment

class MainActivity : AppCompatActivity() {

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main)


        addFragment(SplashFragment(), false, true, animationType = AnimationType.fadeInfadeOut)
    }
}