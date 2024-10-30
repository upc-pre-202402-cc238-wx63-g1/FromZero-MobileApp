package com.cursokotlin.appfromzero.UI.fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.cursokotlin.appfromzero.MainActivity
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.common.Resource
import com.cursokotlin.appfromzero.common.UIState
import com.cursokotlin.appfromzero.data.remote.RetrofitClient
import com.cursokotlin.appfromzero.data.repository.authentication.AuthenticationRepository
import com.cursokotlin.appfromzero.models.authentication.AuthenticationRequest
import com.cursokotlin.appfromzero.models.authentication.AuthenticationResponse
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInFragment : Fragment() {

    private val authenticationRepository = AuthenticationRepository(RetrofitClient.authenticationService)
    private var uiState: UIState<AuthenticationResponse> = UIState()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_log_in, container, false)

        // Limpiar token y rol guardados al iniciar la aplicación
        clearSavedUserData()

        val llNext = rootView.findViewById<LinearLayout>(R.id.ll_Volver)
        llNext.setOnClickListener {
            replaceFragment(InitationFragment())
        }

        val tvOlvidarContra = rootView.findViewById<TextView>(R.id.tv_OlvidarContra)
        tvOlvidarContra.setOnClickListener {
            replaceFragment(RecoveryPasswordFragment())
        }

        val tvRegister = rootView.findViewById<TextView>(R.id.tv_CrearCuenta)
        tvRegister.setOnClickListener {
            replaceFragment(RolFragment())
        }

        val btLogIn = rootView.findViewById<Button>(R.id.bt_IniciarSesion)
        btLogIn.setOnClickListener {
            val username = rootView.findViewById<TextInputEditText>(R.id.user).text.toString()
            val password = rootView.findViewById<TextInputEditText>(R.id.password).text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                performLogin(username, password)
            } else {
                Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        return rootView
    }

    private fun performLogin(username: String, password: String) {
        uiState = UIState(isLoading = true)
        val request = AuthenticationRequest(username, password)
        authenticationRepository.signIn(request).enqueue(object : Callback<AuthenticationResponse?> {
            override fun onResponse(call: Call<AuthenticationResponse?>, response: Response<AuthenticationResponse?>) {
                val resource = if (response.isSuccessful) {
                    Resource.Success(response.body())
                } else {
                    Resource.Error("Login fallido")
                }
                handleLoginResponse(resource)
            }

            override fun onFailure(call: Call<AuthenticationResponse?>, t: Throwable) {
                val resource = Resource.Error<AuthenticationResponse>("Error: ${t.message}")
                handleLoginResponse(resource)
            }
        })
    }

    private fun handleLoginResponse(resource: Resource<AuthenticationResponse>) {
        uiState = UIState(isLoading = false)
        when (resource) {
            is Resource.Success -> {
                val userRoles = resource.data?.roles
                val userId = resource.data?.id ?: 0L
                val token = resource.data?.token ?: ""
                if (!userRoles.isNullOrEmpty()) {
                    val userRole = userRoles[0]
                    saveUserData(userId, token, userRole)
                    navigateToMainActivity(userRole)
                } else {
                    Toast.makeText(requireContext(), "No se encontraron roles", Toast.LENGTH_SHORT).show()
                }
            }
            is Resource.Error -> {
                Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData(userId: Long, token: String, userRole: String) {
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putLong("userId", userId)
            putString("token", token)
            putString("userRole", userRole)
            apply()
        }
    }

    private fun clearSavedUserData() {
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("userId")
            remove("token")
            remove("userRole")
            apply()
        }
    }

    private fun navigateToMainActivity(userRole: String?) {
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.putExtra("userRole", userRole)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.replace(R.id.fragmentAuthContainer, fragment)
        transaction.addToBackStack("principal")
        transaction.commit()
    }
}