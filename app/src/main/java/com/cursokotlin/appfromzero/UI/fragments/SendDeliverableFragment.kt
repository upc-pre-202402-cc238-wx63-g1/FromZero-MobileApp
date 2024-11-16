package com.cursokotlin.appfromzero.UI.fragments

import android.content.Context
import android.os.Bundle
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
import com.cursokotlin.appfromzero.models.deliverable.SendDeliverableResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendDeliverableFragment : DialogFragment() {

    private var listener: OnDeliverableSentListener? = null
    private val deliverableRepository = DeliverableRepository(RetrofitClient.deliverableService)
    private var token: String? = null
    private var deliverableId: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_send_deliverable, container, false)
        initArguments()
        initView(view)
        return view
    }

    private fun initArguments() {
        deliverableId = arguments?.getLong("deliverableId") ?: 0L
        token = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .getString("token", null)
    }

    private fun initView(view: View) {
        val messageField = view.findViewById<EditText>(R.id.etDeveloperMessage)
        val sendButton = view.findViewById<Button>(R.id.btSend)

        sendButton.setOnClickListener {
            val message = messageField.text.toString()
            if (message.isNotEmpty()) {
                token?.let { tokenString ->
                    sendDeliverable(deliverableId, message, tokenString)
                }
            } else {
                Toast.makeText(requireContext(), "Please enter a message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendDeliverable(deliverableId: Long, message: String, token: String) {
        deliverableRepository.sendDeliverable(deliverableId, message, token).enqueue(object : Callback<SendDeliverableResponse> {
            override fun onResponse(call: Call<SendDeliverableResponse>, response: Response<SendDeliverableResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Deliverable sent successfully", Toast.LENGTH_SHORT).show()
                    listener?.onDeliverableSendState(deliverableId, "Awaiting Review")
                    dismiss()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val statusCode = response.code()
                    Toast.makeText(requireContext(), "Failed to send deliverable: ${response.message()} (Status code: $statusCode, Error: $errorBody)", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<SendDeliverableResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Connection error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    interface OnDeliverableSentListener {
        fun onDeliverableSendState(deliverableId: Long, newState: String)
    }

    fun setOnDeliverableSentListener(listener: OnDeliverableSentListener) {
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