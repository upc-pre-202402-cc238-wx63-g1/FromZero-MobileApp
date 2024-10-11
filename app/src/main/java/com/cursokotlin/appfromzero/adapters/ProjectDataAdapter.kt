package com.cursokotlin.appfromzero.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.cursokotlin.appfromzero.ProjectData
import com.cursokotlin.appfromzero.R

class ProjectDataAdapter(val projects: List<ProjectData>): Adapter<ProjectDescriptionPrototype>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectDescriptionPrototype {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.project_description_prototype, parent, false)
        return ProjectDescriptionPrototype(view)
    }

    override fun onBindViewHolder(holder: ProjectDescriptionPrototype, position: Int) {
        holder.bind(projects[position])
    }

    override fun getItemCount(): Int {
        return projects.size
    }

}

class ProjectDescriptionPrototype(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvTitle= itemView.findViewById<TextView>(R.id.tvDescriptionTitle)
    val tvDescription= itemView.findViewById<TextView>(R.id.tvDescriptionText)

    fun bind(projectData: ProjectData) {
        tvTitle.text = projectData.title
        tvDescription.text = projectData.description

        itemView.setOnClickListener{
            if (tvDescription.visibility == View.GONE) {
                tvDescription.visibility = View.VISIBLE
            } else {
                tvDescription.visibility = View.GONE
            }
        }
    }
}
