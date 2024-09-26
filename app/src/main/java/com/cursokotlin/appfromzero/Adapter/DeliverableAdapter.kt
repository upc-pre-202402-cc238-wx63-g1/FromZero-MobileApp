package com.cursokotlin.appfromzero.Adapter

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.model.Deliverable

class DeliverableAdapter(var deliverables:ArrayList<Deliverable>): Adapter<DeliverablePrototype>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliverablePrototype {
        val view= LayoutInflater
            .from(parent.context)
            .inflate(R.layout.prototype_deliverable,parent,false)

        return DeliverablePrototype(view)
    }

    override fun onBindViewHolder(holder: DeliverablePrototype, position: Int) {
        holder.bind(deliverables.get(position))
    }

    override fun getItemCount(): Int {
        return deliverables.size
    }

}

class DeliverablePrototype(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tvDeliverableName = itemView.findViewById<TextView>(R.id.tvDeliverableName)
    val tvProjectName = itemView.findViewById<TextView>(R.id.tvProjectName)
    val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
    val tvDescriptionText = itemView.findViewById<TextView>(R.id.tvDescriptionText)
    val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
    val tvState = itemView.findViewById<TextView>(R.id.tvState)
    val cvDeliverableCard = itemView.findViewById<CardView>(R.id.cvDeliverableCard)

    val ivClock = itemView.findViewById<ImageView>(R.id.ivClock)
    val ivState = itemView.findViewById<ImageView>(R.id.ivState)
    val ivArrow = itemView.findViewById<ImageView>(R.id.ivArrow)

    var isExpanded = false

    fun bind(deliverable: Deliverable) {
        tvDeliverableName.text = deliverable.title
        tvProjectName.text = deliverable.projectName
        tvDescriptionText.text = deliverable.description
        tvDate.text = deliverable.date
        tvState.text = deliverable.state
        tvDescription.text = "Descripción"
        ivClock.setImageResource(android.R.drawable.ic_menu_recent_history)
        ivState.setImageResource(android.R.drawable.ic_menu_info_details)
        ivArrow.setImageResource(R.drawable.arrow_down)

        tvDescriptionText.visibility = View.GONE
        tvDescription.visibility = View.GONE

        cvDeliverableCard.setOnClickListener {
            if (isExpanded) {
                collapseCard(tvDescriptionText, tvDescription)
                ivArrow.setImageResource(R.drawable.arrow_down)
            } else {
                expandCard(tvDescriptionText, tvDescription)
                ivArrow.setImageResource(R.drawable.arrow_up)
            }
            isExpanded = !isExpanded
        }
    }

    private fun expandCard(vararg views: View) {
        views.forEach { it.visibility = View.VISIBLE }

        val initialHeight = cvDeliverableCard.height
        cvDeliverableCard.measure(
            View.MeasureSpec.makeMeasureSpec(cvDeliverableCard.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.UNSPECIFIED
        )
        val targetHeight = cvDeliverableCard.measuredHeight

        val animator = ValueAnimator.ofInt(initialHeight, targetHeight)
        animator.addUpdateListener { valueAnimator ->
            val layoutParams = cvDeliverableCard.layoutParams
            layoutParams.height = valueAnimator.animatedValue as Int
            cvDeliverableCard.layoutParams = layoutParams
        }
        animator.duration = 300
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.start()
    }

    private fun collapseCard(vararg views: View) {
        val initialHeight = cvDeliverableCard.height

        views.forEach { it.visibility = View.GONE }

        // Aseguramos que el colapso se detenga en tvState
        cvDeliverableCard.measure(
            View.MeasureSpec.makeMeasureSpec(cvDeliverableCard.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.UNSPECIFIED
        )
        val stateVisibleHeight = tvState.bottom

        val animator = ValueAnimator.ofInt(initialHeight, stateVisibleHeight)
        animator.addUpdateListener { valueAnimator ->
            val layoutParams = cvDeliverableCard.layoutParams
            layoutParams.height = valueAnimator.animatedValue as Int
            cvDeliverableCard.layoutParams = layoutParams
        }
        animator.duration = 300
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.start()
    }
}