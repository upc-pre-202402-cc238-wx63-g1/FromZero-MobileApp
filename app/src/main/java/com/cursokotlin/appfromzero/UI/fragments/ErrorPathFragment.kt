package com.cursokotlin.appfromzero.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.cursokotlin.appfromzero.R

class ErrorPathFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_error_path, container, false)
        val btnError = rootView.findViewById<Button>(R.id.btn_volver)

        btnError.setOnClickListener {
            parentFragmentManager.popBackStack() // Vuelve al fragmento anterior
        }

        return rootView
    }

}