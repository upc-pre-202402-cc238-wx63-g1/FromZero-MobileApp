package com.cursokotlin.appfromzero.UI.fragments

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.data.remote.RetrofitClient
import com.cursokotlin.appfromzero.data.repository.comunication.SupportRepository
import com.cursokotlin.appfromzero.models.comunication.SupportRequest
import com.cursokotlin.appfromzero.models.comunication.SupportResponse
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SupportFragment : Fragment() {

    private val PICK_FILE_REQUEST_CODE = 1
    private var selectedFileUri: Uri? = null
    private lateinit var supportRepository: SupportRepository
    private var senderId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_support, container, false)

        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "") ?: ""
        senderId = sharedPreferences.getLong("userId", 0)
        supportRepository = SupportRepository(RetrofitClient.supportService(token))

        Log.d("SupportFragment", "Token: $token")
        Log.d("SupportFragment", "Sender ID: $senderId")

        val problemTypes = resources.getStringArray(R.array.problem_types)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            problemTypes
        )
        val autoCompleteTextView =
            view.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        autoCompleteTextView.setAdapter(adapter)

        val fileAttachmentArea = view.findViewById<MaterialCardView>(R.id.fileAttachmentArea)
        fileAttachmentArea.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
        }

        val titleInputLayout = view.findViewById<TextInputLayout>(R.id.titleInputLayout)
        val descriptionInputLayout = view.findViewById<TextInputLayout>(R.id.descriptionInputLayout)
        val submitButton = view.findViewById<MaterialButton>(R.id.submitButton)

        submitButton.setOnClickListener {
            val title = titleInputLayout.editText?.text.toString()
            val type = autoCompleteTextView.text.toString()
            val description = descriptionInputLayout.editText?.text.toString()

            if (title.isNotEmpty() && type.isNotEmpty() && description.isNotEmpty()) {
                val supportRequest = SupportRequest(title, type, description, senderId)
                Log.d("SupportFragment", "Request: $supportRequest")
                createSupport(supportRequest)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Por favor, complete todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedFileUri = data?.data
            selectedFileUri?.let {
                Toast.makeText(
                    requireContext(),
                    "Archivo seleccionado: ${it.path}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createSupport(request: SupportRequest) {
        supportRepository.createSupport(request).enqueue(object : Callback<SupportResponse> {
            override fun onResponse(
                call: Call<SupportResponse>,
                response: Response<SupportResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Soporte creado con éxito", Toast.LENGTH_SHORT)
                        .show()
                    clearInputFields()
                } else {
                    Log.e(
                        "SupportFragment",
                        "Error al crear soporte: ${response.errorBody()?.string()}"
                    )
                    Toast.makeText(requireContext(), "Error al crear soporte", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<SupportResponse>, t: Throwable) {
                Log.e("SupportFragment", "Error: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun clearInputFields() {
        view?.findViewById<TextInputLayout>(R.id.titleInputLayout)?.editText?.text?.clear()
        view?.findViewById<TextInputLayout>(R.id.descriptionInputLayout)?.editText?.text?.clear()
        view?.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)?.text?.clear()
    }
}