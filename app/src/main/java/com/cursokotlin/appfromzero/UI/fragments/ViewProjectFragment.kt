package com.cursokotlin.appfromzero.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.ProjectData
import com.cursokotlin.appfromzero.ProjectDataAdapter
import com.cursokotlin.appfromzero.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewProjectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewProjectFragment : Fragment() {
    lateinit var projectData: List<ProjectData>
    lateinit var projectDataAdapter: ProjectDataAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_project, container, false)
        loadDescription(view)
        return view
        description(view)
    }

    private fun loadDescription(view: View) {
        projectData = listOf(
            ProjectData("Descripción", "La Plataforma de Comercio Electrónico Geekit es un proyecto destinado a crear una experiencia de compra en línea excepcional para nuestra marca de ropa y accesorios para jóvenes apasionados por la cultura geek. La plataforma debe ofrecer una navegación intuitiva, una interfaz atractiva y funcionalidades que mejoren la experiencia del usuario, desde la búsqueda de productos hasta el proceso de compra y seguimiento de pedidos"),
            ProjectData("Tecnologías / Lenguajes", "HTML5\n" +
                    "CSS3\n" +
                    "JavaScrip"),
            ProjectData("Tecnologías / Frameworks", "React.js (Frontend)\n" +
                    "Node.js (Backend)\n" +
                    "Express.js (Backend)"),
            ProjectData("Presupuesto", "\$50,000\n" +
                    "\n" +
                    "El presupuesto asignado para este proyecto es de \$50,000 USD, incluyendo el costo de desarrollo, pruebas, implementación y mantenimiento inicial durante los primeros seis meses."),
            ProjectData("Procesos y Metodologías de Desarrollo", "1. Recolección de Requisitos: Definición de requisitos del proyecto en una reunión inicial.\n" +
                    "2. Desarrollo Iterativo: Metodología ágil con entregas incrementales para retroalimentación temprana.\n" +
                    "3. Diseño de UI/UX: Creación de prototipos de interfaz centrados en usabilidad y estética.\n" +
                    "4. Desarrollo Frontend y Backend: Implementación de frontend y backend con código limpio y modular.\n" +
                    "5. Pruebas y Control de Calidad: Evaluación exhaustiva en todas las etapas para garantizar calidad.\n" +
                    "6. Implementación y Despliegue: Lanzamiento en entorno de producción tras completar desarrollo y pruebas.\n" +
                    "7. Mantenimiento y Soporte: Ofrecimiento de soporte continuo, actualizaciones y monitoreo post-lanzamiento.")
        )
        val rvProject = view.findViewById<RecyclerView>(R.id.rvProjectDescription)
        projectDataAdapter = ProjectDataAdapter(projectData)
        rvProject.layoutManager = LinearLayoutManager(context)
        rvProject.adapter = projectDataAdapter

    }
}