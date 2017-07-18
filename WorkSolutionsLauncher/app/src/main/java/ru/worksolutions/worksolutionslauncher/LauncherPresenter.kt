package ru.worksolutions.worksolutionslauncher

import android.appwidget.AppWidgetHostView
import android.graphics.drawable.Drawable

class LauncherPresenter {
    private var view: IViewWithApps? = null
    private val apps = loadInBackground(App.get())

    private val pageCount = apps.count() / 12 + 1

    private var currentPageNumber = -1

    private val backgrounds: HashMap<Int, Drawable> = hashMapOf()
    private val widgets: HashMap<Int, AppWidgetHostView> = hashMapOf()

    fun bindView(view: IViewWithApps, pageNumber: Int) {
        this.view = view
        currentPageNumber = pageNumber
        view.initialize(apps.drop(pageNumber * 12).take(12))

        if (backgrounds.containsKey(currentPageNumber))
            view.setBackground(backgrounds.get(currentPageNumber)!!)
        else
            view.setBackground(getWallpaper(App.get()))

        if (widgets.containsKey(currentPageNumber))
            view.setWidgets(widgets.get(currentPageNumber)!!)
    }

    fun onBackgroundSelect(drawable: Drawable) {
        if (backgrounds.containsKey(currentPageNumber))
            backgrounds.remove(currentPageNumber)
        backgrounds.put(currentPageNumber, drawable)

        view?.setBackground(drawable)
    }

    fun unbindView() {
        view = null
    }

    fun addWidget(widgetHostView: AppWidgetHostView) {
        widgets.put(currentPageNumber, widgetHostView)
        view?.setWidgets(widgetHostView)
    }

    companion object {
        val presenter = LauncherPresenter()
    }

}