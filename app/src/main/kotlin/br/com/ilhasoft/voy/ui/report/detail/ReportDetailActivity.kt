package br.com.ilhasoft.voy.ui.report.detail

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.PopupMenu
import br.com.ilhasoft.support.core.helpers.DimensionHelper
import br.com.ilhasoft.support.recyclerview.adapters.AutoRecyclerAdapter
import br.com.ilhasoft.support.recyclerview.adapters.OnCreateViewHolder
import br.com.ilhasoft.support.recyclerview.decorations.SpaceItemDecoration
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityReportDetailBinding
import br.com.ilhasoft.voy.databinding.ItemTagBinding
import br.com.ilhasoft.voy.databinding.ViewReportToolbarBinding
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.SharedPreferences
import br.com.ilhasoft.voy.ui.base.BaseActivity
import br.com.ilhasoft.voy.ui.comment.CommentsActivity
import br.com.ilhasoft.voy.ui.report.detail.holder.TagViewHolder
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager

class ReportDetailActivity : BaseActivity(), ReportDetailContract,
        PopupMenu.OnMenuItemClickListener/*, ViewPager.OnPageChangeListener*/ {

    companion object {
        @JvmStatic
        private val REPORT_ID = "reportId"
        @JvmStatic
        private val THEME_ID = "themeId"
        @JvmStatic
        private val REPORT_STATUS = "status"

        @JvmStatic
        fun createIntent(context: Context, reportId: Int, themeId: Int, status: Int): Intent {
            val intent = Intent(context, ReportDetailActivity::class.java)
            intent.putExtra(REPORT_ID, reportId)
            intent.putExtra(THEME_ID, themeId)
            intent.putExtra(REPORT_STATUS, status)
            return intent
        }
    }

    private val binding: ActivityReportDetailBinding by lazy {
        DataBindingUtil.setContentView<ActivityReportDetailBinding>(this, R.layout.activity_report_detail)
    }
    private val presenter: ReportDetailPresenter by lazy {
        ReportDetailPresenter(SharedPreferences(this))
    }
    /*private val carouselAdapter by lazy { CarouselAdapter(supportFragmentManager, getCarouselItems()) }
    private val indicatorViewHolder: OnCreateViewHolder<Indicator, IndicatorViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            IndicatorViewHolder(ViewIndicatorBinding.inflate(layoutInflater, parent, false), presenter)
        }
    }
    private val indicatorAdapter: AutoRecyclerAdapter<Indicator, IndicatorViewHolder> by lazy {
        AutoRecyclerAdapter<Indicator, IndicatorViewHolder>(indicatorViewHolder).apply {
            setHasStableIds(false)
        }
    }*/
    private val tagViewHolder:
            OnCreateViewHolder<String, TagViewHolder> by lazy {
        OnCreateViewHolder { layoutInflater, parent, _ ->
            TagViewHolder(ItemTagBinding.inflate(layoutInflater, parent, false), presenter)
        }
    }
    private val tagsAdapter:
            AutoRecyclerAdapter<String, TagViewHolder> by lazy {
        AutoRecyclerAdapter(mutableListOf(), tagViewHolder).apply {
            setHasStableIds(true)
        }
    }
    private val reportDetailId: Int by lazy { intent.extras.getInt(REPORT_ID) }
    private val themeId: Int by lazy { intent.extras.getInt(THEME_ID) }
    private val reportStatus: Int by lazy { intent.extras.getInt(REPORT_STATUS) }
    private lateinit var popupMenu: PopupMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        presenter.attachView(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun getReportId(): Int = reportDetailId

    override fun getThemeId(): Int? = themeId

    override fun getReportStatus(): Int? = reportStatus

    override fun getThemeColor(): String? = binding.report?.themeColor

    override fun showReportData(report: Report) {
        binding.viewToolbar?.name?.setTextColor(Color.parseColor(getString(R.string.color_hex,
                report.themeColor)))
        binding.name.setTextColor(Color.parseColor(getString(R.string.color_hex,
                report.themeColor)))
        binding.report = report
        report.tags?.let {
            tagsAdapter.addAll(it)
            tagsAdapter.notifyDataSetChanged()
        }
    }

    override fun navigateBack() = onBackPressed()

    override fun showReportAlert() {

    }

    override fun showPopupMenu() {
        popupMenu.show()
    }

    override fun navigateToCommentReport() = startActivity(CommentsActivity.createIntent(this))

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.edit -> true
        R.id.share -> true
        else -> false
    }

    /*override fun swapPage(indicator: Indicator) {
        binding.run {
            viewPager.setCurrentItem(indicator.position, true)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    override fun onPageSelected(position: Int) {
        indicatorAdapter.single { it -> it.selected }.selected = false
        indicatorAdapter[position].selected = true
        presenter.indicator = indicatorAdapter[position]
        indicatorAdapter.notifyDataSetChanged()
    }*/

    private fun setupView() {
        binding.run {
            viewToolbar?.run { setupToolbar(this) }
            /*setupViewPager(viewPager)
            setupIndicatorRecyclerView(indicatorsList)*/
            setupRecyclerView(tags)
            presenter = this@ReportDetailActivity.presenter
        }
    }

    /*private fun setupViewPager(viewPager: WrapContentViewPager) = with(viewPager) {
        adapter = carouselAdapter
        offscreenPageLimit = carouselAdapter.count
        addOnPageChangeListener(this@ReportDetailActivity)
    }

    private fun getCarouselItems(): List<CarouselItem> = mutableListOf()*//*presenter.report.mediaList.map { it ->
        CarouselItem(CarouselFragment.newInstance(it))
    }*/

    private fun setupToolbar(viewToolbar: ViewReportToolbarBinding) = with(viewToolbar) {
        setupPopupMenu(expandedMenu)
        showReportAlert = true
        presenter = this@ReportDetailActivity.presenter
    }

    private fun setupPopupMenu(expandedMenu: ImageButton) {
        popupMenu = PopupMenu(this, expandedMenu)
        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.menuInflater.inflate(R.menu.report_detail, popupMenu.menu)
    }

    private fun setupRecyclerView(tags: RecyclerView) = with(tags) {
        layoutManager = setupLayoutManager()
        addItemDecoration(setupItemDecoration())
        setHasFixedSize(true)
        adapter = tagsAdapter
    }

    private fun setupLayoutManager(): RecyclerView.LayoutManager =
            FlexboxLayoutManager(this).apply {
                flexWrap = FlexWrap.WRAP
            }

    private fun setupItemDecoration(): SpaceItemDecoration {
        val space = DimensionHelper.toPx(this, 4f)
        return SpaceItemDecoration(0, 0, 2 * space, space)
    }

    /*private fun setupIndicatorRecyclerView(indicatorsList: RecyclerView) = with(indicatorsList) {
        layoutManager = setupIndicatorLayoutManager()
        setHasFixedSize(true)
        indicatorAdapter.addAll(presenter.getIndicators())
        indicatorAdapter[Indicator.INITIAL_POSITION].selected = true
        adapter = indicatorAdapter
    }

    private fun setupIndicatorLayoutManager(): RecyclerView.LayoutManager? =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)*/

}
