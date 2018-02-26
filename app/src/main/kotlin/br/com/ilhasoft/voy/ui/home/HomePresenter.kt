package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.*
import br.com.ilhasoft.voy.network.notification.NotificationService
import br.com.ilhasoft.voy.shared.extensions.extractNumbers
import br.com.ilhasoft.voy.shared.helpers.RxHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class HomePresenter(
    private val preferences: Preferences,
    private val homeInteractor: HomeInteractor
) : Presenter<HomeContract>(HomeContract::class.java) {

    //TODO: move to interactor
    private val notificationService by lazy { NotificationService() }

    private var selectedProject: Project? = null
    private val userId = preferences.getInt(User.ID)

    override fun attachView(view: HomeContract) {
        super.attachView(view)
        loadData()
        loadNotifications()
    }

    fun onClickMyAccount() {
        view.navigateToMyAccount()
    }

    fun onClickChooseProject() {
        view.selectProject()
    }

    fun onClickNotifications() {
        view.showNotifications()
    }

    fun onClickHeaderNavView() {
        view.dismissNotifications()
    }

    fun onClickProject(project: Project) {
        selectedProject = project
        view.swapProject(selectedProject!!)
        loadThemesData(selectedProject!!.id)
    }

    fun onClickItemNotification(notification: Notification) {
        notificationService.markAsRead(notification.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { navigateToNotificationDetails(notification) }
            .subscribe({}, { Timber.e(it) })
    }

    fun onClickTheme(theme: Theme) {
        view.navigateToThemeReports(theme)
    }

    fun isSelectedProject(project: Project): Boolean = selectedProject?.id == project.id

    fun getAvatarPositionFromPreferences(): Int =
        preferences.getString(User.AVATAR).extractNumbers().toInt().minus(1) // minus() being used to get the correct position from resources Array

    private fun loadData() {
        homeInteractor.getProjects(userId)
            .doOnSubscribe { view.showLoading() }
            .doOnTerminate { view.dismissLoading() }
            .doOnNext { fillProjectsAdapter(it) }
            .filter { it.isNotEmpty() }
            .doOnNext { selectedProject = it.first() }
            .observeOn(Schedulers.io())
            .flatMap { homeInteractor.getThemes(selectedProject!!.id, userId) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ fillThemesAdapter(it) }, { Timber.e(it) })
    }

    private fun loadThemesData(projectId: Int) {
        homeInteractor.getThemes(projectId, userId)
            .doOnSubscribe { view.showLoading() }
            .doOnTerminate { view.dismissLoading() }
            .subscribe({ fillThemesAdapter(it) }, { Timber.e(it) })
    }

    private fun loadNotifications() {
        notificationService.getNotifications()
            .compose(RxHelper.defaultFlowableSchedulers())
            .subscribe({
                view.fillNotificationAdapter(it)
            }, {
                Timber.e(it)
            })
    }

    private fun fillProjectsAdapter(projects: MutableList<Project>) {
        view.fillProjectsAdapter(projects)
    }

    private fun fillThemesAdapter(themes: MutableList<Theme>) {
        view.fillThemesAdapter(themes)
    }

    private fun navigateToNotificationDetails(notification: Notification) {
        notification.read = true
        view.navigateToNotificationDetail(notification)
    }

}