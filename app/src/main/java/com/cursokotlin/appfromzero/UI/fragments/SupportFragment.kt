package com.cursokotlin.appfromzero.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.cursokotlin.appfromzero.R

class SupportFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_support, container, false)

        val problemTypes = resources.getStringArray(R.array.problem_types)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, problemTypes)

        val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        autoCompleteTextView.setAdapter(adapter)

        return view
    }
}