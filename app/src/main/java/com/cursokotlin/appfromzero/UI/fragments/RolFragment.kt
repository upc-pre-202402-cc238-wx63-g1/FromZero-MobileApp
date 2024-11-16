package com.cursokotlin.appfromzero.UI.fragments

import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
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

        val tv_Empresa = rootView.findViewById<TextView>(R.id.tv_Empresa)
        val tv_Dev = rootView.findViewById<TextView>(R.id.tv_Dev)

        val cv_Empresa = rootView.findViewById<CardView>(R.id.cv_Empresa)
        val iv_Empresa = rootView.findViewById<ImageView>(R.id.iv_empresa)
        iv_Empresa.setImageResource(R.drawable.ic_baul)

        val cv_Dev = rootView.findViewById<CardView>(R.id.cv_Dev)
        val iv_Dev = rootView.findViewById<ImageView>(R.id.iv_Dev)
        iv_Dev.setImageResource(R.drawable.ic_dev_person)

        cv_Empresa.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN && !empresaSelected) {
                iv_Empresa.drawable.setTint(Color.parseColor("#45B7A8"))
                tv_Empresa.setTextColor(Color.parseColor("#45B7A8"))
                applyZoomAnimation(iv_Empresa, true)
                applyZoomAnimation(tv_Empresa, true)

                if (devSelected) {
                    iv_Dev.drawable.setTint(Color.parseColor("#6B74B4"))
                    tv_Dev.setTextColor(Color.parseColor("#6B74B4"))
                    applyZoomAnimation(iv_Dev, false)
                    applyZoomAnimation(tv_Dev, false)
                    devSelected = false
                }
                empresaSelected = true
            }
            false
        }

        cv_Dev.setOnTouchListener { _, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN && !devSelected) {
                iv_Dev.drawable.setTint(Color.parseColor("#45B7A8"))
                tv_Dev.setTextColor(Color.parseColor("#45B7A8"))
                applyZoomAnimation(iv_Dev, true)
                applyZoomAnimation(tv_Dev, true)

                if (empresaSelected) {
                    iv_Empresa.drawable.setTint(Color.parseColor("#6B74B4"))
                    tv_Empresa.setTextColor(Color.parseColor("#6B74B4"))
                    applyZoomAnimation(iv_Empresa, false)
                    applyZoomAnimation(tv_Empresa, false)
                    empresaSelected = false
                }
                devSelected = true
            }
            false
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
                Toast.makeText(requireContext(), "Por favor, seleccione un rol", Toast.LENGTH_SHORT).show()
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