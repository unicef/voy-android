package br.com.ilhasoft.voy.db.theme

import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.shared.extensions.extractNumbers
import io.reactivex.Flowable
import io.realm.Realm

/**
 * Created by lucasbarros on 08/02/18.
 */
class ThemeDbHelper {
    private val realm by lazy { Realm.getDefaultInstance() }

    fun getThemes(project: Int): Flowable<MutableList<Theme>> {
        //TODO: Refactor to include realm with RxJava
        return Flowable.fromCallable {
            val localThemes = realm.where(ThemeDbModel::class.java).equalTo("projectId", project).findAll()
            localThemes.map { it.toTheme() }.toMutableList()
        }
    }

    fun saveThemes(themes: MutableList<Theme>): Flowable<MutableList<Theme>> {
        //TODO: Refactor to include realm with RxJava
        return Flowable.fromCallable {
            var themesDb = themes.map { theme ->
                ThemeDbModel().apply {
                    id = theme.id
                    projectId = theme.project.extractNumbers().toInt()
                    name = theme.name
                    bounds.addAll(theme.bounds.map { bound ->
                        BoundDbModel().apply {
                            lat = bound[0]
                            lng = bound[1]
                        }
                    })
                    tags.addAll(theme.tags.map { title -> TagDbModel().apply { tag = title } })
                    color = theme.color
                    allowLinks = theme.allowLinks
                }
            }
            realm.beginTransaction()
            themesDb = realm.copyToRealmOrUpdate(themesDb)
            realm.commitTransaction()
            themesDb.map { it.toTheme() }.toMutableList()
        }
    }

    fun getThemeTags(themeId: Int): Flowable<MutableList<String>> {
        return Flowable.fromCallable {
           realm.beginTransaction()
            val theme = realm.where(ThemeDbModel::class.java).equalTo("id", themeId).findFirst()
            theme?.tags?.map { it.tag }?.toMutableList() ?: mutableListOf()
        }
    }
}