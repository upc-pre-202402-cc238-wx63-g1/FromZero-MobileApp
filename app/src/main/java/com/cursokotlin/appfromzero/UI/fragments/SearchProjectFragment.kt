package com.cursokotlin.appfromzero.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.adapters.ProjectAdapter
import com.cursokotlin.appfromzero.models.Project


class SearchProjectFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search_project, container, false)

        val recyclerView :RecyclerView =view.findViewById(R.id.rvProjects)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val projects = listOf(
            Project("Plataforma de Comercio Electrónico Geekit",
                "La Plataforma de Comercio Electrónico Geekit es un proyecto destinado a crear una experiencia de compra en línea excepcional para nuestra marca de ropa y accesorios para jóvenes apasionados por la cultura geek. " +
                        "La plataforma debe ofrecer una navegación intuitiva, una interfaz atractiva y funcionalidades que mejoren la experiencia " +
                        "del usuario, desde la búsqueda de productos hasta el proceso de compra y seguimiento de pedidos"
                , R.drawable.geekit_profile,"Geekit.pe")
        )

        recyclerView.adapter = ProjectAdapter(projects)

        return view
    }
}