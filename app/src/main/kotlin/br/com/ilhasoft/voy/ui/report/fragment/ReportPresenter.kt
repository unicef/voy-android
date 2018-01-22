package br.com.ilhasoft.voy.ui.report.fragment

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.reports.ReportService
import br.com.ilhasoft.voy.shared.helpers.RxHelper
import br.com.ilhasoft.voy.ui.report.ReportsActivity

class ReportPresenter(private val preferences: Preferences) :
        Presenter<ReportContract>(ReportContract::class.java) {

    private val reportService: ReportService by lazy { ReportService() }

    override fun attachView(view: ReportContract?) {
        super.attachView(view)
        view?.showLoading()
        loadReportsData()
        view?.dismissLoading()
    }

    private fun loadReportsData() {
        reportService.getReports(page = 1, page_size = 50,
                theme = ReportsActivity.themeId, mapper = preferences.getInt(User.ID), status = getStatus())
                .compose(RxHelper.defaultSingleSchedulers())
                .subscribe({ fillReportsAdapter(it.results) }, {})
    }

    private fun getStatus(): Int? = view?.getStatus()

    private fun fillReportsAdapter(reports: List<Report>) {
        view?.fillReportsAdapter(reports)
    }

    fun navigateToReportDetail(report: Report) {
        view.navigateToReportDetail(report)
    }

    fun onClickAddReport() {
        view.navigateToAddReport()
    }

    fun onClickEditReport(report: Report?) {
        view?.navigateToEditReport(report)
    }

}