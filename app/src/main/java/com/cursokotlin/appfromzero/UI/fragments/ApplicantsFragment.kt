package com.cursokotlin.appfromzero.UI.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.adapters.CandidatesAdapter
import com.cursokotlin.appfromzero.data.remote.RetrofitClient
import com.cursokotlin.appfromzero.data.repository.project.ProjectRepository
import com.cursokotlin.appfromzero.models.project.Candidate
import com.cursokotlin.appfromzero.models.project.Project
import retrofit2.Call
import retrofit2.Response

class ApplicantsFragment : DialogFragment(), CandidatesAdapter.OnCandidateActionListener {

    private val projectRepository = ProjectRepository(RetrofitClient.projectService)

    interface OnDeveloperSelectedListener {
        fun onDeveloperSelected(developer: Candidate)
    }

    private var listener: OnDeveloperSelectedListener? = null
    private var projectId: Long = 0
    private lateinit var candidateList: List<Candidate>
    private lateinit var adapter: CandidatesAdapter
    private var userId: Long = 0
    private var token: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_applicants, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvDevelopers)

        val sharedPreferences =
            requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        userId = sharedPreferences.getLong("userId", 0)
        token = sharedPreferences.getString("token", null)

        adapter = CandidatesAdapter(candidateList, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        return view
    }

    fun setDeveloperList(developerList: List<Candidate>) {
        this.candidateList = developerList
    }

    fun setProjectId(id: Long) {
        this.projectId = id
    }

    fun setOnDeveloperSelectedListener(listener: OnDeveloperSelectedListener) {
        this.listener = listener
    }

    override fun onAccept(candidate: Candidate) {
        if (token != null) {
            projectRepository.assignDeveloper(projectId, candidate.userId, token!!)
                .enqueue(object : retrofit2.Callback<Project> {
                    override fun onResponse(call: Call<Project>, response: Response<Project>) {
                        if (response.isSuccessful) {
                            Log.d("ApplicantsFragment", "Developer assigned to project")
                            listener?.onDeveloperSelected(candidate)
                        } else {
                            Log.d("ApplicantsFragment", "Error assigning developer to project")
                        }
                    }

                    override fun onFailure(call: Call<Project>, t: Throwable) {
                        Log.d("ApplicantsFragment", "Error assigning developer to project")
                    }
                })
        }
    }

    override fun onReject(candidate: Candidate) {
        Log.d("ApplicantsFragment", "Rejected candidate: ${candidate.firstName} ${candidate.lastName}")
    }
}