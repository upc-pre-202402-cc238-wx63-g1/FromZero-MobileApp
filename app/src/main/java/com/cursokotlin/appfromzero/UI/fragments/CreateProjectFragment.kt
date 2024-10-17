package com.cursokotlin.appfromzero.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.models.Project
import com.google.android.material.textfield.TextInputEditText


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateProjectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateProjectFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_project, container, false)

        val btCreateProject = view.findViewById<Button>(R.id.btCreateProject)
        val etTitle = view.findViewById<TextInputEditText>(R.id.etTitle)
        val etDescription = view.findViewById<TextInputEditText>(R.id.etDescription)
        val etLanguages = view.findViewById<TextInputEditText>(R.id.etLanguages)
        val etFrameworks = view.findViewById<TextInputEditText>(R.id.etFrameworks)
        val etPresupuesto = view.findViewById<TextInputEditText>(R.id.etPresupuesto)
        val etProcesos = view.findViewById<TextInputEditText>(R.id.etProcesos)

        btCreateProject.setOnClickListener {
            try {
                val title = etTitle.text.toString()
                val description = etDescription.text.toString()
                val languages = etLanguages.text.toString()
                val frameworks = etFrameworks.text.toString()
                val presupuesto = etPresupuesto.text.toString()
                val procesos = etProcesos.text.toString()

                if (title.isEmpty() || description.isEmpty() || languages.isEmpty() || frameworks.isEmpty() || presupuesto.isEmpty() || procesos.isEmpty()) {
                    throw IllegalArgumentException("Todos los campos tienen que estar llenos")
                }

                val project = Project(
                    title = title,
                    description = description,
                    technologies = languages,
                    frameworks = frameworks,
                    budget = presupuesto,
                    processes = procesos
                )
                Toast.makeText(context, "Proyecto creado correctamente", Toast.LENGTH_SHORT).show()
                // Use the project instance as needed

            } catch (e: IllegalArgumentException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        return view
}

}