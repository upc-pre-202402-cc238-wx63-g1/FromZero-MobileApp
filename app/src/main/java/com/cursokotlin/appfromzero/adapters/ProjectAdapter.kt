package com.cursokotlin.appfromzero.adapters

import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.models.Project

class ProjectAdapter(private val projects: List<Project>) : RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder>() {

    class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val projectPic: ImageView = itemView.findViewById(R.id.ivLogo)
        val title: TextView = itemView.findViewById(R.id.tvPlatformName)
        val description: TextView = itemView.findViewById(R.id.tvDescriptionProject)
        val website:TextView = itemView.findViewById(R.id.tvWebsite)
        val arrow: ImageView = itemView.findViewById(R.id.ivArrow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.prototype_search_projects, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projects[position]
        holder.projectPic.setImageResource(project.pictureLogo)
        holder.title.text = project.title

        val descriptionPrefix = "Descripción:"
        val spannableDescription = SpannableString("$descriptionPrefix\n${project.description}")
        spannableDescription.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            descriptionPrefix.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        holder.description.text = spannableDescription
        holder.website.text = project.website

        holder.arrow.setOnClickListener {
            if (holder.description.visibility == View.GONE) {
                holder.description.visibility = View.VISIBLE
                holder.arrow.setImageResource(R.drawable.arrow_up)
            } else {
                holder.description.visibility = View.GONE
                holder.arrow.setImageResource(R.drawable.arrow_down)
            }
        }
    }

    override fun getItemCount(): Int = projects.size
}