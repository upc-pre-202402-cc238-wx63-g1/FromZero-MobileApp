package com.cursokotlin.appfromzero.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.models.ProjectCard
import com.squareup.picasso.Picasso


class ProjectCardAdapter(
    private val projectCards: List<ProjectCard>,
    private val clickListener: OnItemClickListener
) : Adapter<ProjectCardAdapter.ProjectViewHolder>() {

    inner class ProjectViewHolder(itemView: View) : ViewHolder(itemView) {
        private val tvProjectTitle: TextView = itemView.findViewById(R.id.tvProjectTitle)
        private val tvNumCandidates: TextView = itemView.findViewById(R.id.tvNumCandidates)
        private val ivProfileEnterprisePhoto: ImageView = itemView.findViewById(R.id.ivProfileEnterprisePhoto)
        private val tvEnterpriseName: TextView = itemView.findViewById(R.id.tvEnterpriseName)

        fun bind(project: ProjectCard) {
            tvProjectTitle.text = project.projectName
            tvNumCandidates.text = "Postulante: ${project.numPostulantes}"
            tvEnterpriseName.text = project.enterpriseName

            Picasso.get()
                .load(project.pictureUrl)
                .into(ivProfileEnterprisePhoto)

            itemView.setOnClickListener { clickListener.onItemClick(project) }  // Manejo de clic
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.prototype_project_card, parent, false)
        return ProjectViewHolder(view)
    }

    override fun getItemCount(): Int {
        return projectCards.size
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.bind(projectCards[position])  // Asegúrate de usar projectCards
    }

    interface OnItemClickListener {
        fun onItemClick(projectCard: ProjectCard)
    }
}
