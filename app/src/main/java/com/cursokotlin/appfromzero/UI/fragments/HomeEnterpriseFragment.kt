package com.cursokotlin.appfromzero.UI.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.adapters.ProjectCardAdapter
import com.cursokotlin.appfromzero.data.remote.RetrofitClient
import com.cursokotlin.appfromzero.data.repository.enterprise.EnterpriseRepository
import com.cursokotlin.appfromzero.data.repository.project.ProjectRepository
import com.cursokotlin.appfromzero.models.Enterprise
import com.cursokotlin.appfromzero.models.ProjectCard
import com.cursokotlin.appfromzero.models.ProjectState
import com.cursokotlin.appfromzero.models.profile.EnterpriseProfileResponse
import com.cursokotlin.appfromzero.models.profile.UpdateEnterpriseProfileRequest
import com.cursokotlin.appfromzero.models.project.Candidate
import com.cursokotlin.appfromzero.models.project.Project
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response

class HomeEnterpriseFragment : Fragment(), ApplicantsFragment.OnDeveloperSelectedListener {

    private val enterpriseRepository = EnterpriseRepository(RetrofitClient.enterpriseService)
    private val projectRepository = ProjectRepository(RetrofitClient.projectService)

    private var enterprise: Enterprise? = null
    private lateinit var recyclerView: RecyclerView
    private lateinit var cvCardEmpty: LinearLayout
    private lateinit var adapter: ProjectCardAdapter
    private var projectList: List<ProjectCard> = emptyList()

    private lateinit var emptyView: LinearLayout
    private lateinit var btnCreateProject: Button

    private lateinit var projects: List<Project>

    private lateinit var cvHomeEnterpriseProfile: CardView

    private lateinit var ivEditProfile: ImageView
    private lateinit var ivEditProfileWebSite: ImageView
    private lateinit var etEnterpriseWebsite: TextInputEditText
    private lateinit var ivEditProfileSector: ImageView
    private lateinit var etEnterpriseSector: TextInputEditText
    private lateinit var ivEditProfileDescription: ImageView
    private lateinit var etEnterpriseDescription: TextInputEditText
    private lateinit var ivEditProfilePhone: ImageView
    private lateinit var etEnterprisePhone: TextInputEditText
    private lateinit var ivConfirmEditProfile: ImageView

    private lateinit var ivProfile: ImageView
    private lateinit var tvEnterpriseWebsite: TextView
    private lateinit var tvEnterpriseName: TextView
    private lateinit var tvEnterpriseSector: TextView
    private lateinit var tvEnterpriseRUC: TextView
    private lateinit var tvEnterpriseDescription: TextView
    private lateinit var tvEnterpriseCellphone: TextView

    private lateinit var llExtending: LinearLayout


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_enterprise, container, false)

        emptyView = view.findViewById(R.id.emptyView)
        btnCreateProject = view.findViewById(R.id.btnCreateProject)

        btnCreateProject.setOnClickListener {
            replaceFragment(CreateProjectFragment())
        }

        setupRecyclerView(view)

        val sharedPreferences =
            requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getLong("userId", 0)
        val token = sharedPreferences.getString("token", null)

        initEnterpriseView(view, userId, token, "ROLE_ENTERPRISE")

        return view
    }

    private fun setRecyclerViewContraints(view: View, cvHomeEnterpriseProfile: Int) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvProjects)
        val constraintLayout = view.findViewById<ConstraintLayout>(R.id.clHomeUI)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        constraintSet.connect(recyclerView.id, ConstraintSet.TOP, cvHomeEnterpriseProfile, ConstraintSet.BOTTOM, 15)
        constraintSet.applyTo(constraintLayout)
    }

    private fun initEnterpriseView(view: View, userId: Long, token: String?, userRole: String) {
        cvHomeEnterpriseProfile = view.findViewById(R.id.cvHomeEnterpriseProfile)
        cvHomeEnterpriseProfile.visibility = View.VISIBLE

        fetchData(userId, token, userRole, view)
        setRecyclerViewContraints(view, R.id.cvHomeEnterpriseProfile)
        initEnterpriseComponent(view)

        setUpClickListener(view)
        setupTouchListener(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchListener(view: View) {
        val flContainer: FrameLayout = view.findViewById(R.id.flContainer)
        val cvHomeProfile: CardView = view.findViewById(R.id.cvHomeEnterpriseProfile)

        flContainer.setOnTouchListener { _, _ ->
            if (llExtending.visibility == View.VISIBLE) {
                animateViewVisibility(llExtending, View.GONE)
                ivEditProfileSector.visibility = View.GONE
                ivEditProfileWebSite.visibility = View.GONE
                ivEditProfile.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
            }
            false
        }

        cvHomeProfile.setOnTouchListener { _, _ -> true }
    }

    override fun onDeveloperSelected(developer: Candidate) {
        Toast.makeText(context, "Seleccionaste a ${developer.firstName} ${developer.lastName}", Toast.LENGTH_SHORT).show()
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.rvProjects)
        recyclerView.layoutManager = LinearLayoutManager(context)

        Log.d("SetupRecyclerView", "asdasd" + this.projectList.size)

        adapter = ProjectCardAdapter(this.projectList, object : ProjectCardAdapter.OnItemClickListener {
            override fun onItemClick(projectCard: ProjectCard) {
                when (projectCard.projectState) {
                    ProjectState.BUSQUEDA_DEVELOPER -> {
                        val dialog = ApplicantsFragment()
                        dialog.setDeveloperList(projectCard.candidateList)
                        dialog.setProjectId(projectCard.idProject)
                        dialog.setOnDeveloperSelectedListener(this@HomeEnterpriseFragment)
                        dialog.show(parentFragmentManager, "ApplicantsDialog")
                    }
                    ProjectState.EN_PROGRESO -> {
                        replaceFragmentViewProject(ViewProjectFragment(),projectCard.idProject, true)
                    }
                    ProjectState.FINALIZADO -> {
                        Toast.makeText(context, "Revisando el proyecto ${projectCard.projectName}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        recyclerView.adapter = adapter
        Log.d("SetupRecyclerView", "RecyclerView and Adapter initialized")
    }

    private fun fetchData(userId: Long, token: String?, userRole: String, view: View) {
        if (token != null) {
            if (userRole == "ROLE_ENTERPRISE") {
                val call = enterpriseRepository.getDeveloperByUserId(userId, token)
                call.enqueue(object : retrofit2.Callback<EnterpriseProfileResponse> {
                    override fun onResponse(call: Call<EnterpriseProfileResponse>, response: Response<EnterpriseProfileResponse>) {
                        if (response.isSuccessful) {
                            val enterpriseData = response.body()
                            enterprise = Enterprise(
                                enterpriseData?.enterpriseName ?: "",
                                enterpriseData?.website ?: "",
                                enterpriseData?.profileImgUrl ?: "",
                                enterpriseData?.description ?: "",
                                enterpriseData?.sector ?: "",
                                enterpriseData?.ruc ?: "",
                                enterpriseData?.phone ?: ""
                            )
                            bindDataToViews(role = "empresa")
                        } else {
                            Toast.makeText(requireContext(), "Error al obtener los datos", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<EnterpriseProfileResponse>, t: Throwable) {
                        Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })

                val projectCall = projectRepository.getProjectsByEnterpriseUserId(userId, token)
                projectCall.enqueue(object : retrofit2.Callback<List<Project>> {
                    override fun onResponse(call: Call<List<Project>>, response: Response<List<Project>>) {
                        if (response.isSuccessful) {
                            projects = response.body() ?: emptyList()
                            bindProjectsToViews()
                            setupRecyclerView(view)
                        } else {
                            Toast.makeText(requireContext(), "Error al obtener los proyectos", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<List<Project>>, t: Throwable) {
                        Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(requireContext(), "No se encontró el rol del usuario", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "No se encontró el token", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initEnterpriseComponent(view: View) {
        ivProfile = view.findViewById(R.id.ivProfilePhoto)

        tvEnterpriseWebsite = view.findViewById(R.id.tvEnterpriseWebSite)
        etEnterpriseWebsite = view.findViewById(R.id.etEnterpriseWebsite)
        tvEnterpriseName = view.findViewById(R.id.tvEnterpriseName)
        tvEnterpriseSector = view.findViewById(R.id.tvEnterpriseSector)
        etEnterpriseSector = view.findViewById(R.id.etEnterpriseSector)
        tvEnterpriseRUC = view.findViewById(R.id.tvEnterpriseRUC)
        tvEnterpriseDescription = view.findViewById(R.id.tvEnterpriseDescription)
        etEnterpriseDescription = view.findViewById(R.id.etEnterpriseDescription)
        tvEnterpriseCellphone = view.findViewById(R.id.tvEnterprisePhone)
        etEnterprisePhone = view.findViewById(R.id.etEnterprisePhone)

        ivEditProfileWebSite = view.findViewById(R.id.ivEditProfileWebSite)
        ivEditProfileSector = view.findViewById(R.id.ivEditProfileSector)
        ivEditProfileDescription = view.findViewById(R.id.ivEditProfileDescription)
        ivEditProfilePhone = view.findViewById(R.id.ivEditProfilePhone)

        ivEditProfile = view.findViewById(R.id.ivEditProfile)
        llExtending = view.findViewById(R.id.llExtending)
        ivConfirmEditProfile = view.findViewById(R.id.ivConfirmEditProfile)

        cvCardEmpty = view.findViewById(R.id.cvCardEmpty)

        setupEditToggle(ivEditProfileWebSite, tvEnterpriseWebsite, etEnterpriseWebsite)
        setupEditToggle(ivEditProfileSector, tvEnterpriseSector, etEnterpriseSector)
        setupEditToggle(ivEditProfileDescription, tvEnterpriseDescription, etEnterpriseDescription)
        setupEditToggle(ivEditProfilePhone, tvEnterpriseCellphone, etEnterprisePhone)

        bindDataToViews(role = "empresa")
    }

    private fun bindDataToViews(role: String) {
        if (role == "empresa") {
            enterprise?.let {
                Picasso.get()
                    .load(it.pictureUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(ivProfile)
                tvEnterpriseWebsite.text = it.website
                tvEnterpriseName.text = it.name
                tvEnterpriseSector.text = it.field
                tvEnterpriseRUC.text = it.socialRazon
                tvEnterpriseDescription.text = it.description
                tvEnterpriseCellphone.text = it.cellphone
            }
        }
    }

    private fun bindProjectsToViews() {
        Log.d("BindProjects", "Binding ${projects.size} projects to views")
        this.projectList = projects.map { project ->
            ProjectCard(
                idProject = project.id,
                projectName = project.name,
                numPostulantes = project.candidatesList.size,
                enterpriseName = enterprise?.name ?: "",
                pictureUrl = enterprise?.pictureUrl ?: "",
                projectState = when (project.state) {
                    "En busqueda" -> ProjectState.BUSQUEDA_DEVELOPER
                    "En progreso" -> ProjectState.EN_PROGRESO
                    "Finalizado" -> ProjectState.FINALIZADO
                    else -> ProjectState.BUSQUEDA_DEVELOPER
                },
                projectProgress = project.progress,
                candidateList = project.candidatesList
            )
        }

        Log.d("BindProjects", "Project list size: ${projectList.size}")

        if (!::adapter.isInitialized) {
            adapter = ProjectCardAdapter(this.projectList, object : ProjectCardAdapter.OnItemClickListener {
                override fun onItemClick(projectCard: ProjectCard) {
                    when (projectCard.projectState) {
                        ProjectState.BUSQUEDA_DEVELOPER -> {
                            Toast.makeText(context, "Postulando a ${projectCard.projectName}", Toast.LENGTH_SHORT).show()
                        }
                        ProjectState.EN_PROGRESO -> {
                            replaceFragmentViewProject(ViewProjectFragment(),projectCard.idProject, true)
                        }
                        ProjectState.FINALIZADO -> {
                            Toast.makeText(context, "Revisando el proyecto ${projectCard.projectName}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
            recyclerView.adapter = adapter
        }

        if (projectList.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
            setEmptyViewConstraints(requireView(), R.id.cvHomeEnterpriseProfile)
            cvCardEmpty.visibility = View.GONE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
            adapter.notifyDataSetChanged()
        }
    }

    private fun setEmptyViewConstraints(view: View, cvHomeEnterpriseProfile: Int) {
        val emptyView = view.findViewById<LinearLayout>(R.id.emptyView)
        val constraintLayout = view.findViewById<ConstraintLayout>(R.id.clHomeUI)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        constraintSet.connect(emptyView.id, ConstraintSet.TOP, cvHomeEnterpriseProfile, ConstraintSet.BOTTOM, 15)
        constraintSet.applyTo(constraintLayout)
    }

    private fun setupEditToggle(
        editButton: ImageView,
        textView: TextView,
        editText: TextInputEditText
    ) {
        editButton.setOnClickListener {
            if (textView.visibility == View.VISIBLE) {
                textView.visibility = View.GONE
                editText.visibility = View.VISIBLE
                editText.setText(textView.text)
            } else {
                textView.visibility = View.VISIBLE
                editText.visibility = View.GONE
                textView.text = editText.text
            }
        }
    }

    private fun setUpClickListener(view: View) {
        ivEditProfile.setOnClickListener {
            if (llExtending.visibility == View.GONE) {
                recyclerView.visibility = View.GONE
                ivEditProfile.visibility = View.GONE
                ivEditProfileWebSite.visibility = View.VISIBLE
                ivEditProfileSector.visibility = View.VISIBLE
                animateViewVisibility(llExtending, View.VISIBLE)
            } else {
                llExtending.visibility = View.GONE
                ivEditProfile.visibility = View.VISIBLE
                ivEditProfileSector.visibility = View.GONE
                ivEditProfileWebSite.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

        ivConfirmEditProfile.setOnClickListener {
            updateProfile()
            animateViewVisibility(llExtending, View.GONE)
            ivEditProfile.visibility = View.VISIBLE
            ivEditProfileSector.visibility = View.GONE
            ivEditProfileWebSite.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE

            resetEditMode()
        }
    }

    private fun resetEditMode() {
        val editTextViews = listOf(
            etEnterpriseWebsite,
            etEnterpriseSector,
            etEnterpriseDescription,
            etEnterprisePhone
        )
        val textViews = listOf(
            tvEnterpriseWebsite,
            tvEnterpriseSector,
            tvEnterpriseDescription,
            tvEnterpriseCellphone
        )

        for (i in editTextViews.indices) {
            editTextViews[i].visibility = View.GONE
            textViews[i].visibility = View.VISIBLE
        }
    }

    private fun updateProfile() {
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getLong("userId", 0)
        val token = sharedPreferences.getString("token", null)

        if (token != null && enterprise != null) {
            val updateRequest = UpdateEnterpriseProfileRequest(
                enterpriseName = etEnterpriseWebsite.text.toString(),
                description = etEnterpriseDescription.text.toString(),
                country = "Perú",
                ruc = enterprise!!.socialRazon,
                phone = etEnterprisePhone.text.toString(),
                website = etEnterpriseWebsite.text.toString(),
                profileImgUrl = enterprise!!.pictureUrl,
                sector = etEnterpriseSector.text.toString()
            )

            val call = enterpriseRepository.updateEnterpriseProfile(userId, updateRequest, token)
            call.enqueue(object : retrofit2.Callback<EnterpriseProfileResponse> {
                override fun onResponse(call: Call<EnterpriseProfileResponse>, response: Response<EnterpriseProfileResponse>) {
                    if (response.isSuccessful) {
                        val updatedEnterprise = response.body()
                        if (updatedEnterprise != null) {
                            enterprise = Enterprise(
                                updatedEnterprise.enterpriseName,
                                updatedEnterprise.website,
                                updatedEnterprise.profileImgUrl,
                                updatedEnterprise.description,
                                updatedEnterprise.sector,
                                updatedEnterprise.ruc,
                                updatedEnterprise.phone
                            )
                            bindDataToViews(role = "empresa")
                            Toast.makeText(requireContext(), "Perfil actualizado con éxito", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Error al actualizar el perfil", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<EnterpriseProfileResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(requireContext(), "Token no encontrado o datos de empresa no disponibles", Toast.LENGTH_SHORT).show()
        }
    }

    private fun animateViewVisibility(
        view: View,
        visibility: Int,
        onAnimationEnd: (() -> Unit)? = null
    ) {
        if (visibility == View.VISIBLE) {
            view.alpha = 0f
            view.visibility = View.VISIBLE
            view.animate()
                .alpha(1f)
                .setDuration(900)
                .withEndAction {
                    onAnimationEnd?.invoke()
                }
        } else {
            view.animate()
                .alpha(0f)
                .setDuration(500)
                .withEndAction {
                    view.visibility = View.GONE
                    onAnimationEnd?.invoke()
                }
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.replace(R.id.fragmenContainer, fragment)
        transaction.addToBackStack("principal")
        transaction.commit()
    }

    private fun replaceFragmentViewProject(fragment: Fragment, idProject: Long, isWorking: Boolean) {
        val bundle = Bundle().apply {
            putLong("idProject", idProject)
            putBoolean("isWorking", isWorking)
        }
        fragment.arguments = bundle

        val transaction = parentFragmentManager.beginTransaction()
        transaction.setReorderingAllowed(true)
        transaction.replace(R.id.fragmenContainer, fragment)
        transaction.addToBackStack("principal")
        transaction.commit()
    }
}
