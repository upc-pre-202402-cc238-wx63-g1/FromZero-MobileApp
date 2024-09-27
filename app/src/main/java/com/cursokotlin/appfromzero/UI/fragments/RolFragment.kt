package com.cursokotlin.appfromzero.UI.fragments
import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.view.animation.DecelerateInterpolator
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.cursokotlin.appfromzero.R

/**
 * A simple [Fragment] subclass.
 * Use the [RolFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RolFragment : Fragment() {
    var devSelected = false
    var empresaSelected = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView: View = inflater.inflate(R.layout.fragment_rol, container, false)

        val tv_Empresa = rootView.findViewById<TextView>(R.id.tv_Empresa)
        val tv_Dev = rootView.findViewById<TextView>(R.id.tv_Dev)

        var selectedCard: CardView? = null

        val cv_Empresa = rootView.findViewById<CardView>(R.id.cv_Empresa)
        val iv_Empresa = rootView.findViewById<ImageView>(R.id.iv_empresa)
        iv_Empresa.setImageResource(R.drawable.ic_baul)

        val cv_Dev = rootView.findViewById<CardView>(R.id.cv_Dev)
        val iv_Dev = rootView.findViewById<ImageView>(R.id.iv_Dev)
        iv_Dev.setImageResource(R.drawable.ic_dev_person)


        // Lógica de selección y animación para los CardView
        cv_Empresa.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                // Si ya está seleccionado, no hacemos nada
                if (!empresaSelected) {
                    // Cambia el color del ícono y el texto
                    iv_Empresa.drawable.setTint(Color.parseColor("#45B7A8"))
                    tv_Empresa.setTextColor(Color.parseColor("#45B7A8"))

                    // Aplica animación de zoom a ic_baul y tv_empresa
                    applyZoomAnimation(iv_Empresa, true)
                    applyZoomAnimation(tv_Empresa, true)

                    // Si el otro CardView está seleccionado, revierte su color y tamaño
                    if (devSelected) {
                        iv_Dev.drawable.setTint(Color.parseColor("#6B74B4"))
                        tv_Dev.setTextColor(Color.parseColor("#6B74B4"))
                        applyZoomAnimation(iv_Dev, false)
                        applyZoomAnimation(tv_Dev, false)
                        devSelected = false
                    }
                    empresaSelected = true
                }
            }
            false
        }

        cv_Dev.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                // Si ya está seleccionado, no hacemos nada
                if (!devSelected) {
                    // Cambia el color del ícono y el texto
                    iv_Dev.drawable.setTint(Color.parseColor("#45B7A8"))
                    tv_Dev.setTextColor(Color.parseColor("#45B7A8"))

                    // Aplica animación de zoom a ic_dev y tv_dev
                    applyZoomAnimation(iv_Dev, true)
                    applyZoomAnimation(tv_Dev, true)

                    // Si el otro CardView está seleccionado, revierte su color y tamaño
                    if (empresaSelected) {
                        iv_Empresa.drawable.setTint(Color.parseColor("#6B74B4"))
                        tv_Empresa.setTextColor(Color.parseColor("#6B74B4"))
                        applyZoomAnimation(iv_Empresa, false)
                        applyZoomAnimation(tv_Empresa, false)
                        empresaSelected = false
                    }
                    devSelected = true
                }
            }
            false
        }


        val llVolver = rootView.findViewById<LinearLayout>(R.id.ll_Volver)
        llVolver.setOnClickListener{
            replaceFragment(LogInFragment())
        }

        val btNext = rootView.findViewById<Button>(R.id.bt_Next)
        btNext.setOnClickListener {
            replaceFragment(RegisterFragment())
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

    // Función para aplicar animación de zoom
    private fun applyZoomAnimation(targetView: View, isZoomIn: Boolean) {
        val scaleX = ObjectAnimator.ofFloat(targetView, "scaleX", if (isZoomIn) 1.0f else 1.2f, if (isZoomIn) 1.2f else 1.0f)
        val scaleY = ObjectAnimator.ofFloat(targetView, "scaleY", if (isZoomIn) 1.0f else 1.2f, if (isZoomIn) 1.2f else 1.0f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY)
        animatorSet.duration = 300
        animatorSet.interpolator = DecelerateInterpolator()
        animatorSet.start()
    }
}