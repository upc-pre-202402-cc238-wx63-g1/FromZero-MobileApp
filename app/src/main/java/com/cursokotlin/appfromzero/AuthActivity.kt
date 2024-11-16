package com.cursokotlin.appfromzero

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.cursokotlin.appfromzero.UI.fragments.InitationFragment

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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