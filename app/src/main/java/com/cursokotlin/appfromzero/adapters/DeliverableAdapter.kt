package com.cursokotlin.appfromzero.adapters

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.models.Deliverable
import java.text.SimpleDateFormat
import java.util.Locale

class DeliverableAdapter(
    var deliverables: List<Deliverable>,
    private val userRole: String,
    private val onEditClick: (Deliverable) -> Unit,
    private val onDeleteClick: (Long) -> Unit,
    private val onReviewClick: (Deliverable) -> Unit,
    private val onSendClick: (Long) -> Unit
) : RecyclerView.Adapter<DeliverableAdapter.DeliverableViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliverableViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.prototype_deliverable, parent, false)
        return DeliverableViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeliverableViewHolder, position: Int) {
        holder.bind(deliverables[position], this, userRole, position, onEditClick, onReviewClick, onSendClick)
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
        private val btReview = itemView.findViewById<Button>(R.id.btReview)
        private val btSend = itemView.findViewById<Button>(R.id.btSend)

        private var isExpanded = false
        private var userRole: String? = null

        fun bind(
            deliverable: Deliverable,
            adapter: DeliverableAdapter,
            role: String?,
            position: Int,
            onItemClick: (Deliverable) -> Unit,
            onReviewClick: (Deliverable) -> Unit,
            onSendClick: (Long) -> Unit
        ) {

            tvDeliverableName.text = deliverable.name
            tvProjectName.text = deliverable.projectName
            tvDescriptionText.text = deliverable.description
            tvDate.text = deliverable.date.toString()
            tvState.text = deliverable.state
            tvDescription.text = "Descripción"


            when (deliverable.state) {
                "Completed" -> {
                    tvState.text = "Aprobado"
                    ivState.setImageResource(R.drawable.ic_check)
                    btReview.isEnabled = false
                    btSend.isEnabled = false
                }
                "Rejected" -> {
                    tvState.text = "Rechazado"
                    ivState.setImageResource(R.drawable.ic_reject)
                    btReview.isEnabled = true
                    btSend.isEnabled = false
                }
                "Awaiting Review" -> {
                    tvState.text = "En revisión"
                    ivState.setImageResource(R.drawable.ic_reviewing)
                    btSend.isEnabled = false
                    btSend.setOnClickListener {
                        Toast.makeText(itemView.context, "Ya ha subido un avance a este entregable", Toast.LENGTH_SHORT).show()
                    }
                }
                "Pending" -> {
                    tvState.text = "Pendiente"
                    ivState.setImageResource(R.drawable.ic_pending)
                    btReview.isEnabled = true
                    btSend.isEnabled = true
                }
                else -> {

                    ivState.setImageResource(R.drawable.ic_pending)
                }
            }


            btReview.setOnClickListener {
                if (!btReview.isEnabled) {
                    Toast.makeText(itemView.context, "El entregable ya ha sido revisado", Toast.LENGTH_SHORT).show()
                } else {
                    onReviewClick(deliverable)
                }
            }


            btSend.setOnClickListener {
                onSendClick(deliverable.id)
            }

            // Configuración de la expansión del cardview
            tvDescriptionText.visibility = View.GONE
            tvDescription.visibility = View.GONE
            btDelete.visibility = View.GONE
            btEdit.visibility = View.GONE
            btReview.visibility = View.GONE
            btSend.visibility = View.GONE

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

        private fun expandCard() {
            tvDescriptionText.visibility = View.VISIBLE
            tvDescription.visibility = View.VISIBLE

            if (userRole != "ROLE_DEVELOPER") {
                btDelete.visibility = View.VISIBLE
                btEdit.visibility = View.VISIBLE
                btReview.visibility = View.VISIBLE
            } else {
                btSend.visibility = View.VISIBLE
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

        fun collapseCard() {
            val initialHeight = cvDeliverableCard.height

            tvDescriptionText.visibility = View.GONE
            tvDescription.visibility = View.GONE
            btDelete.visibility = View.GONE
            btEdit.visibility = View.GONE
            btReview.visibility = View.GONE
            btSend.visibility = View.GONE

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
    }

}