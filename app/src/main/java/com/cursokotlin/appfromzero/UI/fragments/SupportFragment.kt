package com.cursokotlin.appfromzero.UI.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cursokotlin.appfromzero.R
import com.google.android.material.card.MaterialCardView

class SupportFragment : Fragment() {

    private val PICK_FILE_REQUEST_CODE = 1
    private var selectedFileUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_support, container, false)

        val problemTypes = resources.getStringArray(R.array.problem_types)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, problemTypes)
        val autoCompleteTextView = view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        autoCompleteTextView.setAdapter(adapter)

        val fileAttachmentArea = view.findViewById<MaterialCardView>(R.id.fileAttachmentArea)
        fileAttachmentArea.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedFileUri = data?.data
            selectedFileUri?.let {
                Toast.makeText(requireContext(), "Archivo seleccionado: ${it.path}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}