package com.cursokotlin.appfromzero.UI.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.adapters.ProjectCardAdapter
import com.cursokotlin.appfromzero.models.ProjectCard
import org.w3c.dom.Text


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProjectCardAdapter
    private lateinit var projectList: List<ProjectCard>

    // Home Edit Profile Components
    private lateinit var ivEditProfile: ImageView
    private lateinit var ivConfirmEditProfile: TextView

    // Home Enterprise Profile Components
    private lateinit var ivProfile: ImageView
    private lateinit var tvEnterpriseWebsite: TextView
    private lateinit var tvEnterpriseName: TextView
    private lateinit var tvEnterpriseSector: TextView
    private lateinit var tvEnterpriseRUC: TextView
    private lateinit var tvEnterpriseDescription: TextView
    private lateinit var tvEnterpriseCellphone: TextView

    // Home Enterprise Extending Components layout
    private lateinit var llExtending: LinearLayout


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Inicializar componentes
        ivEditProfile = view.findViewById(R.id.ivEditProfile)
        llExtending = view.findViewById(R.id.llExtending)

        val flContainer: FrameLayout = view.findViewById(R.id.flContainer)

        // Configurar el click listener
        setUpClickListener()

        // Configurar el touch listener para cerrar el modo de edición
        flContainer.setOnTouchListener { _, _ ->
            if (llExtending.visibility == View.VISIBLE) {
                animateViewVisibility(llExtending, View.GONE)
                ivEditProfile.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
            }
            false
        }

        recyclerView = view.findViewById(R.id.rvProjects)
        recyclerView.layoutManager = LinearLayoutManager(context)

        projectList = listOf(
            ProjectCard("Proyecto A", 5, "Geekit.pe", "https://geekitpe.com/wp-content/uploads/2022/11/152x152.jpg"),
            ProjectCard("Proyecto B", 3, "Geekit.pe", "https://geekitpe.com/wp-content/uploads/2022/11/152x152.jpg"),
            ProjectCard("Proyecto C", 10, "Geekit.pe", "https://geekitpe.com/wp-content/uploads/2022/11/152x152.jpg"),
            ProjectCard("Proyecto E", 7, "Geekit.pe", "https://geekitpe.com/wp-content/uploads/2022/11/152x152.jpg"),
            ProjectCard("Proyecto F", 6, "Geekit.pe", "https://geekitpe.com/wp-content/uploads/2022/11/152x152.jpg"),
            ProjectCard("Proyecto G", 0, "Geekit.pe", "https://geekitpe.com/wp-content/uploads/2022/11/152x152.jpg")
        )

        adapter = ProjectCardAdapter(projectList, object : ProjectCardAdapter.OnItemClickListener {
            override fun onItemClick(projectCard: ProjectCard){
                Toast.makeText(context, "Clic en: ${projectCard.projectName}", Toast.LENGTH_SHORT).show()
            }
        })

        recyclerView.adapter = adapter

        return view
    }

    private fun setUpClickListener() {
        ivEditProfile.setOnClickListener{
            if (llExtending.visibility == View.GONE) {
                recyclerView.visibility = View.GONE
                ivEditProfile.visibility = View.GONE
                animateViewVisibility(llExtending, View.VISIBLE)
            } else {
                llExtending.visibility = View.GONE
                ivEditProfile.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun animateViewVisibility(view: View, visibility: Int, onAnimationEnd: (() -> Unit)? = null) {
        if (visibility == View.VISIBLE) {
            view.alpha = 0f
            view.visibility = View.VISIBLE
            view.animate()
                .alpha(1f)
                .setDuration(900)
                .withEndAction {
                    onAnimationEnd?.invoke()
                }
        } else {
            view.animate()
                .alpha(0f)
                .setDuration(500)
                .withEndAction {
                    view.visibility = View.GONE
                    onAnimationEnd?.invoke()
                }
        }
    }
}