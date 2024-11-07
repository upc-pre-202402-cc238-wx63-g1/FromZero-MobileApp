package com.cursokotlin.appfromzero.UI.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.adapters.DeveloperAdapter
import com.cursokotlin.appfromzero.adapters.ProjectAdapter
import com.cursokotlin.appfromzero.data.remote.RetrofitClient
import com.cursokotlin.appfromzero.data.repository.developer.DeveloperRepository
import com.cursokotlin.appfromzero.models.Developer
import com.cursokotlin.appfromzero.models.profile.DeveloperSearchCard
import com.cursokotlin.appfromzero.models.project.ProjectSearchCard
import retrofit2.Call
import retrofit2.Response

class SearchFragment : Fragment() {
    private val developerRepository = DeveloperRepository(RetrofitClient.developerService)
    private var developerList: List<DeveloperSearchCard> = emptyList()
    private lateinit var developerAdapter: DeveloperAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val sharedPreferences = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        setupRecyclerView(view)
        loadDevelopers(token)
        return view
    }

    private fun loadDevelopers(token: String?){
        if (token != null){
            val call = developerRepository.getDevelopers(token)
            call.enqueue(object : retrofit2.Callback<List<DeveloperSearchCard>>{
                override fun onResponse(
                    call: Call<List<DeveloperSearchCard>>,
                    response: Response<List<DeveloperSearchCard>>
                ) {
                    if(response.isSuccessful){
                        Log.d("SearchDevelopers", "respuesta:" + response.body())
                    }
                    developerList = response.body() ?: emptyList()
                    bindProjectsToViews()
                    developerAdapter.updateDevelopers(developerList)
                }

                override fun onFailure(call: Call<List<DeveloperSearchCard>>, t: Throwable) {
                    Log.e("SearchDevelopers", "Error: ${t.message}")
                }

            })
        }

    }
    private fun bindProjectsToViews() {
        developerList = developerList.map { developer ->
            DeveloperSearchCard(
                id = developer.id,
                firstName = developer.firstName,
                lastName= developer.lastName,
                rating = 3.5f,
                description = developer.description,
                country = developer.country,
                specialties = developer.specialties,
                profileImgUrl = developer.profileImgUrl,
                userId = developer.userId
            )
        }
    }
    private fun setupRecyclerView(view: View) {
        val recyclerView: RecyclerView = view.findViewById(R.id.rvDevelopers)
        developerAdapter = DeveloperAdapter(developerList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = developerAdapter
    }
}