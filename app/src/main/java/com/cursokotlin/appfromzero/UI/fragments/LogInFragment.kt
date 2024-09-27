package com.cursokotlin.appfromzero.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.cursokotlin.appfromzero.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LogInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogInFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_log_in, container, false)

        val llNext = rootView.findViewById<LinearLayout>(R.id.ll_Volver)
        llNext.setOnClickListener{
            replaceFragment(InitationFragment())
        }

        val tvOlvidarContra = rootView.findViewById<TextView>(R.id.tv_OlvidarContra)
        tvOlvidarContra.setOnClickListener {
            replaceFragment(RecoveryPasswordFragment())
        }

        val tvRegister = rootView.findViewById<TextView>(R.id.tv_CrearCuenta)
        tvRegister.setOnClickListener {
            replaceFragment(RolFragment())
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