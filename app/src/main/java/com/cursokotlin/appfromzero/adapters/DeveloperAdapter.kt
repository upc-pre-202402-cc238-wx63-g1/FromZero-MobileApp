package com.cursokotlin.appfromzero.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.models.Developer

class DeveloperAdapter(private val developers: List<Developer>) :
    RecyclerView.Adapter<DeveloperAdapter.DeveloperViewHolder>() {

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
        holder.profilePic.setImageResource(developer.profilePic)
        holder.name.text = developer.name
        holder.flag.setImageResource(developer.countryFlag)
        holder.rating.rating = developer.rating
        holder.summary.text = developer.summary
        holder.skills.text = developer.skills

        holder.arrow.setOnClickListener {
            if (holder.summary.visibility == View.GONE) {
                holder.resumeTitle.visibility = View.VISIBLE
                holder.summary.visibility = View.VISIBLE
                holder.skillsTitle.visibility = View.VISIBLE
                holder.skills.visibility = View.VISIBLE
                holder.arrow.setImageResource(R.drawable.arrow_up)
            } else {
                holder.resumeTitle.visibility = View.GONE
                holder.summary.visibility = View.GONE
                holder.skillsTitle.visibility = View.GONE
                holder.skills.visibility = View.GONE
                holder.arrow.setImageResource(R.drawable.arrow_down)
            }
        }
    }

    override fun getItemCount(): Int = developers.size
}