package com.cursokotlin.appfromzero.UI.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.models.Deliverable
import com.google.android.material.textfield.TextInputEditText
import java.util.*

class CreateDeliverableFragment : DialogFragment() {

    interface OnDeliverableCreatedListener {
        fun onDeliverableCreated(deliverable: Deliverable)
    }

    private var listener: OnDeliverableCreatedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_deliverable, container, false)
        setupCreateButton(view)
        setupCancelButton(view)
        setupDatePicker(view)
        return view
    }

    private fun setupCreateButton(view: View) {
        val createButton = view.findViewById<Button>(R.id.btEdit)
        val titleField = view.findViewById<TextInputEditText>(R.id.etTitle)
        val descriptionField = view.findViewById<TextInputEditText>(R.id.etDescription)
        val dateField = view.findViewById<TextView>(R.id.tvDate)

        createButton.setOnClickListener {
            val title = titleField.text.toString()
            val description = descriptionField.text.toString()
            val date = dateField.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty() && date.isNotEmpty()) {
                val newDeliverable = Deliverable(
                    id = 0, //id temporal
                    title = title,
                    projectName = "Plataforma de Comercio Electrónico Geekit",
                    date = date,
                    state = "Espera",
                    description = description
                )
                listener?.onDeliverableCreated(newDeliverable)
                dismiss()
            } else {
                Toast.makeText(context, "Por favor, completa todos los campos", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setupCancelButton(view: View) {
        val cancelButton = view.findViewById<Button>(R.id.btCancel)
        cancelButton.setOnClickListener {
            dismiss()
        }
    }

    private fun setupDatePicker(view: View) {
        val dateField = view.findViewById<TextView>(R.id.tvDate)

        dateField.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            //formatear la fecha
            val datePickerDialog =
                DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                    val formattedDate =
                        String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                    dateField.text = formattedDate
                }, year, month, day)

            datePickerDialog.show()
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