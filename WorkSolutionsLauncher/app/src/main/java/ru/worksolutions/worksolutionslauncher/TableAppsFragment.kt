package ru.worksolutions.worksolutionslauncher

import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import java.text.Collator
import java.util.*
import kotlin.collections.ArrayList
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Parcelable
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.appwidget.AppWidgetHostView
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.LinearLayout

interface IViewWithApps {
    fun onAppSelect(app: AppModel)
    fun initialize(apps: List<AppModel>)
    fun setBackground(drawable: Drawable)
    fun setWidgets(widget: AppWidgetHostView)
}

class TableAppsFragment : Fragment(), IViewWithApps {

    private lateinit var rvApps: RecyclerView
    //    private lateinit var searchViewApps: SearchView
    private lateinit var ivWidgets: ImageView
    private lateinit var allApplications: List<AppModel>
    private var pageNumber: Int = 0

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        mAppWidgetManager = AppWidgetManager.getInstance(context)
        mAppWidgetHost = AppWidgetHost(context, 1)

        pageNumber = arguments.getInt("PageNumber")

        return inflater.inflate(R.layout.fmt_table_apps, container, false)
    }

    override fun onAppSelect(app: AppModel) {
        val intent = activity.packageManager.getLaunchIntentForPackage(app.applicationPackageName)
        if (intent != null)
            startActivity(intent)

    }

    override fun setBackground(drawable: Drawable) {
        view!!.findViewById<FrameLayout>(R.id.fl_table).background = drawable
    }

    override fun initialize(apps: List<AppModel>) {
        adapter.setData(apps)
    }

    private val adapter = ApplicationAdapter(this)
    private lateinit var mAppWidgetHost: AppWidgetHost
    private lateinit var mAppWidgetManager: AppWidgetManager


    fun addEmptyData(pickIntent: Intent) {
        val customInfo = arrayListOf<Parcelable>()
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo)
        val customExtras = arrayListOf<Parcelable>()
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras)
    };

    override fun setWidgets(widget: AppWidgetHostView) {
        with(view!!.findViewById<LinearLayout>(R.id.ll_widgets)) {
            if (childCount == 0)
                addView(widget)
//            removeView(widget)
//            count = childCount

        }

//        view!!.findViewById<LinearLayout>(R.id.ll_widgets).addView(widget)
    }

    fun createWidget(data: Intent) {

        val appWidgetId = data.extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        val appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId)
        val hostView = mAppWidgetHost.createView(context, appWidgetId, appWidgetInfo)
        hostView.setAppWidget(appWidgetId, appWidgetInfo)
        LauncherPresenter.presenter.addWidget(hostView)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<FrameLayout>(R.id.fl_table).background = getWallpaper(context)

        rvApps = view.findViewById<RecyclerView>(R.id.rv_table_apps)

        rvApps.layoutManager = GridLayoutManager(context, 4)
//        rvApps.layoutManager = StaggeredGridLayoutManager(4, 1);

        rvApps.adapter = adapter

//        LauncherPresenter.presenter.bindView(this, pageNumber)


    }

    override fun onDestroyView() {
        super.onDestroyView()
//        LauncherPresenter.presenter.unbindView()
    }

    fun loadInBackground(): List<AppModel> {

        return (context.packageManager.getInstalledApplications(0) ?: ArrayList<ApplicationInfo>())
                .filter { appInfo -> context.packageManager.getLaunchIntentForPackage(appInfo.packageName) != null }
                .map { appInfo ->
                    val app = AppModel(context, appInfo)
                    app.loadLabel(context)
                    return@map app
                }
                .sortedWith(ALPHA_COMPARATOR).toList()

    }

    val ALPHA_COMPARATOR: Comparator<AppModel> = object : Comparator<AppModel> {
        private val sCollator = Collator.getInstance()
        override fun compare(object1: AppModel, object2: AppModel): Int {
            return sCollator.compare(object1.getLabel(), object2.getLabel())
        }
    }

    companion object {
        fun newInstance(pageNumber: Int): TableAppsFragment {
            val fmt = TableAppsFragment()

            val args = Bundle()
            args.putInt("PageNumber", pageNumber)

            fmt.arguments = args

            return fmt
        }
    }

}