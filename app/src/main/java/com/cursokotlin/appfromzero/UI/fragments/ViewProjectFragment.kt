package com.cursokotlin.appfromzero.UI.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.cursokotlin.appfromzero.MainActivity
import com.cursokotlin.appfromzero.ProjectData
import com.cursokotlin.appfromzero.adapters.ProjectDataAdapter
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.data.remote.RetrofitClient
import com.cursokotlin.appfromzero.models.HomeViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.cursokotlin.appfromzero.data.repository.project.ProjectRepository
import com.cursokotlin.appfromzero.models.project.ProjectProfileResponse
import com.cursokotlin.appfromzero.models.project.Project
import okhttp3.ResponseBody


class ViewProjectFragment : Fragment() {
    //private val homeViewModel: HomeViewModel by activityViewModels()

    private var isWorking: Boolean = false
    private var idProject: Long = 0
    private var isFinished: Boolean = false
    private val projectRepository = ProjectRepository(RetrofitClient.projectService)

    private lateinit var applyProjectDialog: Dialog
    private lateinit var btnConfirmApplyProject: Button
    private lateinit var deleteProjectDialog: Dialog
    private lateinit var btnConfirmDeleteProject: Button
    private lateinit var btnCancelDeleteProject: Button
    private var projectData: List<ProjectData> = emptyList()
    lateinit var projectDataAdapter: ProjectDataAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_project, container, false)
        val btDeliverables = view.findViewById<Button>(R.id.btDeliverables)
        val btDeleteProject = view.findViewById<Button>(R.id.btDeleteProject)

        val sharedPreferences =
            requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", "")
        val userId = sharedPreferences.getLong("userId", 0)

        arguments?.let {
            idProject = it.getLong("idProject")
            isWorking = it.getBoolean("isWorking", false)
            isFinished = it.getBoolean("isFinished", false)
        }

        // Inicializa los diálogos ANTES de asignar los botones
        setupApplyProjectDialog()
        setupDeleteProjectDialog()
        loadDescription(view, idProject, token)

        // Cambiar texto del botón según si es developer
        btDeliverables.text = if (isWorking) "Entregables" else "Postular"

        // Cambiar visibilidad del botón de eliminar proyecto
        btDeleteProject.visibility = if (isFinished) View.VISIBLE else View.GONE

        btDeliverables.setOnClickListener {
            if (isWorking) {
                // Navigate to DeliverablesFragment
                replaceFragment(DeliverablesFragment())
            } else {

                applyProjectDialog.show()


                btnConfirmApplyProject.setOnClickListener {
                    val call = projectRepository.addCandidateToProject(idProject, userId, token!!)
                    call.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            //Log.d("ViewProjectFragment", "response: ${response.body()}")
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Postulación enviada", Toast.LENGTH_SHORT)
                                    .show()
                                applyProjectDialog.dismiss()
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.fragmenContainer, HomeDeveloperFragment())
                                    .addToBackStack(null)
                                    .commit()
                                (activity as MainActivity).showHomeTab()
                            } else {
                                Toast.makeText(context, "Error al postular", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            //Log.d("ViewProjectFragment", "response: ${t.message}")
                            Toast.makeText(context, "Error al postular", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
        }

        btDeleteProject.setOnClickListener{
            deleteProjectDialog.show()

            btnConfirmDeleteProject.setOnClickListener {
                val call = projectRepository.deleteProject(idProject, token!!)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                            Toast.makeText(context, "Proyecto eliminado", Toast.LENGTH_SHORT).show()
                            deleteProjectDialog.dismiss()
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragmenContainer, HomeEnterpriseFragment())
                                .addToBackStack(null)
                                .commit()
                            (activity as MainActivity).showHomeTab()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        Toast.makeText(context, "Error al eliminar proyecto", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            btnCancelDeleteProject.setOnClickListener {
                deleteProjectDialog.dismiss()
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

    private fun setupDeleteProjectDialog() {
        deleteProjectDialog = Dialog(requireContext())
        deleteProjectDialog.setContentView(R.layout.delete_project_dialog)
        deleteProjectDialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_dialog_background)
        )
        deleteProjectDialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        deleteProjectDialog.setCancelable(true)

        btnConfirmDeleteProject = deleteProjectDialog.findViewById(R.id.btn_aceptar)
        btnCancelDeleteProject = deleteProjectDialog.findViewById(R.id.btn_cancelar)

    }

    private fun loadDescription(view: View, idProject: Long, token: String?) {
        if (token != null) {
            val call = projectRepository.getProjectById(idProject, token)
            call.enqueue(object : Callback<Project> {
                override fun onResponse(
                    call: Call<Project>,
                    response: Response<Project>
                ) {
                    if (response.isSuccessful) {
                        val project = response.body()
                        if (project != null) {
                            val tvProjectName = view.findViewById<TextView>(R.id.tvProjectName)
                            tvProjectName.text = project.name
                            projectData = listOf(
                                ProjectData(
                                    "Descripción",
                                    project.description
                                ),
                                ProjectData(
                                    "Tecnologías / Lenguajes",
                                    project.languages.joinToString { it.name }
                                ),
                                ProjectData(
                                    "Tecnologías / Frameworks",
                                    project.frameworks.joinToString { it.name }
                                ),
                                ProjectData(
                                    "Presupuesto",
                                    project.budget
                                ),
                                ProjectData(
                                    "Procesos y Metodologías de Desarrollo",
                                    project.methodologies
                                )
                            )
                            val rvProject =
                                view.findViewById<RecyclerView>(R.id.rvProjectDescription)
                            projectDataAdapter = ProjectDataAdapter(projectData)
                            rvProject.layoutManager = LinearLayoutManager(context)
                            rvProject.adapter = projectDataAdapter
                        } else {
                            Toast.makeText(context, "Proyecto nulo", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Error al obtener datos del proyecto",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Project>, t: Throwable) {
                    Toast.makeText(
                        context,
                        "Error al obtener datos del proyecto",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        } else {
            Toast.makeText(context, "No se encontró token", Toast.LENGTH_SHORT).show()
        }
    }

    // Common method to replace fragment
    private fun replaceFragment(fragment: Fragment) {
        val bundle = Bundle()
        bundle.putLong("idProject", idProject)
        fragment.arguments = bundle

        val transaction = parentFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.replace(R.id.fragmenContainer, fragment)
        transaction.addToBackStack("principal")
        transaction.commit()
    }


}