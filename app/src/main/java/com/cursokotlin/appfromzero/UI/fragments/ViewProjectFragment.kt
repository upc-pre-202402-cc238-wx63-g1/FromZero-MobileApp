package com.cursokotlin.appfromzero.UI.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.ProjectData
import com.cursokotlin.appfromzero.adapters.ProjectDataAdapter
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.models.HomeViewModel
import com.cursokotlin.appfromzero.models.Project

class ViewProjectFragment : Fragment() {
    private val homeViewModel: HomeViewModel by activityViewModels()

    private var isDeveloper: Boolean = true

    private lateinit var applyProjectDialog: Dialog
    private lateinit var btnConfirmApplyProject: Button
    lateinit var projectData: List<ProjectData>
    lateinit var projectDataAdapter: ProjectDataAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_project, container, false)
        val tvProjectName = view.findViewById<TextView>(R.id.tvProjectName)
        val btDeliverables = view.findViewById<Button>(R.id.btDeliverables)

        homeViewModel.userRole.observe(viewLifecycleOwner) { role ->
            isDeveloper = role == "desarrollador"
        }

        // Inicializa los diálogos ANTES de asignar los botones
        setupApplyProjectDialog()
        loadDescription(view)

        // Cambiar texto del botón según si es developer
        btDeliverables.text = if (isDeveloper) "Postular" else "Entregables"

        btDeliverables.setOnClickListener {
            if (isDeveloper) {

                applyProjectDialog.show()

                btnConfirmApplyProject.setOnClickListener {
                    Toast.makeText(context, "Postulación enviada", Toast.LENGTH_SHORT).show()
                    applyProjectDialog.dismiss()
                }
            } else {
                // Navigate to DeliverablesFragment
                replaceFragment(DeliverablesFragment())
            }
        }
        return view
    }

    private fun setupApplyProjectDialog() {
        applyProjectDialog = Dialog(requireContext())
        applyProjectDialog.setContentView(R.layout.apply_project_dialog)
        applyProjectDialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_dialog_background)
        )
        applyProjectDialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        applyProjectDialog.setCancelable(true)

        btnConfirmApplyProject = applyProjectDialog.findViewById(R.id.btn_postular)
    }

    private fun loadDescription(view: View) {
        projectData = listOf(
            ProjectData(
                "Descripción",
                "La Plataforma de Comercio Electrónico Geekit es un proyecto destinado a crear una experiencia de compra en línea excepcional para nuestra marca de ropa y accesorios para jóvenes apasionados por la cultura geek. La plataforma debe ofrecer una navegación intuitiva, una interfaz atractiva y funcionalidades que mejoren la experiencia del usuario, desde la búsqueda de productos hasta el proceso de compra y seguimiento de pedidos"
            ),
            ProjectData(
                "Tecnologías / Lenguajes", "HTML5\n" +
                        "CSS3\n" +
                        "JavaScrip"
            ),
            ProjectData(
                "Tecnologías / Frameworks", "React.js (Frontend)\n" +
                        "Node.js (Backend)\n" +
                        "Express.js (Backend)"
            ),
            ProjectData(
                "Presupuesto", "\$50,000\n" +
                        "\n" +
                        "El presupuesto asignado para este proyecto es de \$50,000 USD, incluyendo el costo de desarrollo, pruebas, implementación y mantenimiento inicial durante los primeros seis meses."
            ),
            ProjectData(
                "Procesos y Metodologías de Desarrollo",
                "1. Recolección de Requisitos: Definición de requisitos del proyecto en una reunión inicial.\n" +
                        "2. Desarrollo Iterativo: Metodología ágil con entregas incrementales para retroalimentación temprana.\n" +
                        "3. Diseño de UI/UX: Creación de prototipos de interfaz centrados en usabilidad y estética.\n" +
                        "4. Desarrollo Frontend y Backend: Implementación de frontend y backend con código limpio y modular.\n" +
                        "5. Pruebas y Control de Calidad: Evaluación exhaustiva en todas las etapas para garantizar calidad.\n" +
                        "6. Implementación y Despliegue: Lanzamiento en entorno de producción tras completar desarrollo y pruebas.\n" +
                        "7. Mantenimiento y Soporte: Ofrecimiento de soporte continuo, actualizaciones y monitoreo post-lanzamiento."
            )
        )
        val rvProject = view.findViewById<RecyclerView>(R.id.rvProjectDescription)
        projectDataAdapter = ProjectDataAdapter(projectData)
        rvProject.layoutManager = LinearLayoutManager(context)
        rvProject.adapter = projectDataAdapter

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