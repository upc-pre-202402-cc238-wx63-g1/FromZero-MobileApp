package com.cursokotlin.appfromzero.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.model.Deliverable
import com.google.android.material.textfield.TextInputEditText


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
        return view
    }

    private fun setupCreateButton(view: View) {
        val createButton = view.findViewById<Button>(R.id.btCreate)
        val titleField = view.findViewById<TextInputEditText>(R.id.etTitle)
        val descriptionField = view.findViewById<TextInputEditText>(R.id.etDescription)
        val dateField = view.findViewById<TextInputEditText>(R.id.etDate)

        createButton.setOnClickListener {

            val title = titleField.text.toString()
            val description = descriptionField.text.toString()
            val date = dateField.text.toString()


            if (title.isNotEmpty() && description.isNotEmpty() && date.isNotEmpty()) {
                val newDeliverable = Deliverable(title, "Plataforma de Comercio Electrónico Geekit", date, "Espera", description)
                listener?.onDeliverableCreated(newDeliverable)
                dismiss()
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
