package br.com.ilhasoft.voy.ui.report.fragment

import br.com.ilhasoft.voy.connectivity.ConnectivityManager
import br.com.ilhasoft.voy.db.report.ReportDbHelper
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.network.reports.ReportService
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.extensions.onMainThread
import io.reactivex.Flowable

/**
 * Created by lucasbarros on 09/02/18.
 */
class ReportInteractorImpl(private val status: Int) : ReportInteractor {

    private val reportService by lazy { ReportService() }
    private val reportDbHelper by lazy { ReportDbHelper() }

    override fun getReports(page: Int?, pageSize: Int?, theme: Int?, mapper: Int?, status: Int?): Flowable<List<Report>> {
        return if (ConnectivityManager.isConnected()) {
            reportService.getReports(page = page, page_size = pageSize, theme = theme, mapper = mapper, status = status)
                    .fromIoToMainThread()
                    .map { it.results }.toFlowable()
        } else {
            if (status == ReportFragment.PENDING_STATUS)
                reportDbHelper.getReports().onMainThread()
            else
                Flowable.empty()
        }
    }

}