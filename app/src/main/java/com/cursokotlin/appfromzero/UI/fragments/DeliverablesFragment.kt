package com.cursokotlin.appfromzero.UI.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.adapters.DeliverableAdapter
import com.cursokotlin.appfromzero.adapters.ProjectCardAdapter
import com.cursokotlin.appfromzero.data.remote.RetrofitClient
import com.cursokotlin.appfromzero.data.repository.deliverable.DeliverableRepository
import com.cursokotlin.appfromzero.models.Deliverable
import com.cursokotlin.appfromzero.models.HomeViewModel
import com.cursokotlin.appfromzero.models.ProjectCard
import com.cursokotlin.appfromzero.models.ProjectState

import com.cursokotlin.appfromzero.models.deliverable.DeliverableCard
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit

class DeliverablesFragment : Fragment(), CreateDeliverableFragment.OnDeliverableCreatedListener,
    EditDeliverableFragment.OnDeliverableEditedListener {


    private lateinit var deliverableAdapter: DeliverableAdapter
    private lateinit var rvDeliverables: RecyclerView
    private lateinit var ivAddDeliverable: ImageView
    private lateinit var cvCardEmpty: CardView
    private val homeViewModel: HomeViewModel by activityViewModels()

    private val deliverableRepository=DeliverableRepository(RetrofitClient.deliverableService)
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

        initView(view)


        homeViewModel.userRole.observe(viewLifecycleOwner) { role ->
            when (role) {
                "desarrollador" -> {

                    ivAddDeliverable.visibility = View.GONE
                    cvCardEmpty.visibility = View.VISIBLE
                    val adapter = rvDeliverables.adapter as? DeliverableAdapter
                    if (adapter != null) {
                        adapter.userRole = role
                    }
                }
            }
        }

        val sharedPreferences =
            requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        arguments?.let {
            idProject = it.getLong("idProject")
        }
        loadDeliverables(view,idProject,token)
        return view
    }

    private fun initView(view: View) {
        rvDeliverables = view.findViewById(R.id.rvDeliverables)
        deliverableAdapter = DeliverableAdapter(deliverables) { deliverable ->
            val dialog = EditDeliverableFragment()
            dialog.setOnDeliverableEditedListener(this@DeliverablesFragment)
            dialog.show(parentFragmentManager, "EditDeliverableDialog")
        }
        rvDeliverables.adapter = deliverableAdapter
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


    private fun loadDeliverables(view: View, projectId: Long, token: String?) {
        if (token == null) {
            Toast.makeText(requireContext(), "Token no encontrado", Toast.LENGTH_SHORT).show()
            return
        }

        val deliverableCall = deliverableRepository.getDeliverablesByProjectId(projectId,token)
        deliverableCall.enqueue(object : retrofit2.Callback<List<Deliverable>> {
            override fun onResponse(call: Call<List<Deliverable>>, response: Response<List<Deliverable>>) {
                if (response.isSuccessful) {
                    deliverables = (response.body() ?: emptyList()).toMutableList()
                    bindDeliverablesToViews()
                    initView(view)
                    Log.d("API Response", "Deliverables: $deliverables")

                } else {
                    Toast.makeText(requireContext(), "Error al obtener los entregables", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Deliverable>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_LONG).show()
                Log.wtf("deliverables","Error: ${t.message}")
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
                developerMessage = deliverable.message ?: "No Message"
            )
        }
    }

    override fun onDeliverableCreated(deliverable: Deliverable) {
        deliverables.add(deliverable)
        deliverableAdapter.notifyItemInserted(deliverables.size - 1)
        rvDeliverables.scrollToPosition(deliverables.size - 1)
    }

    override fun onDeliverableEdited(newDeliverable: Deliverable) {
//        val index = deliverables.indexOfFirst { it.id == newDeliverable.id }
//        if (index != -1) {
//            deliverables[index] = newDeliverable
//            deliverableAdapter.notifyItemChanged(index)
//            val viewHolder =
//                rvDeliverables.findViewHolderForAdapterPosition(index) as? DeliverablePrototype
//            viewHolder?.collapseCard()
//        }
    }

}