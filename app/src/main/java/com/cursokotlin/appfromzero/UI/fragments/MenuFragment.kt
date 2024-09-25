package com.example.fromzero_andre.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.fromzero_andre.R

/**
 * A simple [Fragment] subclass.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_menu, container, false)

        val btnNotifications = rootView.findViewById<ImageButton>(R.id.btnNotifications)
        val btnCreateProject = rootView.findViewById<ImageButton>(R.id.btnCreateProject)
        val btnSupport = rootView.findViewById<ImageButton>(R.id.btnSupport)
        val btnLogout = rootView.findViewById<ImageButton>(R.id.btnLogout)

        // Set click listeners for each button, passing the corresponding fragment to replaceFragment method
        btnNotifications.setOnClickListener {
            replaceFragment(NotificationFragment())
        }

        btnCreateProject.setOnClickListener {
            replaceFragment(CreateProjectFragment())
        }

        btnSupport.setOnClickListener {
            replaceFragment(SupportFragment())
        }

        btnLogout.setOnClickListener {
            replaceFragment(LogoutFragment())
        }

        return rootView
    }

    // Common method to replace fragment
    private fun replaceFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack("principal")
        transaction.commit()
    }
}
