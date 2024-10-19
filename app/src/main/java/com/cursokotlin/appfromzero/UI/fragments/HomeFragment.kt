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
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cursokotlin.appfromzero.R
import com.cursokotlin.appfromzero.adapters.ProjectCardAdapter
import com.cursokotlin.appfromzero.models.Enterprise
import com.cursokotlin.appfromzero.models.ProjectCard
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
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

    private lateinit var enterprise: Enterprise
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProjectCardAdapter
    private lateinit var projectList: List<ProjectCard>

    // Home Edit Profile Components
    private lateinit var ivEditProfile: ImageView
    private lateinit var ivEditProfileWebSite: ImageView
    private lateinit var etEnterpriseWebsite: TextInputEditText
    private lateinit var ivEditProfileSector: ImageView
    private lateinit var etEnterpriseSector: TextInputEditText
    private lateinit var ivEditProfileDescription: ImageView
    private lateinit var etEnterpriseDescription: TextInputEditText
    private lateinit var ivEditProfilePhone: ImageView
    private lateinit var etEnterprisePhone: TextInputEditText

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

        // Fetch data from the API
        fetchData()

        initComponent(view)

        val flContainer: FrameLayout = view.findViewById(R.id.flContainer)
        val cvHomeProfile: CardView = view.findViewById(R.id.cvHomeProfile)

        // Configure the touch listener for the FrameLayout
        flContainer.setOnTouchListener { _, _ ->
            if (llExtending.visibility == View.VISIBLE) {
                animateViewVisibility(llExtending, View.GONE)
                ivEditProfileSector.visibility = View.GONE
                ivEditProfileWebSite.visibility = View.GONE
                ivEditProfile.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
            }
            false
        }

        // Prevent touch events inside the CardView from propagating to the FrameLayout
        cvHomeProfile.setOnTouchListener { _, _ -> true }

        // Configurar el click listener
        setUpClickListener()

        recyclerView = view.findViewById(R.id.rvProjects)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = ProjectCardAdapter(projectList, object : ProjectCardAdapter.OnItemClickListener {
            override fun onItemClick(projectCard: ProjectCard){
                Toast.makeText(context, "Clic en: ${projectCard.projectName}", Toast.LENGTH_SHORT).show()
            }
        })

        recyclerView.adapter = adapter

        return view
    }

    private fun fetchData(){
        // This function will handle data fetching from the API REST in the future
        loadMockData()
    }

    private fun loadMockData(){
        enterprise = Enterprise(
            "Geekit.pe",
            "geekitpe.com",
            "https://geekitpe.com/wp-content/uploads/2022/11/152x152.jpg",
            "Geekit es tu destino para la moda geek. Nos especializamos en ofrecer una selección única de ropa y accesorios para jóvenes apasionados por la cultura pop, los videojuegos, el cine y los cómics. En Geekit, encontrarás prendas que te permiten expresar tu estilo auténtico y tu amor por tus intereses favoritos.",
            "Tecnología",
            "Geekit.pe",
            "987654321"
        )

        projectList = listOf(
            ProjectCard("Proyecto A", 5, "Geekit.pe", "https://geekitpe.com/wp-content/uploads/2022/11/152x152.jpg"),
            ProjectCard("Proyecto B", 3, "Geekit.pe", "https://geekitpe.com/wp-content/uploads/2022/11/152x152.jpg"),
            ProjectCard("Proyecto C", 10, "Geekit.pe", "https://geekitpe.com/wp-content/uploads/2022/11/152x152.jpg"),
            ProjectCard("Proyecto E", 7, "Geekit.pe", "https://geekitpe.com/wp-content/uploads/2022/11/152x152.jpg"),
            ProjectCard("Proyecto F", 6, "Geekit.pe", "https://geekitpe.com/wp-content/uploads/2022/11/152x152.jpg"),
            ProjectCard("Proyecto G", 0, "Geekit.pe", "https://geekitpe.com/wp-content/uploads/2022/11/152x152.jpg")
        )
    }

    private fun  initComponent(view: View){
        ivProfile = view.findViewById(R.id.ivProfilePhoto)

        tvEnterpriseWebsite = view.findViewById(R.id.tvEnterpriseWebSite)
        etEnterpriseWebsite = view.findViewById(R.id.etEnterpriseWebsite)
        tvEnterpriseName = view.findViewById(R.id.tvEnterpriseName)
        tvEnterpriseSector = view.findViewById(R.id.tvEnterpriseSector)
        etEnterpriseSector = view.findViewById(R.id.etEnterpriseSector)
        tvEnterpriseRUC = view.findViewById(R.id.tvEnterpriseRUC)
        tvEnterpriseDescription = view.findViewById(R.id.tvEnterpriseDescription)
        etEnterpriseDescription = view.findViewById(R.id.etEnterpriseDescription)
        tvEnterpriseCellphone = view.findViewById(R.id.tvEnterprisePhone)
        etEnterprisePhone = view.findViewById(R.id.etEnterprisePhone)

        //Initialize the icon for the edit profile
        ivEditProfileWebSite = view.findViewById(R.id.ivEditProfileWebSite)
        ivEditProfileSector = view.findViewById(R.id.ivEditProfileSector)
        ivEditProfileDescription = view.findViewById(R.id.ivEditProfileDescription)
        ivEditProfilePhone = view.findViewById(R.id.ivEditProfilePhone)

        setupEditToggle(ivEditProfileWebSite, tvEnterpriseWebsite, etEnterpriseWebsite)
        setupEditToggle(ivEditProfileSector, tvEnterpriseSector, etEnterpriseSector)
        setupEditToggle(ivEditProfileDescription, tvEnterpriseDescription, etEnterpriseDescription)
        setupEditToggle(ivEditProfilePhone, tvEnterpriseCellphone, etEnterprisePhone)

        bindDataToViews()
    }

    private fun setupEditToggle(editButton: ImageView, textView: TextView, editText: TextInputEditText) {
        editButton.setOnClickListener {
            if (textView.visibility == View.VISIBLE) {
                textView.visibility = View.GONE
                editText.visibility = View.VISIBLE
                editText.setText(textView.text)
            } else {
                textView.visibility = View.VISIBLE
                editText.visibility = View.GONE
                textView.text = editText.text
            }
        }
    }

    private fun bindDataToViews() {
        Picasso.get()
            .load(enterprise.pictureUrl)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(ivProfile)
        tvEnterpriseWebsite.text = enterprise.website
        tvEnterpriseName.text = enterprise.name
        tvEnterpriseSector.text = enterprise.field
        tvEnterpriseRUC.text = enterprise.socialRazon
        tvEnterpriseDescription.text = enterprise.description
        tvEnterpriseCellphone.text = enterprise.cellphone
    }

    private fun setUpClickListener() {
        ivEditProfile.setOnClickListener{
            if (llExtending.visibility == View.GONE) {
                recyclerView.visibility = View.GONE
                ivEditProfile.visibility = View.GONE
                ivEditProfileWebSite.visibility = View.VISIBLE
                ivEditProfileSector.visibility = View.VISIBLE
                animateViewVisibility(llExtending, View.VISIBLE)
            } else {
                llExtending.visibility = View.GONE
                ivEditProfile.visibility = View.VISIBLE
                ivEditProfileSector.visibility = View.GONE
                ivEditProfileWebSite.visibility = View.GONE
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