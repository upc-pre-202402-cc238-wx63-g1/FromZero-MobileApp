package com.cursokotlin.appfromzero.UI.fragments

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.adapters.DeliverableAdapter
import com.cursokotlin.appfromzero.data.remote.RetrofitClient
import com.cursokotlin.appfromzero.data.repository.deliverable.DeliverableRepository
import com.cursokotlin.appfromzero.data.repository.project.ProjectRepository
import com.cursokotlin.appfromzero.models.Deliverable
import com.cursokotlin.appfromzero.models.HomeViewModel
import com.cursokotlin.appfromzero.models.deliverable.DeliverableCard
import com.cursokotlin.appfromzero.models.project.Project
import retrofit2.Call
import retrofit2.Response

class DeliverablesFragment : Fragment(), CreateDeliverableFragment.OnDeliverableCreatedListener,
    EditDeliverableFragment.OnDeliverableEditedListener, SendDeliverableFragment.OnDeliverableSentListener,
    ReviewDeliverableFragment.OnDeliverableReviewedListener {

    private lateinit var deliverableAdapter: DeliverableAdapter
    private lateinit var rvDeliverables: RecyclerView
    private lateinit var ivAddDeliverable: ImageView
    private lateinit var cvCardEmpty: CardView
    private lateinit var deleteDeliverableDialog: Dialog
    private lateinit var btnConfirmDeleteDeliverable: Button
    private lateinit var btnCancelDeleteDeliverable: Button
    private val homeViewModel: HomeViewModel by activityViewModels()

    private val projectRepository = ProjectRepository(RetrofitClient.projectService)
    private val deliverableRepository = DeliverableRepository(RetrofitClient.deliverableService)
    private var deliverables: MutableList<Deliverable> = mutableListOf()
    private var deliverableList: List<DeliverableCard> = emptyList()
    private var idProject: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_deliverables, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPreferences =
            requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        arguments?.let {
            idProject = it.getLong("idProject")
        }

        setupDeleteDeliverableDialog()
        initView(view)
        loadDeliverables(view, idProject, token)
        return view
    }

    private fun setupDeleteDeliverableDialog() {
        deleteDeliverableDialog = Dialog(requireContext())
        deleteDeliverableDialog.setContentView(R.layout.delete_project_dialog)
        deleteDeliverableDialog.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(requireContext(), R.drawable.rounded_dialog_background)
        )
        deleteDeliverableDialog.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        deleteDeliverableDialog.setCancelable(true)

        btnConfirmDeleteDeliverable = deleteDeliverableDialog.findViewById(R.id.btn_aceptar)
        btnCancelDeleteDeliverable = deleteDeliverableDialog.findViewById(R.id.btn_cancelar)
    }

    private fun initView(view: View) {
        rvDeliverables = view.findViewById(R.id.rvDeliverables)

        homeViewModel.userRole.observe(viewLifecycleOwner) { role ->
            deliverableAdapter = DeliverableAdapter(
                deliverables,
                role,
                { deliverable -> onDeliverableSelected(deliverable) },
                { deliverableId -> deleteDeliverable(deliverableId) },
                { deliverable -> onReviewDeliverable(deliverable.id, deliverable.developerMessage ?: "") },
                { deliverableId -> onSendDeliverable(deliverableId) }
            )
            rvDeliverables.adapter = deliverableAdapter
            deliverableAdapter.notifyDataSetChanged()

            if (role == "ROLE_DEVELOPER") {
                ivAddDeliverable.visibility = View.GONE
                cvCardEmpty.visibility = View.VISIBLE
            } else {
                ivAddDeliverable.visibility = View.VISIBLE
            }
        }

        rvDeliverables.layoutManager = LinearLayoutManager(requireContext())

        ivAddDeliverable = view.findViewById(R.id.ivAddDeliverable)
        cvCardEmpty = view.findViewById(R.id.cvCardEmpty)
        ivAddDeliverable.setOnClickListener {
            val dialog = CreateDeliverableFragment()
            val bundle = Bundle()
            bundle.putLong("idProject", idProject)
            dialog.arguments = bundle
            dialog.setOnDeliverableCreatedListener(this)
            dialog.show(parentFragmentManager, "AddDeliverableDialog")
        }
    }

    private fun loadProjectName(projectId: Long, token: String?) {
        if (token == null) {
            Toast.makeText(requireContext(), "Token no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        val projectCall = projectRepository.getProjectById(projectId, token)
        projectCall.enqueue(object : retrofit2.Callback<Project> {
            override fun onResponse(call: Call<Project>, response: Response<Project>) {
                if (response.isSuccessful) {
                    val project = response.body()
                    val projectName = project?.name ?: "Nombre no disponible"
                    deliverables.forEach { deliverable ->
                        deliverable.projectName = projectName
                    }
                    deliverableAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Error al obtener el proyecto", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Project>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun loadDeliverables(view: View, projectId: Long, token: String?) {
        if (token == null) {
            Toast.makeText(requireContext(), "Token no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        val deliverableCall = deliverableRepository.getDeliverablesByProjectId(projectId, token)
        deliverableCall.enqueue(object : retrofit2.Callback<List<Deliverable>> {
            override fun onResponse(call: Call<List<Deliverable>>, response: Response<List<Deliverable>>) {
                if (response.isSuccessful) {
                    deliverables = (response.body() ?: emptyList()).toMutableList()
                    bindDeliverablesToViews()
                    initView(view)
                    loadProjectName(projectId, token)
                    Log.d("API Response", "Deliverables: $deliverables")
                } else {
                    Toast.makeText(requireContext(), "Error al obtener los entregables", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Deliverable>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                Log.wtf("deliverables", "Error: ${t.message}")
            }
        })
    }

    private fun deleteDeliverable(deliverableId: Long) {
        val tvTitle = deleteDeliverableDialog.findViewById<TextView>(R.id.tvTitle)
        tvTitle.text = "¿Estás seguro de que quieres eliminar este entregable?"
        deleteDeliverableDialog.show()

        btnConfirmDeleteDeliverable.setOnClickListener {
            deleteDeliverableDialog.dismiss()
            performDeleteDeliverable(deliverableId)
        }

        btnCancelDeleteDeliverable.setOnClickListener {
            deleteDeliverableDialog.dismiss()
        }
    }

    private fun performDeleteDeliverable(deliverableId: Long) {
        val token = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .getString("token", null)
        if (token == null) {
            Toast.makeText(requireContext(), "Token no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        val deleteCall = deliverableRepository.deleteDeliverable(deliverableId, token)
        deleteCall.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Entregable eliminado", Toast.LENGTH_SHORT).show()
                    deliverables.removeAll { it.id == deliverableId }
                    deliverableAdapter.notifyDataSetChanged()
                    loadDeliverables(requireView(), idProject, token)
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                    Log.e("DeliverablesFragment", "Error al eliminar el deliverable: $errorMessage")
                    Toast.makeText(requireContext(), "Error al eliminar el deliverable", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("DeliverablesFragment", "Error: ${t.message}", t)
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun bindDeliverablesToViews() {
        this.deliverableList = deliverables.map { deliverable ->
            DeliverableCard(
                id = deliverable.id,
                name = deliverable.name ?: "No Title",
                description = deliverable.description ?: "No Description",
                date = deliverable.date ?: "No Date",
                state = deliverable.state ?: "No State",
                projectId = deliverable.idProject,
                developerMessage = deliverable.developerMessage ?: "No Message",
                projectName = deliverable.projectName ?: "No Project Name"
            )
        }
    }


    override fun onDeliverableCreated(deliverable: Deliverable) {
        deliverables.add(deliverable)
        deliverableAdapter.notifyItemInserted(deliverables.size - 1)
        rvDeliverables.scrollToPosition(deliverables.size - 1)
        loadDeliverables(requireView(), idProject, requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE).getString("token", null))
    }

    override fun onDeliverableEdited(newDeliverable: Deliverable) {
        val index = deliverables.indexOfFirst { it.id == newDeliverable.id }
        if (index != -1) {
            deliverables[index] = newDeliverable
            deliverableAdapter.notifyItemChanged(index)
            val viewHolder = rvDeliverables.findViewHolderForAdapterPosition(index) as? DeliverableAdapter.DeliverableViewHolder
            viewHolder?.collapseCard()
        }
    }

    private fun onDeliverableSelected(deliverable: Deliverable) {
        val dialog = EditDeliverableFragment().apply {
            arguments = Bundle().apply {
                putLong("deliverableId", deliverable.id)
                putString("deliverableName", deliverable.name)
                putString("deliverableDescription", deliverable.description)
                putString("deliverableDate", deliverable.date)
                putString("projectName", deliverable.projectName)
            }
        }
        dialog.setOnDeliverableEditedListener(this)
        dialog.show(parentFragmentManager, "EditDeliverableDialog")
    }

    private fun onReviewDeliverable(deliverableId: Long, developerMessage: String) {
        val reviewDeliverableFragment = ReviewDeliverableFragment()
        val bundle = Bundle()
        bundle.putLong("deliverableId", deliverableId)
        bundle.putString("developerMessage", developerMessage)
        reviewDeliverableFragment.arguments = bundle
        reviewDeliverableFragment.setOnDeliverableReviewedListener(this)
        reviewDeliverableFragment.show(parentFragmentManager, "reviewDeliverableFragment")
    }


    private fun onSendDeliverable(deliverableId: Long) {
        val sendDeliverableFragment = SendDeliverableFragment()
        val bundle = Bundle()
        bundle.putLong("deliverableId", deliverableId)
        sendDeliverableFragment.arguments = bundle
        sendDeliverableFragment.setOnDeliverableSentListener(this)
        sendDeliverableFragment.show(parentFragmentManager, "sendDeliverableFragment")
    }

    override fun onDeliverableSendState(deliverableId: Long, newState: String) {
        val index = deliverables.indexOfFirst { it.id == deliverableId }
        if (index != -1) {
            deliverables[index].state = newState
            deliverableAdapter.notifyItemChanged(index)
        }
    }

    override fun onDeliverableReviewState(deliverableId: Long, newState: String) {
        val index = deliverables.indexOfFirst { it.id == deliverableId }
        if (index != -1) {
            deliverables[index].state = newState
            deliverableAdapter.notifyItemChanged(index)
        }
    }

}