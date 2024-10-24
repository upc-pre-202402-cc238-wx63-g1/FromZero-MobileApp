package com.cursokotlin.appfromzero.UI.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.cursokotlin.appfromzero.AuthActivity
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.models.HomeViewModel

class MenuFragment : Fragment() {

    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_menu, container, false)

        // Inicializar los botones y aplicar lógica de visibilidad según el rol del usuario
        initializeUI(rootView)

        return rootView
    }

    // Encapsula la inicialización y configuración del UI
    private fun initializeUI(rootView: View) {
        val lyCreateProject = rootView.findViewById<LinearLayout>(R.id.ly_createProject)
        val btnNotifications = rootView.findViewById<ImageButton>(R.id.btnNotifications)
        val btnCreateProject = rootView.findViewById<ImageButton>(R.id.btnCreateProject)
        val btnSupport = rootView.findViewById<ImageButton>(R.id.btnSupport)
        val btnLogout = rootView.findViewById<ImageButton>(R.id.btnLogout)

        // Observar el rol del usuario para manipular la UI
        homeViewModel.userRole.observe(viewLifecycleOwner) { role ->
            if (role == "desarrollador") {
                // Eliminar el LinearLayout si el rol es desarrollador
                (lyCreateProject.parent as? ViewGroup)?.removeView(lyCreateProject)
            }
        }

        // Establecer listeners para los botones
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
            val intent = Intent(requireContext(), AuthActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    // Método común para reemplazar fragmentos
    private fun replaceFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.replace(R.id.fragmenContainer, fragment)
        transaction.addToBackStack("principal")
        transaction.commit()
    }
}
