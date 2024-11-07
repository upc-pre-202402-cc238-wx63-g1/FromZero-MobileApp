package com.cursokotlin.appfromzero.UI.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.adapters.ProjectAdapter
import com.cursokotlin.appfromzero.data.remote.RetrofitClient
import com.cursokotlin.appfromzero.data.repository.project.ProjectRepository
import com.cursokotlin.appfromzero.models.project.ProjectSearchCard
import retrofit2.Call
import retrofit2.Response

class SearchProjectFragment : Fragment(), ProjectAdapter.OnItemClickListener {
    private val projectRepository = ProjectRepository(RetrofitClient.projectService)
    private var projectList: List<ProjectSearchCard> = emptyList()
    private lateinit var projectAdapter: ProjectAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_project, container, false)
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        setupRecyclerView(view)
        loadProject(token)
        return view
    }

    private fun loadProject(token: String?) {
        if (token != null) {
            val call = projectRepository.getProjectsByState("En busqueda",token)
            call.enqueue(object : retrofit2.Callback<List<ProjectSearchCard>> {
                override fun onResponse(
                    call: Call<List<ProjectSearchCard>>,
                    response: Response<List<ProjectSearchCard>>
                ) {
                    if (response.isSuccessful) {
                        Log.d("SearchProject", "respuesta:" + response.body())
                        projectList = response.body() ?: emptyList()
                        bindProjectsToViews()
                        projectAdapter.updateProjects(projectList)
                    }
                }

                override fun onFailure(call: Call<List<ProjectSearchCard>>, t: Throwable) {
                    Log.e("SearchProject", "Error: ${t.message}")
                }
            })
        }
    }

    private fun bindProjectsToViews() {
        projectList = projectList.map { project ->
            ProjectSearchCard(
                id = project.id,
                name = project.name,
                description = project.description,
                pictureLogo = R.drawable.geekit,
                website = "geekit.com"
            )
        }
    }

    private fun setupRecyclerView(view: View) {
        val recyclerView: RecyclerView = view.findViewById(R.id.rvProjects)
        projectAdapter = ProjectAdapter(projectList, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = projectAdapter
    }

    override fun onItemClick(project: ProjectSearchCard) {
        replaceFragment(ViewProjectFragment(), project.id, false)
    }

    private fun replaceFragment(fragment: Fragment, idProject: Long, isWorking: Boolean) {
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
