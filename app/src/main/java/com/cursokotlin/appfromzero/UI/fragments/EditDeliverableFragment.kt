package com.cursokotlin.appfromzero.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.models.Deliverable

class EditDeliverableFragment : DialogFragment() {

    interface OnDeliverableEditedListener {
        fun onDeliverableEdited(newDeliverable: Deliverable)
    }

    private var listener: OnDeliverableEditedListener? = null

    fun setOnDeliverableEditedListener(listener: OnDeliverableEditedListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_deliverable, container, false)

        val deliverableId = arguments?.getInt("deliverableId")
        val deliverableTitle = arguments?.getString("deliverableTitle")
        val deliverableDescription = arguments?.getString("deliverableDescription")
        val deliverableDate = arguments?.getString("deliverableDate")

        val etTitle = view.findViewById<EditText>(R.id.etTitle)
        val etDescription = view.findViewById<EditText>(R.id.etDescription)
        val etDate = view.findViewById<EditText>(R.id.etDate)

        etTitle.setText(deliverableTitle)
        etDescription.setText(deliverableDescription)
        etDate.setText(deliverableDate)

        setupCancelButton(view)
        setupSaveButton(view, deliverableId, etTitle, etDescription, etDate)
        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(R.drawable.rounded_dialog_background)
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupSaveButton(view: View, deliverableId: Int?, etTitle: EditText, etDescription: EditText, etDate: EditText) {
        val saveButton = view.findViewById<Button>(R.id.btEdit)
        saveButton.setOnClickListener {
            val newDeliverable = Deliverable(
                id = deliverableId ?: 0,
                title = etTitle.text.toString(),
                projectName = "Plataforma de Comercio Electrónico Geekit",
                date = etDate.text.toString(),
                state = "Espera",
                description = etDescription.text.toString()
            )
            listener?.onDeliverableEdited(newDeliverable)
            dismiss()
        }
    }

    private fun setupCancelButton(view: View) {
        val cancelButton = view.findViewById<Button>(R.id.btCancel)
        cancelButton.setOnClickListener {
            dismiss()
        }
    }
}