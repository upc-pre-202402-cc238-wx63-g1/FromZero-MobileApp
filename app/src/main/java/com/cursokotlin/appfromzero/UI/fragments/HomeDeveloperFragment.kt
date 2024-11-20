package com.cursokotlin.appfromzero.UI.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.adapters.CircleTransform
import com.cursokotlin.appfromzero.adapters.ProjectCardAdapter
import com.cursokotlin.appfromzero.data.SupabaseStorageClient
import com.cursokotlin.appfromzero.data.remote.RetrofitClient
import com.cursokotlin.appfromzero.data.repository.developer.DeveloperRepository
import com.cursokotlin.appfromzero.data.repository.project.ProjectRepository
import com.cursokotlin.appfromzero.db.AppDatabase
import com.cursokotlin.appfromzero.models.Developer
import com.cursokotlin.appfromzero.models.project.Project
import com.cursokotlin.appfromzero.models.ProjectCard
import com.cursokotlin.appfromzero.models.ProjectState
import com.cursokotlin.appfromzero.models.profile.DeveloperProfileResponse
import com.cursokotlin.appfromzero.models.profile.UpdateDeveloperProfileRequest
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body
import java.io.File
import java.util.UUID

class HomeDeveloperFragment : Fragment() {

    private val developerRepository = DeveloperRepository(RetrofitClient.developerService)
    private val projectRepository = ProjectRepository(RetrofitClient.projectService)
    private var developer: Developer? = null

    private val PICK_IMAGE_REQUEST = 1

    private var projectList: List<ProjectCard> = emptyList()
    private lateinit var projects: List<Project>

    private lateinit var adapter: ProjectCardAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: LinearLayout
    private lateinit var btnCreateProject: Button
    private lateinit var ivEditDevProfile: ImageView
    private lateinit var cvCardEmpty: LinearLayout

    private lateinit var cvHomeDeveloperProfile: CardView
    private lateinit var btnChangeProfilePhoto: ImageButton
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_developer, container, false)

        emptyView = view.findViewById(R.id.emptyView)
        btnCreateProject = view.findViewById(R.id.btnSearchProject)

        btnChangeProfilePhoto = view.findViewById(R.id.btnChangeDevProfilePhoto)

        btnCreateProject.setOnClickListener {
            replaceFragment(SearchProjectFragment())
        }

        setupRecyclerView(view)
        setChangeProfilePhotoListener()

        val sharedPreferences =
            requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val userRole = sharedPreferences.getString("userRole", null)
        val userId = sharedPreferences.getLong("userId", 0)
        val token = sharedPreferences.getString("token", null)

        initDeveloperView(view, userId, token, "ROLE_DEVELOPER")

        return view
    }

    private fun setChangeProfilePhotoListener() {
        btnChangeProfilePhoto.setOnClickListener {
            showPhotoOptions()
        }
    }

    private fun showPhotoOptions() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_photo_options, null)
        bottomSheetDialog.setContentView(view)

        val tvChooseFromGallery = view.findViewById<TextView>(R.id.tvChooseFromGallery)

        tvChooseFromGallery.setOnClickListener {
            checkAndRequestPermissions()
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            startActivityForResult(intent, PICK_IMAGE_REQUEST)

            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }

    private fun checkAndRequestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (permissions.any {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    it
                ) != PackageManager.PERMISSION_GRANTED
            }) {
            ActivityCompat.requestPermissions(requireActivity(), permissions, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                handleImageUri(uri)
            } else {
                Toast.makeText(
                    requireContext(),
                    "No se seleccionó ninguna imagen",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun handleImageUri(uri: Uri) {
        try {
            // Usar ContentResolver para acceder al archivo
            val inputStream = requireContext().contentResolver.openInputStream(uri)

            // Generar un nombre único para el archivo
            val uniqueFileName = "profile_${UUID.randomUUID()}.png"

            // Guardar el archivo con nombre único y formato PNG
            val file = File(requireContext().cacheDir, uniqueFileName)
            file.outputStream().use { outputStream ->
                inputStream?.copyTo(outputStream)
            }

            // Subir la imagen a Supabase
            uploadImageToSupabase(file)  // Sube el archivo a Supabase
        } catch (e: Exception) {
            Log.e("ImageSelection", "Error al manejar el archivo seleccionado", e)
            Toast.makeText(requireContext(), "Error al procesar la imagen", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun uploadImageToSupabase(file: File) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = SupabaseStorageClient.uploadFileToSupabase(file, "profile")
                withContext(Dispatchers.Main) {
                    if (url != null) {
                        saveProfileImageUrl(url)  // Guarda el URL en tu base de datos
                        bindDataToViews(role = "empresa")
                        bindProjectsToViews(projects)
                        Toast.makeText(
                            requireActivity(),
                            "Imagen subida con éxito",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "Error al subir la imagen",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("SupabaseUpload", "Error al subir la imagen", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireActivity(),
                        "Error al subir la imagen",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun saveProfileImageUrl(url: String) {

        val sharedPreferences =
            requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getLong("userId", 0)
        val token = sharedPreferences.getString("token", null)

        Log.d("UpdateProfile", "Updating profile for user $token")

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
                profileImgUrl = url
            )
            Log.d("UpdateRequest", updateRequest.toString())

            val call = developerRepository.updateDeveloperProfile(userId, updateRequest, token)
            call.enqueue(object : retrofit2.Callback<DeveloperProfileResponse> {
                override fun onResponse(
                    call: Call<DeveloperProfileResponse>,
                    response: Response<DeveloperProfileResponse>
                ) {
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
                                email = developer!!.email,
                                profileImgUrl = developer!!.profileImgUrl
                            )
                            val dao = AppDatabase.getInstance(requireContext()).getDeveloperDao()
                            dao.updateProfileImg(
                                updatedDeveloper.userId,
                                updatedDeveloper.profileImgUrl
                            )

                            fetchData(userId, token, "ROLE_DEVELOPER", requireView())
                            bindDataToViews(role = "developer")
                            bindProjectsToViews(projects)
                            Toast.makeText(
                                requireContext(),
                                "Profile updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e(
                            "UpdateProfile",
                            "Error updating profile: ${response.code()} - $errorBody"
                        )
                        Toast.makeText(
                            requireContext(),
                            "Error updating profile: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<DeveloperProfileResponse>, t: Throwable) {
                    Log.e("UpdateProfile", "Error updating profile", t)
                    Toast.makeText(requireContext(), "Error updating profile", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        } else {
            Toast.makeText(
                requireContext(),
                "Token not found or developer data not available",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initDeveloperView(view: View, userId: Long, token: String?, userRole: String) {
        cvHomeDeveloperProfile = view.findViewById(R.id.cvHomeDeveloperProfile)
        cvHomeDeveloperProfile.visibility = View.VISIBLE

        fetchData(userId, token, userRole, view)
        setRecyclerViewContraints(view, R.id.cvHomeDeveloperProfile)

        setUpClickListener(view)
        setupTouchListener(view)
    }

    private fun fetchData(userId: Long, token: String?, userRole: String, view: View) {
        if (token.isNullOrEmpty()) {
            showToast("Token no válido")
            return
        }

        if (userRole != "ROLE_DEVELOPER") {
            showToast("Rol del usuario no válido")
            return
        }

        initDeveloperComponent(view)

        val dao = AppDatabase.getInstance(requireContext()).getDeveloperDao()
        val dev = dao.getDeveloperByUserId(userId)

        if (dev == null) {
            fetchDeveloperProfile(userId, token)
        } else {
            this.developer = dev
            Log.d("Developer", developer.toString())
            bindDataToViews(role = "developer")
        }
        fetchProjects(userId, token, view)
    }

    private fun fetchDeveloperProfile(userId: Long, token: String) {
        val call = developerRepository.getDeveloperByUserId(userId, token)
        call.enqueue(object : Callback<DeveloperProfileResponse> {
            override fun onResponse(
                call: Call<DeveloperProfileResponse>,
                response: Response<DeveloperProfileResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { developerData ->
                        developer = Developer(
                            id = developerData.userId,
                            name = "${developerData.firstName} ${developerData.lastName}",
                            rating = 0f,
                            profilePic = R.drawable.placeholder,
                            countryFlag = R.drawable.sample_flag,
                            summary = developerData.description ?: "Sin descripción",
                            skills = developerData.specialties ?: "Sin especialidades",
                            phone = developerData.phone ?: "No disponible",
                            email = "example@gmail.com",
                            profileImgUrl = developerData.profileImgUrl
                        )
                        val dao = AppDatabase.getInstance(requireContext()).getDeveloperDao()
                        dao.insertOne(developer!!)
                        bindDataToViews(role = "developer")
                    } ?: showToast("No se encontró el perfil del desarrollador")
                } else {
                    logError("Error al obtener el perfil: ${response.errorBody()?.string()}")
                    showToast("Error al obtener el perfil")
                }
            }

            override fun onFailure(call: Call<DeveloperProfileResponse>, t: Throwable) {
                logError("Error en la solicitud de perfil: ${t.message}")
                showToast("Error al obtener el perfil del desarrollador")
            }
        })
    }

    private fun fetchProjects(userId: Long, token: String, view: View) {
        val call = projectRepository.getProjectsByDeveloperUserId(userId, token)
        call.enqueue(object : Callback<List<Project>> {
            override fun onResponse(call: Call<List<Project>>, response: Response<List<Project>>) {
                if (response.isSuccessful) {
                    projects = response.body().orEmpty()
                    Log.d("Projects", projects.toString())
                    bindProjectsToViews(projects)
                    setupRecyclerView(view)
                } else {
                    logError("Error al obtener proyectos: ${response.errorBody()?.string()}")
                    showToast("No se encontraron proyectos")
                }
            }

            override fun onFailure(call: Call<List<Project>>, t: Throwable) {
                logError("Error en la solicitud de proyectos: ${t.message}")
                showToast("Error al obtener los proyectos")
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun logError(message: String) {
        Log.e("FetchData", message)
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
                etDevDescription.setText(it.summary)
                etDevSpecialties.setText(it.skills)
                etCellphone.setText(it.phone)

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
                        Toast.makeText(
                            context,
                            "Postulando a ${projectCard.projectName}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    ProjectState.EN_PROGRESO -> {
                        replaceFragmentViewProject(
                            ViewProjectFragment(),
                            projectCard.idProject,
                            true
                        )
                    }

                    ProjectState.FINALIZADO -> {
                        Toast.makeText(
                            context,
                            "Revisando el proyecto ${projectCard.projectName}",
                            Toast.LENGTH_SHORT
                        ).show()
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
                pictureUrl = developer?.profileImgUrl ?: "",
                projectState = when (project.state) {
                    "En busqueda" -> ProjectState.BUSQUEDA_DEVELOPER
                    "En progreso" -> ProjectState.EN_PROGRESO
                    "Finalizado" -> ProjectState.FINALIZADO
                    else -> ProjectState.BUSQUEDA_DEVELOPER
                },
                projectProgress = project.progress.toInt(),
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

        constraintSet.connect(
            recyclerView.id,
            ConstraintSet.TOP,
            cvHomeEnterpriseProfile,
            ConstraintSet.BOTTOM,
            15
        )
        constraintSet.applyTo(constraintLayout)
    }

    private fun setEmptyViewConstraints(view: View, cvHomeDeveloperProfile: Int) {
        val emptyView = view.findViewById<LinearLayout>(R.id.emptyView)
        val constraintLayout = view.findViewById<ConstraintLayout>(R.id.clHomeUIDev)
        if (constraintLayout != null) {
            val constraintSet = ConstraintSet()
            constraintSet.clone(constraintLayout)
            constraintSet.connect(
                emptyView.id,
                ConstraintSet.TOP,
                cvHomeDeveloperProfile,
                ConstraintSet.BOTTOM,
                15
            )
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

    private fun updateProfile() {
        val sharedPreferences =
            requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getLong("userId", 0)
        val token = sharedPreferences.getString("token", null)

        Log.d("UpdateProfile", "Updating profile for user $token")

        if (token != null && developer != null) {
            val nameParts = developer!!.name.split(" ")
            val firstName = nameParts[0]
            val lastName = nameParts.getOrElse(1) { "" }

            developer!!.profileImgUrl?.let { profileImgUrl ->
                val updateRequest = UpdateDeveloperProfileRequest(
                    firstName = firstName,
                    lastName = lastName,
                    description = etDevDescription.text.toString(),
                    country = developer!!.countryFlag.toString(), // Assuming countryFlag holds the country information
                    phone = etCellphone.text.toString(),
                    specialties = etDevSpecialties.text.toString(),
                    profileImgUrl = profileImgUrl
                )

                Log.d("UpdateRequest", updateRequest.toString())

                val call = developerRepository.updateDeveloperProfile(userId, updateRequest, token)
                call.enqueue(object : retrofit2.Callback<DeveloperProfileResponse> {
                    override fun onResponse(
                        call: Call<DeveloperProfileResponse>,
                        response: Response<DeveloperProfileResponse>
                    ) {
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
                                    email = developer!!.email,
                                    profileImgUrl = updatedDeveloper.profileImgUrl
                                )

                                val dao =
                                    AppDatabase.getInstance(requireContext()).getDeveloperDao()

                                dao.updateDeveloperProfile(
                                    updatedDeveloper.userId,
                                    updatedDeveloper.specialties,
                                    updatedDeveloper.description,
                                    updatedDeveloper.phone
                                )
                                val dev1 = dao.getDeveloperByUserId(userId)
                                Log.d("DeveloperGet", dev1.toString())

                                // Reassign the new values to the EditText fields
                                etDevDescription.setText(updatedDeveloper.description)
                                etDevSpecialties.setText(updatedDeveloper.specialties)
                                etCellphone.setText(updatedDeveloper.phone)
                                etEmail.setText(developer!!.email)

                                bindDataToViews(role = "developer")
                                Toast.makeText(
                                    requireContext(),
                                    "Profile updated successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e(
                                "UpdateProfile",
                                "Error updating profile: ${response.code()} - $errorBody"
                            )
                            Toast.makeText(
                                requireContext(),
                                "Error updating profile: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<DeveloperProfileResponse>, t: Throwable) {
                        Log.e("UpdateProfile", "Error updating profile", t)
                        Toast.makeText(
                            requireContext(),
                            "Error updating profile",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            } ?: run {
                Toast.makeText(
                    requireContext(),
                    "Profile image URL is not available",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(
                requireContext(),
                "Token not found or developer data not available",
                Toast.LENGTH_SHORT
            ).show()
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

    private fun replaceFragmentViewProject(
        fragment: Fragment,
        idProject: Long,
        isWorking: Boolean
    ) {
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