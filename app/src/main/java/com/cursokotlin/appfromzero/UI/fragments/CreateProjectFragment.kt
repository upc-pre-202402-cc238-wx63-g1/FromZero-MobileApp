package com.cursokotlin.appfromzero.UI.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.models.Project
import com.google.android.material.textfield.TextInputEditText

class CreateProjectFragment : Fragment() {

    private lateinit var createProjectDialog: Dialog
    private lateinit var btConfirmCreateProject: Button

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

        // Inicializa los diálogos ANTES de asignar los botones
        setupCreateProjectDialog()

        btCreateProject.setOnClickListener {
            try {
                val title = etTitle.text.toString()
                val description = etDescription.text.toString()
                val languages = etLanguages.text.toString()
                val frameworks = etFrameworks.text.toString()
                val presupuesto = etPresupuesto.text.toString()
                val procesos = etProcesos.text.toString()

                if (title.isEmpty() || description.isEmpty() || languages.isEmpty() ||
                    frameworks.isEmpty() || presupuesto.isEmpty() || procesos.isEmpty()
                ) {
                    throw IllegalArgumentException("Todos los campos tienen que estar llenos")
                }

                createProjectDialog.show()

                btConfirmCreateProject.setOnClickListener {
                    val project = Project(
                        title = title,
                        description = description,
                        technologies = languages,
                        frameworks = frameworks,
                        budget = presupuesto,
                        processes = procesos
                    )

                    Toast.makeText(
                        context, "Proyecto creado correctamente", Toast.LENGTH_SHORT
                    ).show()
                    createProjectDialog.dismiss()
                }

            } catch (e: IllegalArgumentException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun setupCreateProjectDialog() {
        createProjectDialog = Dialog(requireContext())
        createProjectDialog.setContentView(R.layout.create_project_dialog)
        createProjectDialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_dialog_background)
        )
        createProjectDialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        createProjectDialog.setCancelable(true)

        btConfirmCreateProject = createProjectDialog.findViewById(R.id.btn_aceptar)
    }
}
