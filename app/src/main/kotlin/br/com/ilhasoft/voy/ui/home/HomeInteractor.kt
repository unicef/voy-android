package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.voy.models.Notification
import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.models.Theme
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

/**
 * Created by lucas on 07/02/18.
 */
interface HomeInteractor {

    fun getProjects(userId: Int, lang: String): Flowable<MutableList<Project>>
    fun getThemes(projectId: Int, userId: Int, lang: String): Flowable<MutableList<Theme>>
    fun getTheme(themeId: Int, lang: String): Maybe<Theme>
    fun getNotifications(): Flowable<List<Notification>>
    fun markAsRead(notificationId: Int): Completable
}