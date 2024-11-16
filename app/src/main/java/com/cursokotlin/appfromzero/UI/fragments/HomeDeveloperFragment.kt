package com.cursokotlin.appfromzero.UI.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.adapters.CircleTransform
import com.cursokotlin.appfromzero.adapters.ProjectCardAdapter
import com.cursokotlin.appfromzero.data.remote.RetrofitClient
import com.cursokotlin.appfromzero.data.repository.developer.DeveloperRepository
import com.cursokotlin.appfromzero.data.repository.project.ProjectRepository
import com.cursokotlin.appfromzero.models.Developer
import com.cursokotlin.appfromzero.models.project.Project
import com.cursokotlin.appfromzero.models.ProjectCard
import com.cursokotlin.appfromzero.models.ProjectState
import com.cursokotlin.appfromzero.models.profile.DeveloperProfileResponse
import com.cursokotlin.appfromzero.models.profile.UpdateDeveloperProfileRequest
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body

class HomeDeveloperFragment : Fragment() {

    private val developerRepository = DeveloperRepository(RetrofitClient.developerService)
    private val projectRepository = ProjectRepository(RetrofitClient.projectService)
    private var developer: Developer? = null

    private var projectList: List<ProjectCard> = emptyList()
    private lateinit var projects: List<Project>

    private lateinit var adapter: ProjectCardAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: LinearLayout
    private lateinit var btnCreateProject: Button
    private lateinit var ivEditDevProfile: ImageView
    private lateinit var cvCardEmpty: LinearLayout

    private lateinit var cvHomeDeveloperProfile: CardView
    private lateinit var ivProfileDevPhoto: ImageView
    private lateinit var tvDevName: TextView
    private lateinit var ivCountryIcon: ImageView
    private lateinit var ratingBar: RatingBar
    private lateinit var tvDevSpecialties: TextView
    private lateinit var etDevSpecialties: TextInputEditText
    private lateinit var ivEditDevSpecialties: ImageView
    private lateinit var tvDevDescription: TextView
    private lateinit var etDevDescription: TextInputEditText
    private lateinit var ivEditDevDescription: ImageView
    private lateinit var tvCellphone: TextView
    private lateinit var etCellphone: TextInputEditText
    private lateinit var ivEditDevCellphone: ImageView
    private lateinit var tvEmail: TextView
    private lateinit var etEmail: TextInputEditText
    private lateinit var tvDeveloperProjects: TextView
    private lateinit var llDevExtending: LinearLayout
    private lateinit var ivConfirmEditDevProfile: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_developer, container, false)

        emptyView = view.findViewById(R.id.emptyView)
        btnCreateProject = view.findViewById(R.id.btnSearchProject)

        btnCreateProject.setOnClickListener {
            replaceFragment(SearchProjectFragment())
        }

        setupRecyclerView(view)

        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val userRole = sharedPreferences.getString("userRole", null)
        val userId = sharedPreferences.getLong("userId", 0)
        val token = sharedPreferences.getString("token", null)

        initDeveloperView(view, userId, token, "ROLE_DEVELOPER")

        return view
    }

    private fun initDeveloperView(view: View, userId: Long, token: String?, userRole: String) {
        cvHomeDeveloperProfile = view.findViewById(R.id.cvHomeDeveloperProfile)
        cvHomeDeveloperProfile.visibility = View.VISIBLE

        fetchData(userId, token, userRole, view)
        setRecyclerViewContraints(view, R.id.cvHomeDeveloperProfile)
        initDeveloperComponent(view)

        setUpClickListener(view)
        setupTouchListener(view)
    }

    private fun fetchData(userId: Long, token: String?, userRole: String, view: View) {
        if (token != null) {
            if (userRole == "ROLE_DEVELOPER") {
                val call = developerRepository.getDeveloperByUserId(userId, token)
                call.enqueue(object: retrofit2.Callback<DeveloperProfileResponse>{
                    override fun onResponse(
                        call: Call<DeveloperProfileResponse>,
                        response: Response<DeveloperProfileResponse>
                    ) {
                        if ( response.isSuccessful){
                            val developerData = response.body()
                            if (developerData != null) {
                                developer = Developer(
                                    name = "${developerData.firstName} ${developerData.lastName}",
                                    rating = 0f, // Assuming rating is not provided in the response
                                    profilePic = R.drawable.placeholder, // Assuming a placeholder image resource
                                    countryFlag = R.drawable.sample_flag, // Assuming a placeholder flag resource
                                    summary = developerData.description,
                                    skills = developerData.specialties,
                                    phone = developerData.phone,
                                    email = "example@gmail.com",
                                    profileImgUrl = developerData.profileImgUrl
                                )
                                bindDataToViews(role = "developer")
                            } else {
                                Toast.makeText(requireContext(), "No se encontró el perfil del desarrollador", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<DeveloperProfileResponse>, t: Throwable) {
                        Toast.makeText(requireContext(), "Error al obtener el perfil del desarrollador", Toast.LENGTH_SHORT).show()
                    }
                })

                val projectCall = projectRepository.getProjectsByDeveloperUserId(userId, token)
                projectCall.enqueue(object: retrofit2.Callback<List<Project>> {
                    override fun onResponse(call: Call<List<Project>>, response:Response<List<Project>>){
                        if (response.isSuccessful) {
                            projects = response.body() ?: emptyList()
                            Log.d("Projects", projects.toString())
                            bindProjectsToViews(projects)
                            setupRecyclerView(view)
                        } else {
                            Toast.makeText(requireContext(), "No se encontraron proyectos", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<List<Project>>, t: Throwable) {
                        Toast.makeText(requireContext(), "Error al obtener los proyectos", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(requireContext(), "No se encontró el rol del usuario", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "No se encontró el token", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bindDataToViews(role: String) {
        if (role == "developer") {
            developer?.let {
                Picasso.get()
                    .load(it.profileImgUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .transform(CircleTransform())
                    .into(ivProfileDevPhoto)
                tvDevName.text = it.name
                ratingBar.rating = it.rating
                tvDevSpecialties.text = it.skills
                tvDevDescription.text = it.summary
                tvCellphone.text = it.phone
                tvEmail.text = it.email
                tvDeveloperProjects.text = "0"
            }
        }
    }

    private fun setupRecyclerView(view: View) {
        recyclerView = view.findViewById(R.id.rvProjectsDev)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize adapter with an empty list
        adapter = ProjectCardAdapter(projectList, object : ProjectCardAdapter.OnItemClickListener {
            override fun onItemClick(projectCard: ProjectCard) {
                when (projectCard.projectState) {
                    ProjectState.BUSQUEDA_DEVELOPER -> {
                        Toast.makeText(context, "Postulando a ${projectCard.projectName}", Toast.LENGTH_SHORT).show()
                    }
                    ProjectState.EN_PROGRESO -> {
                        replaceFragmentViewProject(ViewProjectFragment(), projectCard.idProject,true)
                    }
                    ProjectState.FINALIZADO -> {
                        Toast.makeText(context, "Revisando el proyecto ${projectCard.projectName}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        recyclerView.adapter = adapter
    }

    private fun bindProjectsToViews(projects: List<Project>) {
        this.projectList = projects.map { project ->
            ProjectCard(
                idProject = project.id,
                projectName = project.name,
                numPostulantes = project.candidatesList.size,
                enterpriseName = developer?.name ?: "",
                pictureUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Default_pfp.svg/2048px-Default_pfp.svg.png",
                projectState = when (project.state) {
                    "En busqueda" -> ProjectState.BUSQUEDA_DEVELOPER
                    "En progreso" -> ProjectState.EN_PROGRESO
                    "Finalizado" -> ProjectState.FINALIZADO
                    else -> ProjectState.BUSQUEDA_DEVELOPER
                },
                projectProgress = project.progress,
                candidateList = project.candidatesList,
            )
        }
        if (projectList.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
            setEmptyViewConstraints(requireView(), R.id.cvHomeDeveloperProfile)
            cvCardEmpty.visibility = View.GONE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
            adapter.notifyDataSetChanged()
        }
    }

    private fun initDeveloperComponent(view: View) {
        // Ensure these IDs exist in fragment_home_developer.xml
        cvHomeDeveloperProfile = view.findViewById(R.id.cvHomeDeveloperProfile)
        ivProfileDevPhoto = view.findViewById(R.id.ivProfileDevPhoto)
        tvDevName = view.findViewById(R.id.tvDevName)
        ivCountryIcon = view.findViewById(R.id.ivCountryIcon)
        ratingBar = view.findViewById(R.id.ratingBar)
        tvDevSpecialties = view.findViewById(R.id.tvDevSpecialties)
        etDevSpecialties = view.findViewById(R.id.etDevSpecialties)
        ivEditDevSpecialties = view.findViewById(R.id.ivEditProfileDevSpecialties)
        tvDevDescription = view.findViewById(R.id.tvDeveloperDescription)
        etDevDescription = view.findViewById(R.id.etDeveloperDescription)
        ivEditDevDescription = view.findViewById(R.id.ivEditProfileDevDescription)
        tvCellphone = view.findViewById(R.id.tvDeveloperPhone)
        etCellphone = view.findViewById(R.id.etDeveloperPhone)
        ivEditDevCellphone = view.findViewById(R.id.ivEditDevProfilePhone)
        tvEmail = view.findViewById(R.id.tvDeveloperMail)
        etEmail = view.findViewById(R.id.etDeveloperEmail)
        tvDeveloperProjects = view.findViewById(R.id.tvDeveloperProjects)
        llDevExtending = view.findViewById(R.id.llExtendingDeveloper)
        ivConfirmEditDevProfile = view.findViewById(R.id.ivConfirmEditDevProfile)
        ivEditDevProfile = view.findViewById(R.id.ivEditDevProfile)
        cvCardEmpty = view.findViewById(R.id.cvCardEmpty)

        setupEditToggle(ivEditDevSpecialties, tvDevSpecialties, etDevSpecialties)
        setupEditToggle(ivEditDevDescription, tvDevDescription, etDevDescription)
        setupEditToggle(ivEditDevCellphone, tvCellphone, etCellphone)

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchListener(view: View) {
        val flContainer: FrameLayout = view.findViewById(R.id.flContainer)
        val cvHomeProfile: CardView = view.findViewById(R.id.cvHomeDeveloperProfile)

        flContainer.setOnTouchListener { _, _ ->
            if (llDevExtending.visibility == View.VISIBLE) {
                animateViewVisibility(llDevExtending, View.GONE)
                ivEditDevSpecialties.visibility = View.GONE
                ivEditDevDescription.visibility = View.GONE
                ivConfirmEditDevProfile.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
            }
            false
        }

        cvHomeProfile.setOnTouchListener { _, _ -> true }
    }

    private fun setupEditToggle(
        editButton: View,
        textView: View,
        editText: TextInputEditText
    ) {
        editButton.setOnClickListener {
            if (textView.visibility == View.VISIBLE) {
                textView.visibility = View.GONE
                editText.visibility = View.VISIBLE
                editText.setText((textView as TextView).text)
            } else {
                textView.visibility = View.VISIBLE
                editText.visibility = View.GONE
                (textView as TextView).text = editText.text
            }
        }
    }

    private fun setRecyclerViewContraints(view: View, cvHomeEnterpriseProfile: Int) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvProjectsDev)
        val constraintLayout = view.findViewById<ConstraintLayout>(R.id.clHomeUIDev)
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)

        constraintSet.connect(recyclerView.id, ConstraintSet.TOP, cvHomeEnterpriseProfile, ConstraintSet.BOTTOM, 15)
        constraintSet.applyTo(constraintLayout)
    }

    private fun setEmptyViewConstraints(view: View, cvHomeDeveloperProfile: Int) {
        val emptyView = view.findViewById<LinearLayout>(R.id.emptyView)
        val constraintLayout = view.findViewById<ConstraintLayout>(R.id.clHomeUIDev)
        if (constraintLayout != null) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)
            constraintSet.connect(emptyView.id, ConstraintSet.TOP, cvHomeDeveloperProfile, ConstraintSet.BOTTOM, 15)
            constraintSet.applyTo(constraintLayout)
        } else {
            Log.e("HomeDeveloperFragment", "ConstraintLayout with ID clHomeUI not found")
        }
    }

    private fun setUpClickListener(view: View) {
        ivEditDevProfile.setOnClickListener {
            if (llDevExtending.visibility == View.GONE) {
                recyclerView.visibility = View.GONE
                ivEditDevProfile.visibility = View.GONE
                ivEditDevSpecialties.visibility = View.VISIBLE
                ivEditDevDescription.visibility = View.VISIBLE
                ivEditDevCellphone.visibility = View.VISIBLE
                animateViewVisibility(llDevExtending, View.VISIBLE)
            } else {
                llDevExtending.visibility = View.GONE
                ivEditDevProfile.visibility = View.VISIBLE
                ivEditDevSpecialties.visibility = View.GONE
                ivEditDevDescription.visibility = View.GONE
                ivEditDevCellphone.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

        ivConfirmEditDevProfile.setOnClickListener {
            updateProfile()
            bindDataToViews(role = "developer")
            animateViewVisibility(llDevExtending, View.GONE)
            ivEditDevSpecialties.visibility = View.GONE
            ivEditDevDescription.visibility = View.GONE
            ivEditDevCellphone.visibility = View.GONE
            ivEditDevProfile.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE


            resetEditMode()
        }
    }

    private fun resetEditMode() {
        val editTextViews = listOf(
            etDevSpecialties,
            etDevDescription,
            etCellphone,
            etEmail
        )
        val textViews = listOf(
            tvDevSpecialties,
            tvDevDescription,
            tvCellphone,
            tvEmail
        )

        for (i in editTextViews.indices) {
            editTextViews[i].visibility = View.GONE
            textViews[i].visibility = View.VISIBLE
        }
    }

    private fun updateProfile()  {
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getLong("userId", 0)
        val token = sharedPreferences.getString("token", null)

        if (token != null && developer != null) {
            val nameParts = developer!!.name.split(" ")
            val firstName = nameParts[0]
            val lastName = nameParts.getOrElse(1) { "" }

            val updateRequest = UpdateDeveloperProfileRequest(
                firstName = firstName,
                lastName = lastName,
                description = etDevDescription.text.toString(),
                country = developer!!.countryFlag.toString(), // Assuming countryFlag holds the country information
                phone = etCellphone.text.toString(),
                specialties = etDevSpecialties.text.toString(),
                profileImgUrl = developer!!.profilePic.toString(),
                
            )

            val call = developerRepository.updateDeveloperProfile(userId, updateRequest, token)
            call.enqueue(object : retrofit2.Callback<DeveloperProfileResponse> {
                override fun onResponse(call: Call<DeveloperProfileResponse>, response: Response<DeveloperProfileResponse>) {
                    if (response.isSuccessful) {
                        val updatedDeveloper = response.body()
                        if (updatedDeveloper != null) {
                            developer = Developer(
                                name = "${updatedDeveloper.firstName} ${updatedDeveloper.lastName}",
                                rating = developer!!.rating,
                                profilePic = developer!!.profilePic,
                                countryFlag = developer!!.countryFlag,
                                summary = updatedDeveloper.description,
                                skills = updatedDeveloper.specialties,
                                phone = updatedDeveloper.phone,
                                email = developer!!.email
                            )
                            bindDataToViews(role = "developer")
                            Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Error updating profile", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DeveloperProfileResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(requireContext(), "Token not found or developer data not available", Toast.LENGTH_SHORT).show()
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