package br.com.ilhasoft.voy.ui.report

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ActivityReportsBinding
import br.com.ilhasoft.voy.databinding.ViewReportsToolbarBinding
import br.com.ilhasoft.voy.ui.base.BaseActivity
import br.com.ilhasoft.voy.ui.report.adapter.NavigationItem
import br.com.ilhasoft.voy.ui.report.adapter.ReportsAdapter
import br.com.ilhasoft.voy.ui.report.fragment.ReportFragment

/**
 * Created by developer on 11/01/18.
 */
class ReportsActivity : BaseActivity(), ReportsContract {

    companion object {
        @JvmStatic
        private val THEME_ID = "themeId"
        @JvmStatic
        private val THEME_NAME = "themeName"
        @JvmStatic
        private val THEME_COLOR = "themeColor"

        @JvmStatic
        fun createIntent(context: Context, themeId: Int?,
                         themeName: String?, themeColor: String?): Intent {
            val intent = Intent(context, ReportsActivity::class.java)
            intent.putExtra(THEME_ID, themeId)
            intent.putExtra(THEME_NAME, themeName)
            intent.putExtra(THEME_COLOR, themeColor)
            return intent
        }
    }

    private val binding: ActivityReportsBinding by lazy {
        DataBindingUtil.setContentView<ActivityReportsBinding>(this, R.layout.activity_reports)
    }
    private val presenter: ReportsPresenter by lazy { ReportsPresenter() }
    private val themeId: Int by lazy { intent.extras.getInt(THEME_ID) }
    private val themeName: String by lazy { intent.extras.getString(THEME_NAME) }
    private val themeColor: String by lazy { intent.extras.getString(THEME_COLOR) }

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

    override fun navigateBack() {
        finish()
    }

    private fun setupView() {
        binding.apply {
            viewToolbar?.run { setupToolbar(this) }
            setupTabs()
        }
    }

    private fun setupToolbar(viewToolbar: ViewReportsToolbarBinding) = with(viewToolbar) {
        name.setTextColor(Color.parseColor(getString(R.string.color_hex, this@ReportsActivity.themeColor)))
        themeName = this@ReportsActivity.themeName
        presenter = this@ReportsActivity.presenter
    }

    private fun setupTabs() {
        val adapter = ReportsAdapter(supportFragmentManager, createNavigationItems())
        binding.apply {
            viewPager.let {
                it.adapter = adapter
                it.offscreenPageLimit = adapter.count
            }
            tabLayout.setupWithViewPager(binding.viewPager)
        }
    }

    private fun createNavigationItems(): MutableList<NavigationItem> {
        val approved = NavigationItem(ReportFragment.newInstance(ReportFragment.APPROVED_STATUS,
                themeId), getString(R.string.approved_fragment_title))
        val pending = NavigationItem(ReportFragment.newInstance(ReportFragment.PENDING_STATUS,
                themeId), getString(R.string.pending_fragment_title))
        val rejected = NavigationItem(ReportFragment.newInstance(ReportFragment.NOT_APPROVED_STATUS,
                themeId), getString(R.string.not_approved_fragment_title))
        return mutableListOf(approved, pending, rejected)
    }

}
