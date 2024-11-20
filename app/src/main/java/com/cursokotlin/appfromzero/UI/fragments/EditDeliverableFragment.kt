package com.cursokotlin.appfromzero.UI.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.data.remote.RetrofitClient
import com.cursokotlin.appfromzero.data.repository.deliverable.DeliverableRepository
import com.cursokotlin.appfromzero.models.Deliverable
import com.cursokotlin.appfromzero.models.deliverable.UpdateDeliverableRequest
import com.cursokotlin.appfromzero.models.deliverable.UpdateDeliverableResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class EditDeliverableFragment : DialogFragment() {

    interface OnDeliverableEditedListener {
        fun onDeliverableEdited(newDeliverable: Deliverable)
    }

    private var listener: OnDeliverableEditedListener? = null
    private val deliverableRepository = DeliverableRepository(RetrofitClient.deliverableService)
    private var idProject: Long = 0
    private var deliverableId: Long = 0
    private var projectName: String = ""

    fun setOnDeliverableEditedListener(listener: OnDeliverableEditedListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_deliverable, container, false)

        arguments?.let {
            idProject = it.getLong("idProject")
            deliverableId = it.getLong("deliverableId")
            projectName = it.getString("projectName", "")
        }

        if (deliverableId == 0L) {
            Log.e("EditDeliverableFragment", "Invalid deliverableId: $deliverableId")
            Toast.makeText(requireContext(), "Invalid deliverable ID", Toast.LENGTH_SHORT).show()
            dismiss()
            return null
        }

        val deliverableTitle = arguments?.getString("deliverableName")
        val deliverableDescription = arguments?.getString("deliverableDescription")
        val deliverableDate = arguments?.getString("deliverableDate")

        val etTitle = view.findViewById<EditText>(R.id.etTitle)
        val etDescription = view.findViewById<EditText>(R.id.etDescription)
        val etDate = view.findViewById<EditText>(R.id.etDate)

        etTitle.setText(deliverableTitle)
        etDescription.setText(deliverableDescription)
        etDate.setText(deliverableDate)

        setupDatePicker(etDate)
        setupCancelButton(view)
        setupSaveButton(view, etTitle, etDescription, etDate)
        return view
    }

    private fun setupDatePicker(dateField: EditText) {
        dateField.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog =
                DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate =
                        String.format("%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                    dateField.setText(formattedDate)
                }, year, month, day)
            datePickerDialog.show()
        }
    }

    private fun setupSaveButton(
        view: View,
        etTitle: EditText,
        etDescription: EditText,
        etDate: EditText
    ) {
        val saveButton = view.findViewById<Button>(R.id.btEdit)
        saveButton.setOnClickListener {
            val updatedTitle = etTitle.text.toString()
            val updatedDescription = etDescription.text.toString()
            val updatedDate = etDate.text.toString()

            val updateRequest = UpdateDeliverableRequest(
                name = updatedTitle,
                description = updatedDescription,
                date = updatedDate
            )

            updateDeliverableOnServer(updateRequest)
        }
    }

    private fun updateDeliverableOnServer(updateRequest: UpdateDeliverableRequest) {
        val sharedPreferences =
            requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)

        if (token != null) {
            val call = deliverableRepository.updateDeliverable(deliverableId, updateRequest, token)
            Log.d("EditDeliverableFragment", "Request URL: ${call.request()}")
            Log.d("EditDeliverableFragment", "Request Body: $updateRequest")

            call.enqueue(object : Callback<UpdateDeliverableResponse> {
                override fun onResponse(
                    call: Call<UpdateDeliverableResponse>,
                    response: Response<UpdateDeliverableResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            val developerMessage = responseBody.developerMessage ?: "No Message"
                            val updatedDeliverable = Deliverable(
                                id = responseBody.id,
                                name = responseBody.name,
                                description = responseBody.description,
                                date = responseBody.date,
                                state = responseBody.state,
                                idProject = idProject,
                                developerMessage = developerMessage,
                                projectName = projectName
                            )

                            listener?.onDeliverableEdited(updatedDeliverable)
                            dismiss()
                        }
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Error updating deliverable",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UpdateDeliverableResponse>, t: Throwable) {
                    Toast.makeText(
                        requireContext(),
                        "Connection error: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            Toast.makeText(requireContext(), "Authentication token not found", Toast.LENGTH_SHORT)
                .show()
        }
    }


    private fun setupCancelButton(view: View) {
        val cancelButton = view.findViewById<Button>(R.id.btCancel)
        cancelButton.setOnClickListener {
            dismiss()
        }
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