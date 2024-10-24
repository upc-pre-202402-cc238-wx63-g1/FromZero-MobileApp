package com.cursokotlin.appfromzero.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.cursokotlin.appfromzero.R

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_register, container, false)
        val llVolver = rootView.findViewById<LinearLayout>(R.id.ll_Volver)
        llVolver.setOnClickListener {
            replaceFragment(LogInFragment())
        }

        val bt_IniciarSesion = rootView.findViewById<Button>(R.id.bt_IniciarSesion)
        bt_IniciarSesion.setOnClickListener {
            replaceFragment(LogInFragment())
        }
        return rootView
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.replace(R.id.fragmentAuthContainer, fragment)
        transaction.addToBackStack("principal")
        transaction.commit()
    }

}