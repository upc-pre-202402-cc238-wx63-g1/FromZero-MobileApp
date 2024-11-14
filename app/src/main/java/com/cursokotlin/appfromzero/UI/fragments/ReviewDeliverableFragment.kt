package com.cursokotlin.appfromzero.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.cursokotlin.appfromzero.R

class ReviewDeliverableFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_review_deliverable, container, false)

        val developerMessage = arguments?.getString("developerMessage") ?: "No hay una entrega disponible."

        val tvDeveloperMessage = view.findViewById<TextView>(R.id.tvDeveloperMessage)
        tvDeveloperMessage.text = developerMessage

        val btApprove = view.findViewById<Button>(R.id.btApprove)
        btApprove.setOnClickListener {
            if (developerMessage == "No hay ninguna entrega disponible.") {
                Toast.makeText(requireContext(), "No puede realizar esta acción porque no existe una entrega.", Toast.LENGTH_SHORT).show()
            } else {
                //TODO: Approve endpoint
            }
        }

        setupCancelButton(view)
        return view
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