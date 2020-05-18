package com.example.wpam

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wpam.ui.MainFragment
import com.example.wpam.ui.PhotoActivity

class BottomNavActivity : AppCompatActivity() {

    private val mainFragment = MainFragment()
    private val photoFragment = PhotoActivity()
    private val fm = supportFragmentManager
    private var selectedFragment: Fragment = mainFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(navListener)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_notifications
            )
        )

        fm.beginTransaction().add(R.id.nav_host_fragment, photoFragment, "2").hide(photoFragment)
            .commit()
        fm.beginTransaction().add(R.id.nav_host_fragment, mainFragment, "1").commit()
    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.navigation_home -> {
                fm.beginTransaction().hide(selectedFragment).show(mainFragment).commit()
                selectedFragment = mainFragment
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                fm.beginTransaction().hide(selectedFragment).show(photoFragment).commit()
                selectedFragment = photoFragment
                return@OnNavigationItemSelectedListener true
            }
        }

        false
    }
}
