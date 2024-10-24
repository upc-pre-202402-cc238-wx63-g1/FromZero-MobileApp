package com.cursokotlin.appfromzero.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.models.HomeViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MenuFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        homeViewModel.userRole.observe(viewLifecycleOwner){ role ->
            when(role) {
                "empresa" -> {

                }
                "desarrollador" -> {

                }
            }
        }

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
//            replaceFragment(ViewProjectFragment())
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
        transaction.replace(R.id.fragmenContainer, fragment)
        transaction.addToBackStack("principal")
        transaction.commit()
    }
}
