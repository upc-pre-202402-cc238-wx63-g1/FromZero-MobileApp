package com.cursokotlin.appfromzero.UI.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.data.remote.RetrofitClient
import com.cursokotlin.appfromzero.data.repository.deliverable.DeliverableRepository
import com.cursokotlin.appfromzero.models.deliverable.ReviewDeliverableResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Long.parseLong

class ReviewDeliverableFragment : DialogFragment() {

    private val deliverableRepository = DeliverableRepository(RetrofitClient.deliverableService)
    private var token: String? = null
    private var deliverableId: Long = 0L
    private var developerMessage: String = "No hay una entrega disponible."

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_review_deliverable, container, false)
        initArguments()
        initView(view)
        setupCancelButton(view)
        return view
    }

    private fun initArguments() {
        developerMessage = arguments?.getString("developerMessage") ?: "No hay una entrega disponible."
        deliverableId = arguments?.getLong("deliverableId") ?: 0L
        token = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .getString("token", null)
    }

    private fun initView(view: View) {
        val tvDeveloperMessage = view.findViewById<TextView>(R.id.tvDeveloperMessage)
        tvDeveloperMessage.text = developerMessage

        val btApprove = view.findViewById<Button>(R.id.btApprove)
        btApprove.setOnClickListener {
            if (developerMessage == "No hay ninguna entrega disponible.") {
                Toast.makeText(requireContext(), "No puede realizar esta acción porque no existe una entrega.", Toast.LENGTH_SHORT).show()
            } else {
                token?.let { tokenString ->
                    reviewDeliverable(true, tokenString)
                }
            }
        }

        val btReject = view.findViewById<Button>(R.id.btReject)
        btReject.setOnClickListener {
            if (developerMessage == "No hay ninguna entrega disponible.") {
                Toast.makeText(requireContext(), "No puede realizar esta acción porque no existe una entrega.", Toast.LENGTH_SHORT).show()
            } else {
                token?.let { tokenString ->
                    reviewDeliverable(false, tokenString)
                }
            }
        }
    }

    private fun reviewDeliverable(accepted: Boolean, token: String) {
        Log.d("ReviewDeliverable", "Sending request with deliverableId: $deliverableId, accepted: $accepted, token: $token")

        deliverableRepository.reviewDeliverable(deliverableId, accepted, token).enqueue(object : Callback<ReviewDeliverableResponse> {
            override fun onResponse(
                call: Call<ReviewDeliverableResponse>, response: Response<ReviewDeliverableResponse>
            ) {
                if (response.isSuccessful) {
                    val message = if (accepted) "Entrega aprobada" else "Entrega rechazada"
                    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

                    val newState = if (accepted) "Approved" else "Rejected"
                    (activity as? DeliverablesFragment)?.updateDeliverableState(deliverableId, newState)

                    dismiss()
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("ReviewDeliverable", "Error al realizar la acción. Código: ${response.code()}, Cuerpo: $errorBody")
                    Toast.makeText(requireContext(), "Error al realizar la acción.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ReviewDeliverableResponse>, t: Throwable) {
                Log.e("ReviewDeliverable", "Error de red: ${t.message}")
                Toast.makeText(requireContext(), "Error de red. Intenta nuevamente.", Toast.LENGTH_SHORT).show()
            }
        })
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