package com.cursokotlin.appfromzero.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.adapters.DeliverableAdapter
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.adapters.DeliverablePrototype
import com.cursokotlin.appfromzero.models.Deliverable

class DeliverablesFragment : Fragment(), CreateDeliverableFragment.OnDeliverableCreatedListener, EditDeliverableFragment.OnDeliverableEditedListener {

    private var deliverables = ArrayList<Deliverable>()
    private lateinit var deliverableAdapter: DeliverableAdapter
    private lateinit var rvDeliverables: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_deliverables, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadDeliverables()
        initView(view)
        return view
    }

    private fun initView(view: View) {
        rvDeliverables = view.findViewById(R.id.rvDeliverables)
        deliverableAdapter = DeliverableAdapter(deliverables) { deliverable ->
            val dialog = EditDeliverableFragment()
            val bundle = Bundle().apply {
                putInt("deliverableId", deliverable.id)
                putString("deliverableTitle", deliverable.title)
                putString("deliverableDescription", deliverable.description)
                putString("deliverableDate", deliverable.date)
            }
            dialog.arguments = bundle
            dialog.setOnDeliverableEditedListener(this@DeliverablesFragment)
            dialog.show(parentFragmentManager, "EditDeliverableDialog")
        }
        rvDeliverables.adapter = deliverableAdapter
        rvDeliverables.layoutManager = LinearLayoutManager(requireContext())

        val ivAddDeliverable = view.findViewById<ImageView>(R.id.ivAddDeliverable)
        ivAddDeliverable.setOnClickListener {
            val dialog = CreateDeliverableFragment()
            dialog.setOnDeliverableCreatedListener(this)
            dialog.show(parentFragmentManager, "AddDeliverableDialog")
        }
    }

    override fun onDeliverableCreated(deliverable: Deliverable) {
        deliverable.id = deliverables.size
        deliverables.add(deliverable)
        deliverableAdapter.notifyItemInserted(deliverables.size - 1)
    }

    override fun onDeliverableEdited(newDeliverable: Deliverable) {
        val index = deliverables.indexOfFirst { it.id == newDeliverable.id }
        if (index != -1) {
            deliverables[index] = newDeliverable
            deliverableAdapter.notifyItemChanged(index)
            val viewHolder = rvDeliverables.findViewHolderForAdapterPosition(index) as? DeliverablePrototype
            viewHolder?.collapseCard()
        }
    }

    private fun loadDeliverables() {
        deliverables.add(Deliverable(1, "Entregable 1", "Plataforma de Comercio Electrónico Geekit", "24/09/2024", "Espera", "Este entregable consistirá en un documento detallado que describe los requisitos funcionales y no funcionales de la Plataforma de Comercio Electrónico Geekit. Incluirá casos de uso, diagramas de flujo, requisitos de usuario, requisitos de sistema y cualquier otra información relevante para guiar el desarrollo del software."))
        deliverables.add(Deliverable(2, "Entregable 2", "Plataforma de Comercio Electrónico Geekit", "31/10/2024", "Espera", "Se entregará un prototipo interactivo de la interfaz de usuario de la Plataforma de Comercio Electrónico Geekit. Este prototipo permitirá a los stakeholders visualizar y navegar por las diferentes pantallas y funcionalidades de la aplicación, proporcionando una representación visual de cómo se verá y funcionará la plataforma final."))
        deliverables.add(Deliverable(3, "Entregable 3", "Plataforma de Comercio Electrónico Geekit", "30/11/2024", "Espera", "Este entregable consistirá en el código fuente del frontend y backend de la Plataforma de Comercio Electrónico Geekit. Se proporcionará una estructura de directorios organizada, con comentarios claros y limpios en el código para facilitar la comprensión y el mantenimiento futuro."))
        deliverables.add(Deliverable(4, "Entregable 4", "Plataforma de Comercio Electrónico Geekit", "30/11/2024", "Espera", "Este entregable consistirá en el código fuente del frontend y backend de la Plataforma de Comercio Electrónico Geekit. Se proporcionará una estructura de directorios organizada, con comentarios claros y limpios en el código para facilitar la comprensión y el mantenimiento futuro."))
    }
}