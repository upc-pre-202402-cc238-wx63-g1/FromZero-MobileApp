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
                , R.drawable.geekit_profile,"Geekit.pe"),

            Project("Aplicación Móvil de Entrenamiento Personalizado",
                "La Aplicación Móvil de Entrenamiento Personalizado es un proyecto destinado a crear una aplicación móvil que permita a los usuarios " +
                        "realizar rutinas de entrenamiento personalizadas, con ejercicios y recomendaciones adaptadas a sus objetivos y necesidades. " +
                        "La aplicación debe ofrecer una interfaz amigable, un diseño atractivo y funcionalidades que faciliten la creación y seguimiento de rutinas de entrenamiento."
                , R.drawable.geekit_profile ,"FitApp"),
            Project("Plataforma de Gestión de Proyectos",
                "La Plataforma de Gestión de Proyectos es un proyecto destinado a crear una plataforma web que permita a las empresas gestionar sus proyectos de manera eficiente y colaborativa. " +
                        "La plataforma debe ofrecer herramientas para la planificación, seguimiento y control de proyectos, así como la gestión de recursos, tareas y comunicaciones entre los miembros del equipo."
                , R.drawable.geekit_profile,"ProjectManager"),
            Project("Aplicación de Reservas de Restaurantes", "La Aplicación de Reservas de Restaurantes es un proyecto destinado a crear una aplicación móvil que permita a los usuarios " +
                    "buscar, reservar y gestionar mesas en restaurantes de manera rápida y sencilla. La aplicación debe ofrecer una interfaz intuitiva, un diseño atractivo y funcionalidades que mejoren la experiencia del usuario, desde la búsqueda de restaurantes hasta la confirmación de reservas."
                , R.drawable.geekit_profile,"ReservaYa")
        )

        recyclerView.adapter = ProjectAdapter(projects)

        return view
    }
}