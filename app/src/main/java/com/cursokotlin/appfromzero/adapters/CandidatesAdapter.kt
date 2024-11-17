package com.cursokotlin.appfromzero.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.cursokotlin.appfromzero.adapters.CircleTransform
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.data.remote.RetrofitClient
import com.cursokotlin.appfromzero.data.repository.project.ProjectRepository
import com.cursokotlin.appfromzero.models.profile.DeveloperSearchCard
import com.cursokotlin.appfromzero.models.project.Candidate
import com.squareup.picasso.Picasso

class CandidatesAdapter(
    private var candidates: List<Candidate>,
    private val listener: OnCandidateActionListener
) : RecyclerView.Adapter<CandidatesAdapter.CandidateViewHolder>() {

    interface OnCandidateActionListener {
        fun onAccept(candidate: Candidate)
        fun onReject(candidate: Candidate)
    }

    class CandidateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profilePic: ImageView = itemView.findViewById(R.id.ivProfilePic)
        val name: TextView = itemView.findViewById(R.id.tvDeveloperName)
        val flag: ImageView = itemView.findViewById(R.id.ivFlag)
        val resumeTitle: TextView = itemView.findViewById(R.id.tvResumeTitle)
        val skillTitle: TextView = itemView.findViewById(R.id.tvSkillsTitle)
        val summary: TextView = itemView.findViewById(R.id.tvDeveloperSummary)
        val skills: TextView = itemView.findViewById(R.id.tvDeveloperSkills)
        val arrow: ImageView = itemView.findViewById(R.id.ivArrow)

        val btnAccept: CardView = itemView.findViewById(R.id.btnAccept)
        val btnReject: CardView = itemView.findViewById(R.id.btnReject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.prototype_candidate_card, parent, false)
        return CandidateViewHolder(view)
    }

    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        val candidate = candidates[position]

        // Carga la imagen del perfil usando Picasso
        Picasso.get()
            .load(candidate.profileImgUrl)
            .transform(CircleTransform())
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(holder.profilePic)

        // Carga la imagen de la bandera usando Picasso
        Picasso.get()
            .load(candidate.country)
            .error(R.drawable.sample_flag)
            .into(holder.flag)

        // Asignación de otros datos
        holder.name.text = "${candidate.firstName} ${candidate.lastName}"
        holder.summary.text = candidate.description
        holder.skills.text = candidate.specialties

        // Expande o colapsa la vista al hacer clic en la flecha
        holder.arrow.setOnClickListener {
            val isCollapsed = holder.summary.visibility == View.GONE
            holder.btnAccept.visibility = if (isCollapsed) View.VISIBLE else View.GONE
            holder.btnReject.visibility = if (isCollapsed) View.VISIBLE else View.GONE
            holder.resumeTitle.visibility = if (isCollapsed) View.VISIBLE else View.GONE
            holder.skillTitle.visibility = if (isCollapsed) View.VISIBLE else View.GONE
            holder.summary.visibility = if (isCollapsed) View.VISIBLE else View.GONE
            holder.skills.visibility = if (isCollapsed) View.VISIBLE else View.GONE
            holder.arrow.setImageResource(if (isCollapsed) R.drawable.arrow_up else R.drawable.arrow_down)
        }

        holder.btnAccept.setOnClickListener {
            listener.onAccept(candidate)
        }

        holder.btnReject.setOnClickListener {
            listener.onReject(candidate)
        }
    }

    override fun getItemCount(): Int = candidates.size

    fun updateCandidates(newCandidates: List<Candidate>) {
        candidates = newCandidates
        notifyDataSetChanged()
    }
}
