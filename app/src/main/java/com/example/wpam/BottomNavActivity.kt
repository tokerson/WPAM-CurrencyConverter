package com.example.wpam

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.wpam.ui.SettingsFragment
import com.example.wpam.ui.CameraFragment

class BottomNavActivity : AppCompatActivity() {

    private val settingsFragment = SettingsFragment()
    private val photoFragment = CameraFragment()
    private val fm = supportFragmentManager
    private var selectedFragment: Fragment = photoFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(navListener)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration(
            setOf(
                R.id.navigation_photo, R.id.navigation_settings
            )
        )

        fm.beginTransaction().add(R.id.nav_host_fragment, settingsFragment, "2").hide(settingsFragment)
            .commit()
        fm.beginTransaction().add(R.id.nav_host_fragment, photoFragment, "1").commit()
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.navigation_photo -> {
                fm.beginTransaction().hide(selectedFragment).show(photoFragment).commit()
                selectedFragment = photoFragment
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                fm.beginTransaction().hide(selectedFragment).show(settingsFragment).commit()
                selectedFragment = settingsFragment
                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }
}
