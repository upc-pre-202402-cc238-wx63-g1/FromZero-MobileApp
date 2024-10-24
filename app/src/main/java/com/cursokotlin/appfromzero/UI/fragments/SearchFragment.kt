package com.cursokotlin.appfromzero.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.adapters.DeveloperAdapter
import com.cursokotlin.appfromzero.models.Developer

class   SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        // Initialize RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.rvDevelopers)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Sample data
        val developers = listOf(
            Developer("Juan Pérez", 4.5f, R.drawable.sample_profile, R.drawable.sample_flag, "Especialista en desarrollo móvil con 5 años de experiencia en Android y iOS.", "Android, iOS, Kotlin, Swift"),
            Developer("Ana Gómez", 4.0f, R.drawable.sample_profile, R.drawable.sample_flag, "Desarrolladora full-stack con experiencia en Java y JavaScript.", "Java, JavaScript, React, Spring"),
            Developer("Carlos Ruiz", 4.8f, R.drawable.sample_profile, R.drawable.sample_flag, "Ingeniero de software con enfoque en inteligencia artificial.", "Python, TensorFlow, Keras, PyTorch")
        )

        // Set adapter
        recyclerView.adapter = DeveloperAdapter(developers)

        return view
    }
}