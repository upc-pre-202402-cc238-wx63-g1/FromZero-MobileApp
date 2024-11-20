package com.cursokotlin.appfromzero

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.cursokotlin.appfromzero.UI.fragments.InitationFragment

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ocultar la barra de navegación, mantener la barra de estado visible
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // Oculta barra de navegación
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY // Permite interactuar y mantener la barra oculta
                )

        setContentView(R.layout.activity_auth)
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        val userRole = sharedPreferences.getString("userRole", null)

        if (!token.isNullOrEmpty() && !userRole.isNullOrEmpty()) {
            // El usuario ya está autenticado, redirige a MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("userRole", userRole)
            startActivity(intent)
            finish()
        } else {
            // Mostrar el flujo de autenticación
            replaceFragment(InitationFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentAuthContainer, fragment)
            .commit()
    }


}