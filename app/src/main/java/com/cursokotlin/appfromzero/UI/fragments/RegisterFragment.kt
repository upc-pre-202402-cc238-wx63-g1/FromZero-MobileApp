package com.cursokotlin.appfromzero.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.data.remote.RetrofitClient
import com.cursokotlin.appfromzero.data.repository.authentication.AuthenticationRepository
import com.cursokotlin.appfromzero.models.authentication.DeveloperRegisterRequest
import com.cursokotlin.appfromzero.models.authentication.EnterpriseRegisterRequest
import com.cursokotlin.appfromzero.models.authentication.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {

    private val authenticationRepository = AuthenticationRepository(RetrofitClient.authenticationService)
    private var userRole: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_register, container, false)

        userRole = arguments?.getString("userRole")

        val etFirstNameLayout = rootView.findViewById<View>(R.id.etFirstNameLayout)
        val etLastNameLayout = rootView.findViewById<View>(R.id.etLastNameLayout)
        val etEnterpriseNameLayout = rootView.findViewById<View>(R.id.etEnterpriseNameLayout)

        // Mostrar/ocultar campos según el rol del usuario
        if (userRole == "ROLE_DEVELOPER") {
            etFirstNameLayout.visibility = View.VISIBLE
            etLastNameLayout.visibility = View.VISIBLE
            etEnterpriseNameLayout.visibility = View.GONE
        } else if (userRole == "ROLE_ENTERPRISE") {
            etFirstNameLayout.visibility = View.GONE
            etLastNameLayout.visibility = View.GONE
            etEnterpriseNameLayout.visibility = View.VISIBLE
        }

        val btRegister = rootView.findViewById<Button>(R.id.bt_Registrar)
        btRegister.setOnClickListener {
            val username = rootView.findViewById<EditText>(R.id.etEmail).text.toString()
            val password = rootView.findViewById<EditText>(R.id.etPassword).text.toString()
            if (userRole == "ROLE_DEVELOPER") {
                val firstName = rootView.findViewById<EditText>(R.id.etFirstName).text.toString()
                val lastName = rootView.findViewById<EditText>(R.id.etLastName).text.toString()
                if (username.isNotEmpty() && password.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty()) {
                    performDeveloperRegister(username, password, firstName, lastName)
                } else {
                    Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                }
            } else if (userRole == "ROLE_ENTERPRISE") {
                val enterpriseName = rootView.findViewById<EditText>(R.id.etEnterpriseName).text.toString()
                if (username.isNotEmpty() && password.isNotEmpty() && enterpriseName.isNotEmpty()) {
                    performEnterpriseRegister(username, password, enterpriseName)
                } else {
                    Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val btBack = rootView.findViewById<LinearLayout>(R.id.bt_Back)
        btBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return rootView
    }

    private fun performDeveloperRegister(username: String, password: String, firstName: String, lastName: String) {
        val request = DeveloperRegisterRequest(username, password, firstName, lastName)
        authenticationRepository.registerDeveloper(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Creación de cuenta exitosa", Toast.LENGTH_SHORT).show()
                    navigateToLoginFragment()
                } else {
                    Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun performEnterpriseRegister(username: String, password: String, enterpriseName: String) {
        val request = EnterpriseRegisterRequest(username, password, enterpriseName)
        authenticationRepository.registerEnterprise(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Creación de cuenta exitosa", Toast.LENGTH_SHORT).show()
                    navigateToLoginFragment()
                } else {
                    Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToLoginFragment() {
        val loginFragment = LogInFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentAuthContainer, loginFragment)
            .addToBackStack(null)
            .commit()
    }
}