package br.com.ilhasoft.voy.network.reports

import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ReportFile
import io.reactivex.Observable
import io.reactivex.Single
import java.io.File

/**
 * Created by erickjones on 06/03/18.
 */
interface ReportDataSource {

    fun getReports(theme: Int? = null, project: Int? = null,
                   mapper: Int? = null, status: Int? = null): Single<List<Report>>

    fun getReport(
        id: Int,
        theme: Int? = null,
        project: Int? = null,
        mapper: Int? = null,
        status: Int? = null
    ): Single<Report>

    fun saveReport(report: Report): Single<Report>

    fun saveReport(
        theme: Int, location: Location, description: String?, name: String,
        tags: List<String>, urls: List<String>?, medias: List<File>
    ): Observable<Report>

    fun saveReports(reports: List<Report>): Single<List<Report>>

    fun updateReport(
        reportId: Int,
        theme: Int,
        location: Location,
        description: String?,
        name: String,
        tags: List<String>,
        urls: List<String>?,
        newFiles: List<File>?,
        filesToDelete: List<Int>?
    ): Observable<Report>

    fun saveFile(file: File, reportId: Int): Single<ReportFile>
}