package com.cursokotlin.appfromzero.adapters

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.models.Deliverable
import java.text.SimpleDateFormat
import java.util.Locale

class DeliverableAdapter(
    var deliverables: List<Deliverable>,
    private val onItemClick: (Deliverable) -> Unit,
    private val onDeleteClick: (Long) -> Unit
) : RecyclerView.Adapter<DeliverableAdapter.DeliverableViewHolder>() {

    var userRole: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliverableViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.prototype_deliverable, parent, false)
        return DeliverableViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeliverableViewHolder, position: Int) {
        holder.bind(deliverables[position], this, userRole, position, onItemClick)
    }

    override fun getItemCount(): Int = deliverables.size


    inner class DeliverableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvDeliverableName = itemView.findViewById<TextView>(R.id.tvDeliverableName)
        private val tvProjectName = itemView.findViewById<TextView>(R.id.tvProjectName)
        private val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
        private val tvDescriptionText = itemView.findViewById<TextView>(R.id.tvDescriptionText)
        private val tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        private val tvState = itemView.findViewById<TextView>(R.id.tvState)
        private val cvDeliverableCard = itemView.findViewById<CardView>(R.id.cvDeliverableCard)

        private val ivClock = itemView.findViewById<ImageView>(R.id.ivClock)
        private val ivState = itemView.findViewById<ImageView>(R.id.ivState)
        private val ivArrow = itemView.findViewById<ImageView>(R.id.ivArrow)

        private val btDelete = itemView.findViewById<Button>(R.id.btDelete)
        private val btEdit = itemView.findViewById<Button>(R.id.btEdit)

        private var isExpanded = false
        private var userRole: String? = null

        fun bind(
            deliverable: Deliverable,
            adapter: DeliverableAdapter,
            role: String?,
            position: Int,
            onItemClick: (Deliverable) -> Unit
        ) {
            tvDeliverableName.text = deliverable.name
            tvProjectName.text = deliverable.projectName
            tvDescriptionText.text = deliverable.description
            tvDate.text = deliverable.date.toString()
            tvState.text = deliverable.state
            tvDescription.text = "Descripción"
            ivClock.setImageResource(android.R.drawable.ic_menu_recent_history)
            ivState.setImageResource(android.R.drawable.ic_menu_info_details)
            ivArrow.setImageResource(R.drawable.arrow_down)

            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val formattedDate = deliverable.date?.let {
                try {
                    val date = inputDateFormat.parse(it)
                    outputDateFormat.format(date)
                } catch (e: Exception) {
                    it
                }
            } ?: "No Date"
            tvDate.text = formattedDate

            tvDescriptionText.visibility = View.GONE
            tvDescription.visibility = View.GONE
            btDelete.visibility = View.GONE
            btEdit.visibility = View.GONE

            userRole = role

            cvDeliverableCard.setOnClickListener {
                if (isExpanded) {
                    collapseCard()
                    ivArrow.setImageResource(R.drawable.arrow_down)
                } else {
                    expandCard()
                    ivArrow.setImageResource(R.drawable.arrow_up)
                }
                isExpanded = !isExpanded
            }

            btDelete.setOnClickListener {
                onDeleteClick(deliverable.id)
            }

            btEdit.setOnClickListener {
                onItemClick(deliverable)
            }


        }

        fun collapseCard() {
            val initialHeight = cvDeliverableCard.height

            tvDescriptionText.visibility = View.GONE
            tvDescription.visibility = View.GONE
            btDelete.visibility = View.GONE
            btEdit.visibility = View.GONE

            cvDeliverableCard.measure(
                View.MeasureSpec.makeMeasureSpec(cvDeliverableCard.width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.UNSPECIFIED
            )
            val stateVisibleHeight = tvState.bottom + 100

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

        private fun expandCard() {
            tvDescriptionText.visibility = View.VISIBLE
            tvDescription.visibility = View.VISIBLE

            if (userRole == "ROLE_DEVELOPER") {
                btDelete.visibility = View.VISIBLE
                btEdit.visibility = View.VISIBLE
            }else{
                btDelete.visibility = View.VISIBLE
                btEdit.visibility = View.VISIBLE
            }

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


    }
}