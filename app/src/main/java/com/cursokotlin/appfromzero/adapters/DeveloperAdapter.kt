package com.cursokotlin.appfromzero.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.models.profile.DeveloperSearchCard
import com.squareup.picasso.Picasso

class DeveloperAdapter(
    private var developers: List<DeveloperSearchCard>
) : RecyclerView.Adapter<DeveloperAdapter.DeveloperViewHolder>() {

    class DeveloperViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profilePic: ImageView = itemView.findViewById(R.id.ivProfilePic)
        val name: TextView = itemView.findViewById(R.id.tvDeveloperName)
        val flag: ImageView = itemView.findViewById(R.id.ivFlag)
        val rating: RatingBar = itemView.findViewById(R.id.rbDeveloperRating)
        val resumeTitle: TextView = itemView.findViewById(R.id.tvResumeTitle)
        val summary: TextView = itemView.findViewById(R.id.tvDeveloperSummary)
        val skillsTitle: TextView = itemView.findViewById(R.id.tvSkillsTitle)
        val skills: TextView = itemView.findViewById(R.id.tvDeveloperSkills)
        val arrow: ImageView = itemView.findViewById(R.id.ivArrow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeveloperViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.prototype_search_developer, parent, false)
        return DeveloperViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeveloperViewHolder, position: Int) {
        val developer = developers[position]

        // Carga la imagen del perfil usando Picasso
        Picasso.get()
            .load(developer.profileImgUrl)
            .error(R.drawable.sample_profile)
            .into(holder.profilePic)

        // Carga la imagen de la bandera usando Picasso
        Picasso.get()
            .load(developer.country)
            .error(R.drawable.sample_flag)
            .into(holder.flag)

        // Asignación de otros datos
        holder.name.text = "${developer.firstName} ${developer.lastName}"
        holder.rating.rating = developer.rating
        holder.summary.text = developer.description
        holder.skills.text = developer.specialties

        // Expande o colapsa la vista al hacer clic en la flecha
        holder.arrow.setOnClickListener {
            val isCollapsed = holder.summary.visibility == View.GONE
            holder.resumeTitle.visibility = if (isCollapsed) View.VISIBLE else View.GONE
            holder.summary.visibility = if (isCollapsed) View.VISIBLE else View.GONE
            holder.skillsTitle.visibility = if (isCollapsed) View.VISIBLE else View.GONE
            holder.skills.visibility = if (isCollapsed) View.VISIBLE else View.GONE
            holder.arrow.setImageResource(if (isCollapsed) R.drawable.arrow_up else R.drawable.arrow_down)
        }
    }

    override fun getItemCount(): Int = developers.size

    fun updateDevelopers(newDevelopers: List<DeveloperSearchCard>) {
        developers = newDevelopers
        notifyItemRangeChanged(0, newDevelopers.size)
    }
}
