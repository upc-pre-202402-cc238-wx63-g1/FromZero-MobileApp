package com.cursokotlin.appfromzero.UI.fragments

import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.cursokotlin.appfromzero.R

class RolFragment : Fragment() {
    var devSelected = false
    var empresaSelected = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_rol, container, false)

        // Resetear las variables de selección
        devSelected = false
        empresaSelected = false

        val tvEnterprise = rootView.findViewById<TextView>(R.id.tv_Empresa)
        val tvDev = rootView.findViewById<TextView>(R.id.tv_Dev)

        val cvEnterprise = rootView.findViewById<CardView>(R.id.cv_Empresa)
        val ivEnterprise = rootView.findViewById<ImageView>(R.id.iv_empresa)
        ivEnterprise.setImageResource(R.drawable.ic_baul)

        val cvDev = rootView.findViewById<CardView>(R.id.cv_Dev)
        val ivDev = rootView.findViewById<ImageView>(R.id.iv_Dev)
        ivDev.setImageResource(R.drawable.developer_icon)

        cvEnterprise.setOnClickListener {
            if (!empresaSelected) {
                ivEnterprise.drawable.setTint(Color.parseColor("#45B7A8"))
                tvEnterprise.setTextColor(Color.parseColor("#45B7A8"))
                applyZoomAnimation(ivEnterprise, true)
                applyZoomAnimation(tvEnterprise, true)

                if (devSelected) {
                    ivDev.drawable.setTint(Color.parseColor("#6B74B4"))
                    tvDev.setTextColor(Color.parseColor("#6B74B4"))
                    applyZoomAnimation(ivDev, false)
                    applyZoomAnimation(tvDev, false)
                    devSelected = false
                }
                empresaSelected = true
            }
        }

        cvDev.setOnClickListener {
            if (!devSelected) {
                ivDev.drawable.setTint(Color.parseColor("#45B7A8"))
                tvDev.setTextColor(Color.parseColor("#45B7A8"))
                applyZoomAnimation(ivDev, true)
                applyZoomAnimation(tvDev, true)

                if (empresaSelected) {
                    ivEnterprise.drawable.setTint(Color.parseColor("#6B74B4"))
                    tvEnterprise.setTextColor(Color.parseColor("#6B74B4"))
                    applyZoomAnimation(ivEnterprise, false)
                    applyZoomAnimation(tvEnterprise, false)
                    empresaSelected = false
                }
                devSelected = true
            }
        }

        val llVolver = rootView.findViewById<LinearLayout>(R.id.ll_Volver)
        llVolver.setOnClickListener {
            replaceFragment(LogInFragment())
        }

        val btNext = rootView.findViewById<Button>(R.id.bt_Next)
        btNext.setOnClickListener {
            if (empresaSelected || devSelected) {
                val registerFragment = RegisterFragment()
                val bundle = Bundle()
                if (empresaSelected) {
                    bundle.putString("userRole", "ROLE_ENTERPRISE")
                } else if (devSelected) {
                    bundle.putString("userRole", "ROLE_DEVELOPER")
                }
                registerFragment.arguments = bundle
                replaceFragment(registerFragment)
            } else {
                Toast.makeText(requireContext(), "Por favor, seleccione un rol", Toast.LENGTH_SHORT)
                    .show()
            }
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

    private fun applyZoomAnimation(targetView: View, isZoomIn: Boolean) {
        val scaleX = ObjectAnimator.ofFloat(
            targetView,
            "scaleX",
            if (isZoomIn) 1.0f else 1.1f,
            if (isZoomIn) 1.1f else 1.0f
        )
        val scaleY = ObjectAnimator.ofFloat(
            targetView,
            "scaleY",
            if (isZoomIn) 1.0f else 1.1f,
            if (isZoomIn) 1.1f else 1.0f
        )

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY)
        animatorSet.duration = 300
        animatorSet.interpolator = DecelerateInterpolator()
        animatorSet.start()

        // Forzar el rediseño
        targetView.invalidate()
        targetView.requestLayout()
    }

}