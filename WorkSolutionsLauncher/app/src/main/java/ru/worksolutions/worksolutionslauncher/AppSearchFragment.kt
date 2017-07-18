package ru.worksolutions.worksolutionslauncher

import android.appwidget.AppWidgetHostView
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class AppSearchFragment : Fragment(), IViewWithApps {
    private lateinit var apps: List<AppModel>
    private val adapter = ApplicationAdapter(this)

    private lateinit var searchView: SearchView;
    private lateinit var rvApps: RecyclerView

    override fun onAppSelect(app: AppModel) {
        activity.packageManager.getLaunchIntentForPackage(app.applicationPackageName)?.let { startActivity(it) }
    }

    override fun initialize(apps: List<AppModel>) {
        adapter.setData(apps)
    }

    override fun setWidgets(widget: AppWidgetHostView) {}

    override fun setBackground(drawable: Drawable) {}

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        apps = loadInBackground(context)
        return inflater.inflate(R.layout.fmt_all_apps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = view.findViewById(R.id.sv_apps)
        searchView.setIconifiedByDefault(false)
        searchView.setOnQueryTextListener(object : android.support.v7.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchApps(newText)
                return false
            }
        })


        rvApps = view.findViewById(R.id.rv_all_apps)

        rvApps.layoutManager = GridLayoutManager(context, 5)
        rvApps.adapter = adapter

        adapter.setData(apps)
    }

    fun searchApps(pattern: String) {
        adapter.setData(apps.filter { app -> app.label.toLowerCase().contains(pattern) })
    }

}