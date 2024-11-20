package com.cursokotlin.appfromzero.UI.fragments

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.data.remote.RetrofitClient
import com.cursokotlin.appfromzero.data.repository.comunication.MessageRepository
import com.cursokotlin.appfromzero.models.comunication.MessageRequest
import com.cursokotlin.appfromzero.models.comunication.MessageResponse
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageFragment : Fragment() {
    private lateinit var messageRepository: MessageRepository
    private var senderId: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_message, container, false)

        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "") ?: ""
        senderId = sharedPreferences.getLong("userId", 0)
        messageRepository = MessageRepository(RetrofitClient.messageService(token))

        Log.d("MessageFragment", "Token: $token")
        Log.d("MessageFragment", "Sender ID: $senderId")

        val recipientInput = rootView.findViewById<TextInputEditText>(R.id.recipient)
        val titleInput = rootView.findViewById<TextInputEditText>(R.id.message_title)
        val subjectInput = rootView.findViewById<TextInputEditText>(R.id.subject)
        val messageInput = rootView.findViewById<TextInputEditText>(R.id.message)
        val sendButton = rootView.findViewById<MaterialButton>(R.id.attach_files)

        sendButton.setOnClickListener {
            val recipientId = recipientInput.text.toString().toLongOrNull()
            val title = titleInput.text.toString()
            val subject = subjectInput.text.toString()
            val emailBody = messageInput.text.toString()

            if (recipientId != null && title.isNotEmpty() && subject.isNotEmpty() && emailBody.isNotEmpty()) {
                val request = MessageRequest(subject, emailBody, recipientId, senderId)
                Log.d("MessageFragment", "Request: $request")
                sendMessage(request)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Por favor, complete todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return rootView
    }

    private fun sendMessage(request: MessageRequest) {
        messageRepository.createMessage(request).enqueue(object : Callback<MessageResponse> {
            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Mensaje enviado con éxito",
                        Toast.LENGTH_SHORT
                    ).show()
                    clearInputFields()
                } else {
                    Log.e(
                        "MessageFragment",
                        "Error al enviar el mensaje: ${response.errorBody()?.string()}"
                    )
                    Toast.makeText(
                        requireContext(),
                        "Error al enviar el mensaje",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                Log.e("MessageFragment", "Error: ${t.message}")
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun clearInputFields() {
        view?.findViewById<TextInputEditText>(R.id.recipient)?.text?.clear()
        view?.findViewById<TextInputEditText>(R.id.message_title)?.text?.clear()
        view?.findViewById<TextInputEditText>(R.id.subject)?.text?.clear()
        view?.findViewById<TextInputEditText>(R.id.message)?.text?.clear()
    }
}