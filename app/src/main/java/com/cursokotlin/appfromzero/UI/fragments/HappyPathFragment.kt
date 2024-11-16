package com.cursokotlin.appfromzero.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.cursokotlin.appfromzero.MainActivity
import com.cursokotlin.appfromzero.R

class HappyPathFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_happy_path, container, false)
        val btnAccept = rootView.findViewById<Button>(R.id.btn_accept)

        val source = arguments?.getString("source") // Detectar el origen
        btnAccept.setOnClickListener {
            if (source == "register") {
                navigateToLoginFragment()
            } else if (source == "HomeDev") {
                navigateToHomeDeveloperFragment()
            } else if (source == "HomeEnterprise") {
                navigateToHomeEnterpriseFragment()
            } else {
                Toast.makeText(requireContext(), "No existe ese origen", Toast.LENGTH_SHORT).show()
            }
        }
        return rootView
    }

    private fun navigateToLoginFragment() {
        val loginFragment = LogInFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentAuthContainer, loginFragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToHomeDeveloperFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmenContainer, HomeDeveloperFragment())
            .addToBackStack(null)
            .commit()
        (activity as MainActivity).showHomeTab()
    }

    private fun navigateToHomeEnterpriseFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmenContainer, HomeEnterpriseFragment())
            .addToBackStack(null)
            .commit()
        (activity as MainActivity).showHomeTab()
    }
}