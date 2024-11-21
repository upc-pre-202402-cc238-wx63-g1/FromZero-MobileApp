package com.cursokotlin.appfromzero.UI.fragments

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.constraintlayout.widget.Guideline
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

    private val authenticationRepository =
        AuthenticationRepository(RetrofitClient.authenticationService)
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
//        val guideline = rootView.findViewById<Guideline>(R.id.guideline) // Add this line
//        val dynamicguideline = rootView.findViewById<Guideline>(R.id.dynamicGuideline)

        if (userRole == "ROLE_DEVELOPER") {
            etFirstNameLayout.visibility = View.VISIBLE
            etLastNameLayout.visibility = View.VISIBLE
            etEnterpriseNameLayout.visibility = View.GONE
//            guideline.setGuidelineBegin(1520)
//            dynamicguideline.setGuidelineBegin(1500)
        } else if (userRole == "ROLE_ENTERPRISE") {
            etFirstNameLayout.visibility = View.GONE
            etLastNameLayout.visibility = View.GONE
            etEnterpriseNameLayout.visibility = View.VISIBLE
//            guideline.setGuidelineBegin(1310)
//            dynamicguideline.setGuidelineBegin(1270)
        }

        val btRegister = rootView.findViewById<Button>(R.id.bt_Registrar)
        btRegister.setOnClickListener {
            val username = rootView.findViewById<EditText>(R.id.etEmail).text.toString()
            val password = rootView.findViewById<EditText>(R.id.etPassword).text.toString()
            if (userRole == "ROLE_DEVELOPER") {
                val firstName = rootView.findViewById<EditText>(R.id.etFirstName).text.toString()
                val lastName = rootView.findViewById<EditText>(R.id.etLastName).text.toString()
                if (username.isNotEmpty() && password.isNotEmpty() && firstName.isNotEmpty() && lastName.isNotEmpty()) {
                    showTermsConditionsDialog(rootView)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Por favor, complete todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else if (userRole == "ROLE_ENTERPRISE") {
                val enterpriseName =
                    rootView.findViewById<EditText>(R.id.etEnterpriseName).text.toString()
                if (username.isNotEmpty() && password.isNotEmpty() && enterpriseName.isNotEmpty()) {
                    showTermsConditionsDialog(rootView)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Por favor, complete todos los campos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        val btBack = rootView.findViewById<LinearLayout>(R.id.bt_Back)
        btBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return rootView
    }


    private fun performDeveloperRegister(
        username: String,
        firstName: String,
        lastName: String,
        password: String
    ) {
        val request = DeveloperRegisterRequest(username, firstName, lastName, password)
        Log.d("RegisterFragment", "DeveloperRegisterRequest: $request")
        authenticationRepository.registerDeveloper(request)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("RegisterFragment", "Registration successful: ${response.body()}")
                        Toast.makeText(
                            requireContext(),
                            "Creación de cuenta exitosa",
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToHappyPathFragment()
                    } else {
                        Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_SHORT)
                            .show()
                        navigateToErrorPathFragment()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                    navigateToErrorPathFragment()
                }
            })
    }

    private fun performEnterpriseRegister(
        username: String,
        enterpriseName: String,
        password: String
    ) {
        val request = EnterpriseRegisterRequest(username, enterpriseName, password)
        Log.d("RegisterFragment", "DeveloperRegisterRequest: $request")
        authenticationRepository.registerEnterprise(request)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        Log.d("RegisterFragment", "Registration successful: ${response.body()}")
                        Toast.makeText(
                            requireContext(),
                            "Creación de cuenta exitosa",
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToHappyPathFragment()
                    } else {
                        Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_SHORT)
                            .show()
                        navigateToErrorPathFragment()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                    navigateToErrorPathFragment()
                }
            })
    }

    private fun navigateToHappyPathFragment() {
        val happyPathFragment = HappyPathFragment()
        happyPathFragment.arguments = Bundle().apply {
            putString("source", "register")
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentAuthContainer, happyPathFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToErrorPathFragment() {
        val errorPathFragment = ErrorPathFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentAuthContainer, errorPathFragment)
            .addToBackStack(null) // Agrega este fragmento al stack
            .commit()
    }

    private fun showTermsConditionsDialog(rootView: View) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.terms_and_conditions)
        dialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 1).toInt(),
            (resources.displayMetrics.heightPixels * 1).toInt()
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setDimAmount(0.8f)

        val scrollView = dialog.findViewById<ScrollView>(R.id.scrollView)
        val btnAcceptTerms = dialog.findViewById<Button>(R.id.btnAcceptTerms)

        scrollView.viewTreeObserver.addOnScrollChangedListener {
            val view = scrollView.getChildAt(scrollView.childCount - 1)
            val diff = view.bottom - (scrollView.height + scrollView.scrollY)
            btnAcceptTerms.isEnabled = diff <= 0
        }

        btnAcceptTerms.setOnClickListener {

            val username = rootView.findViewById<EditText>(R.id.etEmail).text.toString()
            val password = rootView.findViewById<EditText>(R.id.etPassword).text.toString()
            if (userRole == "ROLE_DEVELOPER") {
                val firstName = rootView.findViewById<EditText>(R.id.etFirstName).text.toString()
                val lastName = rootView.findViewById<EditText>(R.id.etLastName).text.toString()
                    performDeveloperRegister(username, password, firstName, lastName)
            } else if (userRole == "ROLE_ENTERPRISE") {
                val enterpriseName =
                    rootView.findViewById<EditText>(R.id.etEnterpriseName).text.toString()
                    performEnterpriseRegister(username, password, enterpriseName)
            }

            dialog.dismiss()
        }

        dialog.show()
    }
}