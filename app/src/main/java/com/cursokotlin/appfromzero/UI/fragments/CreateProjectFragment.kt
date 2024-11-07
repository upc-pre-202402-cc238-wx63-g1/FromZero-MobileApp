package com.cursokotlin.appfromzero.UI.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.data.remote.RetrofitClient
import com.cursokotlin.appfromzero.models.project.ProjectResponse
import com.google.android.material.textfield.TextInputEditText
import com.cursokotlin.appfromzero.data.repository.project.ProjectRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateProjectFragment : Fragment() {

    private lateinit var createProjectDialog: Dialog
    private lateinit var btConfirmCreateProject: Button
    private val projectRepository = ProjectRepository(RetrofitClient.projectService)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val sharedPreferences =
            requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")

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

                val languages = mutableListOf<String>()
                languages.add(etLanguages.text.toString())
                val frameworks = mutableListOf<String>()
                frameworks.add(etFrameworks.text.toString())

                val presupuesto = etPresupuesto.text.toString()
                val procesos = etProcesos.text.toString()

                if (title.isEmpty() || description.isEmpty() || languages.isEmpty() ||
                    frameworks.isEmpty() || presupuesto.isEmpty() || procesos.isEmpty()
                ) {
                    throw IllegalArgumentException("Todos los campos tienen que estar llenos")
                }

                createProjectDialog.show()

                btConfirmCreateProject.setOnClickListener {
                    val project = ProjectResponse(
                        name = title,
                        description = description,
                        ownerId = 1,
                        languages = languages,
                        frameworks = frameworks,
                        budget = presupuesto,
                        type = "Desarrollo",
                        methodologies = procesos
                    )

                    val call = projectRepository.createProject(project, token!!)
                    call.enqueue(object : Callback<ProjectResponse> {
                        override fun onResponse(
                            call: Call<ProjectResponse>,
                            response: Response<ProjectResponse>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(
                                    context, "Proyecto creado correctamente", Toast.LENGTH_SHORT
                                ).show()
                                createProjectDialog.dismiss()
                            } else {
                                Toast.makeText(
                                    context, "Error al crear el proyecto", Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onFailure(call: Call<ProjectResponse>, t: Throwable) {
                            Toast.makeText(
                                context, "Error al crear el proyecto", Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
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
