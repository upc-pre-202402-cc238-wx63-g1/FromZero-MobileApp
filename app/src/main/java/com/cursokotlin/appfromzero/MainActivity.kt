package com.cursokotlin.appfromzero

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cursokotlin.appfromzero.UI.fragments.*
import com.cursokotlin.appfromzero.common.UIState
import com.cursokotlin.appfromzero.models.HomeViewModel
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation

class MainActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var bottomNavigation: CurvedBottomNavigation
    private var uiState: UIState<String> = UIState()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val userId = sharedPreferences.getLong("userId", 0L)
        val token = sharedPreferences.getString("token", "") ?: ""
        val userRole = sharedPreferences.getString("userRole", "ROLE_DESCONOCIDO")

        homeViewModel.userRole.value = userRole

        Toast.makeText(this, "Rol de usuario: $userRole", Toast.LENGTH_SHORT).show()
        Log.d("MainActivity", "Rol de usuario recuperado de SharedPreferences: $userRole")
        Log.d("MainActivity", "ID de usuario recuperado de SharedPreferences: $userId")
        Log.d("MainActivity", "Token de usuario recuperado de SharedPreferences: $token")

        initializeComponents()

        when (userRole) {
            "ROLE_ENTERPRISE" -> {
                setupBottomNavigationForEnterprise()
                replaceFragment(HomeEnterpriseFragment())
            }
            "ROLE_DEVELOPER" -> {
                setupBottomNavigationForDeveloper()
                replaceFragment(HomeDeveloperFragment())
            }
            else -> Log.w("MainActivity", "Rol de usuario desconocido: $userRole")
        }
        bottomNavigation.show(1)
    }

    private fun initializeComponents() {
        bottomNavigation = findViewById(R.id.bottomNavigation)

        bottomNavigation.add(CurvedBottomNavigation.Model(1, "Home", R.drawable.ic_home))
        bottomNavigation.add(CurvedBottomNavigation.Model(2, "Search", R.drawable.ic_search))
        bottomNavigation.add(CurvedBottomNavigation.Model(3, "Message", R.drawable.ic_message))
        bottomNavigation.add(CurvedBottomNavigation.Model(4, "Menu", R.drawable.ic_menu))
    }

    private fun setupBottomNavigationForDeveloper() {
        bottomNavigation.setOnClickMenuListener {
            when (it.id) {
                1 -> replaceFragment(HomeDeveloperFragment())
                2 -> replaceFragment(SearchProjectFragment()) // Vista de proyectos para desarrolladores
                3 -> replaceFragment(MessageFragment())
                4 -> replaceFragment(MenuFragment())
            }
        }
    }

    private fun setupBottomNavigationForEnterprise() {
        bottomNavigation.setOnClickMenuListener {
            when (it.id) {
                1 -> replaceFragment(HomeEnterpriseFragment())
                2 -> replaceFragment(SearchFragment()) // Vista de búsqueda para empresas
                3 -> replaceFragment(MessageFragment())
                4 -> replaceFragment(MenuFragment())
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmenContainer, fragment)
            .commit()
    }
}