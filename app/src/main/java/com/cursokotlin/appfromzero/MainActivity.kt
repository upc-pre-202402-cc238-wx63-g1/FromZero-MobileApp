package com.cursokotlin.appfromzero

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.cursokotlin.appfromzero.UI.fragments.DeliverablesFragment
import com.cursokotlin.appfromzero.UI.fragments.HomeFragment
import com.cursokotlin.appfromzero.UI.fragments.MenuFragment
import com.cursokotlin.appfromzero.UI.fragments.MessageFragment
import com.cursokotlin.appfromzero.UI.fragments.SearchFragment
import com.cursokotlin.appfromzero.UI.fragments.SearchProjectFragment
import com.cursokotlin.appfromzero.models.HomeViewModel
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation

class MainActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Validacion del rol
        val userRole = intent.getStringExtra("userRole")
        homeViewModel.userRole.value = userRole

        val bottomNavigation = findViewById<CurvedBottomNavigation>(R.id.bottomNavigation)
        bottomNavigation.add(
            CurvedBottomNavigation.Model(1, "Home", R.drawable.ic_home)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(2, "Search", R.drawable.ic_search)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(3, "Message", R.drawable.ic_message)
        )
        bottomNavigation.add(
            CurvedBottomNavigation.Model(4, "Menu", R.drawable.ic_menu)
        )

        bottomNavigation.setOnClickMenuListener {
            when (it.id) {
                1 -> {
                    replaceFragment(HomeFragment())
                }
                //2 -> {replaceFragment(SearchProjectFragment())} //para vista devs busca de proyectos
                2 -> {
                    replaceFragment(SearchFragment())
                }

                3 -> {
                    replaceFragment(MessageFragment())
                }

                4 -> {
                    replaceFragment(MenuFragment())
                }
            }
        }

        replaceFragment(HomeFragment())
        bottomNavigation.show(1)

    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmenContainer, fragment)
            .commit()
    }
}