package com.cursokotlin.appfromzero

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.cursokotlin.appfromzero.UI.fragments.*
import com.cursokotlin.appfromzero.models.HomeViewModel
import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation

class MainActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var bottomNavigation: CurvedBottomNavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Validación del rol
        val userRole = intent.getStringExtra("userRole")
        homeViewModel.userRole.value = userRole

        // Inicialización común de componentes
        initializeComponents()

        // Configuración del bottomNavigation según el rol
        when (userRole) {
            "empresa" -> setupBottomNavigationForEmpresa()
            "desarrollador" -> setupBottomNavigationForDeveloper()
        }

        // Establecer fragmento inicial
        replaceFragment(HomeFragment())
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
                1 -> replaceFragment(HomeFragment())
                2 -> replaceFragment(SearchProjectFragment()) // Vista de proyectos para desarrolladores
                3 -> replaceFragment(MessageFragment())
                4 -> replaceFragment(MenuFragment())
            }
        }
    }

    private fun setupBottomNavigationForEmpresa() {
        bottomNavigation.setOnClickMenuListener {
            when (it.id) {
                1 -> replaceFragment(HomeFragment())
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
