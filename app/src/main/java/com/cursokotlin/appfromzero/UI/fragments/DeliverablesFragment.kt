package com.cursokotlin.appfromzero.UI.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.Adapter.DeliverableAdapter
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.model.Deliverable

class DeliverablesFragment : Fragment() {

    private var deliverables = ArrayList<Deliverable>()
    private lateinit var deliverableAdapter: DeliverableAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
        val rvDeliverables = view.findViewById<RecyclerView>(R.id.rvDeliverables)
        deliverableAdapter = DeliverableAdapter(deliverables)
        rvDeliverables.adapter = deliverableAdapter
        rvDeliverables.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun loadDeliverables() {
        deliverables.add(Deliverable("Entregable 1", "Plataforma de Comercio Electrónico Geekit","24/09/2024","Espera","Este entregable consistirá en un documento detallado que describe los requisitos funcionales y no funcionales de la Plataforma de Comercio Electrónico Geekit. Incluirá casos de uso, diagramas de flujo, requisitos de usuario, requisitos de sistema y cualquier otra información relevante para guiar el desarrollo del software."))
        deliverables.add(Deliverable("Entregable 2", "Plataforma de Comercio Electrónico Geekit","31/10/2024","Espera","Se entregará un prototipo interactivo de la interfaz de usuario de la Plataforma de Comercio Electrónico Geekit. Este prototipo permitirá a los stakeholders visualizar y navegar por las diferentes pantallas y funcionalidades de la aplicación, proporcionando una representación visual de cómo se verá y funcionará la plataforma final."))
        deliverables.add(Deliverable("Entregable 3", "Plataforma de Comercio Electrónico Geekit","30/11/2024","Espera","Este entregable consistirá en el código fuente del frontend y backend de la Plataforma de Comercio Electrónico Geekit. Se proporcionará una estructura de directorios organizada, con comentarios claros y limpios en el código para facilitar la comprensión y el mantenimiento futuro."))
        deliverables.add(Deliverable("Entregable 4", "Plataforma de Comercio Electrónico Geekit","30/11/2024","Espera","Este entregable consistirá en el código fuente del frontend y backend de la Plataforma de Comercio Electrónico Geekit. Se proporcionará una estructura de directorios organizada, con comentarios claros y limpios en el código para facilitar la comprensión y el mantenimiento futuro."))
    }
}
