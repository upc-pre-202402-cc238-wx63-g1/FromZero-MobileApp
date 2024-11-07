package com.cursokotlin.appfromzero.UI.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.data.remote.RetrofitClient
import com.cursokotlin.appfromzero.models.Deliverable
import com.cursokotlin.appfromzero.models.deliverable.DeliverableResponse
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Response

class CreateDeliverableFragment : DialogFragment() {

    interface OnDeliverableCreatedListener {
        fun onDeliverableCreated(deliverable: Deliverable)
    }

    private var listener: OnDeliverableCreatedListener? = null

    private var idProject: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_deliverable, container, false)

        arguments?.let {
            idProject = it.getLong("idProject")
        }

        setupCreateButton(view)
        setupCancelButton(view)

        return view
    }

    private fun setupCreateButton(view: View) {
        val createButton = view.findViewById<Button>(R.id.btEdit)
        val titleField = view.findViewById<TextInputEditText>(R.id.etTitle)
        val descriptionField = view.findViewById<TextInputEditText>(R.id.etDescription)
        val dateField = view.findViewById<TextInputEditText>(R.id.etDate)

        createButton.setOnClickListener {
            val title = titleField.text.toString()
            val description = descriptionField.text.toString()
            val date = dateField.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty() && date.isNotEmpty()) {
                val newDeliverableResponse = DeliverableResponse(
                    name = title,
                    description = description,
                    date = date,
                    projectId = idProject
                )


                val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                val token = sharedPreferences.getString("token", "") ?: ""

                RetrofitClient.deliverableService.createDeliverable(newDeliverableResponse, "Bearer $token")
                    .enqueue(object : retrofit2.Callback<DeliverableResponse> {
                        override fun onResponse(call: Call<DeliverableResponse>, response: Response<DeliverableResponse>) {
                            if (response.isSuccessful) {

                                response.body()?.let {
                                    val newDeliverable = Deliverable(
                                        id = 0,
                                        name = it.name,
                                        idProject = it.projectId,
                                        date = it.date,
                                        state = "Pendiente",
                                        description = it.description,
                                        message = ""
                                    )
                                    listener?.onDeliverableCreated(newDeliverable)
                                    dismiss()
                                }
                            } else {
                                // Manejar error de la API
                                Toast.makeText(context, "Error al crear el entregable: ${response.message()}", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<DeliverableResponse>, t: Throwable) {
                            // Manejar error en la conexión
                            Toast.makeText(context, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                        }
                    })
            } else {
                Toast.makeText(context, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupCancelButton(view: View) {
        val cancelButton = view.findViewById<Button>(R.id.btCancel)
        cancelButton.setOnClickListener {
            dismiss()
        }
    }


    fun setOnDeliverableCreatedListener(listener: OnDeliverableCreatedListener) {
        this.listener = listener
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog_background)
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}