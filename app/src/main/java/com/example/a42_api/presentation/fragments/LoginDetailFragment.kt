package com.example.a42_api.presentation.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.DrawableRes
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.chayangkoon.champ.glide.ktx.load
import com.example.a42_api.App
import com.example.a42_api.R
import com.example.a42_api.databinding.FragmentLoginDetailsBinding
import com.example.a42_api.domain.interactor.UserInteractor
import com.example.a42_api.presentation.adapters.ProjectListAdapter
import com.example.a42_api.presentation.adapters.SkillsListAdapter
import com.example.a42_api.presentation.contract.LoginDetailContract
import com.example.a42_api.presentation.mapper.LoginDetailMapper
import com.example.a42_api.presentation.models.Course
import com.example.a42_api.presentation.models.Project
import com.example.a42_api.presentation.models.Skill
import com.example.a42_api.presentation.models.User
import com.example.a42_api.presentation.presenters.LoginDetailPresenter
import com.example.a42_api.presentation.util.KeyForLogin
import com.example.a42_api.presentation.util.OnSpinnerItemSelectedListener

class LoginDetailFragment : BaseLoginFragment(), LoginDetailContract.View {
    private var binding: FragmentLoginDetailsBinding? = null
    private lateinit var presenter: LoginDetailPresenter
    private var spCourseChooser: Spinner? = null
    private var skillsAdapter: SkillsListAdapter? = null
    private var projectsAdapter: ProjectListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        val app: App = (requireActivity().applicationContext as App)
        val interactor: UserInteractor = app.interactor
        presenter = LoginDetailPresenter(this, interactor, LoginDetailMapper())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginDetailsBinding.inflate(inflater, container, false)
        spCourseChooser = binding?.loginProfile?.spCoursesChooser
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNavigationToolBar()
        initLogin()
        initSkillsList()
        initProjectList()
        setOnRetryConnectionClickListener()
    }

    private fun initNavigationToolBar() {
        binding?.detailToolbar?.setupWithNavController(findNavController())
    }

    private fun initLogin() {
        arguments?.let { bundle ->
            bundle.getSerializable(KeyForLogin.key)?.let {
                presenter.init(it as User)
            }
        }
    }

    private fun initSkillsList() {
        val progressLevelBackgroundDrawable: Drawable? = getDrawable(R.drawable.light_grey_card)
        skillsAdapter =
            SkillsListAdapter(progressLevelBackgroundDrawable, this::setColorToTextByRes)
        binding?.itemSkills?.rvProperties?.adapter = skillsAdapter
    }

    private fun initProjectList() {
        projectsAdapter = ProjectListAdapter(this::itemUpdateVisibility, this::setColorToTextByRes)
        binding?.itemProjects?.rvProperties?.adapter = projectsAdapter
        binding?.itemProjects?.rvProperties?.let {
            addDividerItemToRecyclerView(it, R.drawable.projects_divider)
        }
    }

    private fun addDividerItemToRecyclerView(rv: RecyclerView, @DrawableRes drawableResId: Int) {
        val dividerItem = DividerItemDecoration(activity, RecyclerView.VERTICAL)
        getDrawable(drawableResId)?.let {
            dividerItem.setDrawable(it)
            rv.addItemDecoration(dividerItem)
        }
    }

    private fun itemUpdateVisibility(view: View, show: Boolean) =
        view.updateVisibility(show)

    private fun setOnRetryConnectionClickListener() {
        binding?.layoutConnectionError?.buttonRetryConnection?.setOnClickListener {
            presenter.onRetryButtonClick()
        }
    }

    override fun showDetail(user: User, coursesNames: List<String>) {
        viewUpdateVisibility()
        binding?.detailToolbar?.title = user.login

        initCoursesSpinnerAdapter(coursesNames)
        setMainProfileInfo(user)
    }

    private fun setMainProfileInfo(user: User) {
        binding?.loginProfile?.let {
            it.ivPerson.load(user.imageUrl)
            it.tvPersonName.text = user.fullName
            it.tvLocation.text = user.location
            it.tvWallet.text = resources.getString(R.string.wallet, user.wallet)
            it.tvPoint.text = user.points.toString()
            it.tvEmail.text = user.email
            it.tvCity.text = user.city
        }
    }

    override fun updateColorProfile(colorResId: Int) {
        val color = getColor(colorResId)
        updateColorProfile(color.toLong())
    }

    override fun updateColorProfile(color: Long) {
        binding?.loginProfile?.let { itemProfile ->
            changeColorForImageIcon(itemProfile.imgWallet, color)
            changeColorForImageIcon(itemProfile.imgLocation, color)
            changeColorForImageIcon(itemProfile.imgPoint, color)
            changeColorForImageIcon(itemProfile.imCoursesChooser, color)

            val progressLevelDrawable =
                itemProfile.layoutProgressLevel.progressLevel.progressDrawable
            changeColorForDrawable(progressLevelDrawable, color)

            itemProfile.layoutProgressLevel.cardProgressLevel.background =
                getDrawable(R.drawable.transparent_black_card)

            setColorToTextByRes(itemProfile.layoutProgressLevel.tvProgressLevel, R.color.white)
            itemProfile.tvEmail.setTextColor(color.toInt())
        }
    }

    override fun updateBackgroundProfile(drawableResId: Int) {
        binding?.loginProfile?.imgBackgroundProfile?.background = getDrawable(drawableResId)
    }

    override fun updateBackgroundProfile(imageUrl: String) {
        binding?.loginProfile?.imgBackgroundProfile?.let {
            setImageToBackgroundFromUrl(it, imageUrl)
        }
    }

    override fun updateProgressLevel(level: Course.Level) {
        binding?.loginProfile?.layoutProgressLevel?.let {
            it.tvProgressLevel.text = resources.getString(
                R.string.level_and_percentage, level.fullLevel, level.percentage
            )
            it.progressLevel.progress = level.percentage
        }
    }

    override fun updateUserSkills(skills: List<Skill>) {
        binding?.itemSkills?.root?.updateVisibility(true)
        binding?.itemSkills?.tvNameProperty?.text = getString(R.string.skills)
        skillsAdapter?.submitList(skills)
    }

    override fun updateUserProjects(projects: List<Project>) {
        binding?.itemProjects?.root?.updateVisibility(true)
        binding?.itemProjects?.tvNameProperty?.text = getString(R.string.projects)
        projectsAdapter?.submitList(projects)
    }

    override fun showUserCoalition(show: Boolean) {
        binding?.loginProfile?.imgCoalition?.updateVisibility(show)
        binding?.loginProfile?.imgCoalitionBack?.updateVisibility(show)
    }

    override fun updateUserCoalition(coalitionImageUrl: String, coalitionBackgroundColor: Long) {
        binding?.loginProfile?.imgCoalition?.let {
            setSvgToImageViewWithCallBackFuncIfRequired(
                it, coalitionImageUrl, this::changeLayerPainForImageView
            )
        }
        binding?.loginProfile?.imgCoalitionBack?.let {
            changeColorForImageIcon(it, coalitionBackgroundColor)
        }
    }

    override fun showEmptyListProjectsMessage() {
        Log.w(null, "empty projects list")
    }

    private fun viewUpdateVisibility() {
        binding?.loginProfile?.root?.updateVisibility(true)
    }

    private fun initCoursesSpinnerAdapter(courseNames: List<String>) {
        activity?.let { activity ->
            val adapter = ArrayAdapter(
                activity.applicationContext, R.layout.item_course, courseNames
            )
            adapter.setDropDownViewResource(R.layout.item_clicked_course)
            spCourseChooser?.adapter = adapter

            initOnCoursesSelectListener()
        }
    }

    private fun initOnCoursesSelectListener() {
        spCourseChooser?.onItemSelectedListener =
            object : OnSpinnerItemSelectedListener() {
                override fun onItemSelected(
                    parent: AdapterView<*>, view: View, position: Int, id: Long
                ) =
                    presenter.onCourseClick(parent.getItemAtPosition(position).toString())
            }
    }

    override fun showConnectionErrorLayout(show: Boolean) {
        binding?.layoutConnectionError?.root?.updateVisibility(show)
    }

    override fun showProgressBar(show: Boolean) {
        binding?.progressIndicator?.updateVisibility(show)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}