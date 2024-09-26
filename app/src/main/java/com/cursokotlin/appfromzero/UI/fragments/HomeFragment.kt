package com.cursokotlin.appfromzero.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.adapters.ProjectCardAdapter
import com.cursokotlin.appfromzero.models.ProjectCard


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProjectCardAdapter
    private lateinit var projectList: List<ProjectCard>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.rvProjects)
        recyclerView.layoutManager = LinearLayoutManager(context)

        projectList = listOf(
            ProjectCard("Proyecto A", 5, "Geekit.pe", "https://geekitpe.com/wp-content/uploads/2022/11/152x152.jpg"),
            ProjectCard("Proyecto B", 3, "Geekit.pe", "https://geekitpe.com/wp-content/uploads/2022/11/152x152.jpg"),
            ProjectCard("Proyecto C", 10, "Geekit.pe", "https://geekitpe.com/wp-content/uploads/2022/11/152x152.jpg")
        )

        adapter = ProjectCardAdapter(projectList, object : ProjectCardAdapter.OnItemClickListener {
            override fun onItemClick(projectCard: ProjectCard){
                Toast.makeText(context, "Clic en: ${projectCard.projectName}", Toast.LENGTH_SHORT).show()
            }
        })

        recyclerView.adapter = adapter

        return view
    }
}