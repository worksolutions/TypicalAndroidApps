package ru.worksolutions.worksolutionslauncher

import android.app.WallpaperManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import java.text.Collator
import java.util.Comparator


fun getWallpaper(context: Context): Drawable = WallpaperManager.getInstance(context).drawable

fun loadInBackground(context: Context): List<AppModel> {

    val ALPHA_COMPARATOR: Comparator<AppModel> = object : Comparator<AppModel> {
        private val sCollator = Collator.getInstance()
        override fun compare(object1: AppModel, object2: AppModel): Int {
            return sCollator.compare(object1.getLabel(), object2.getLabel())
        }
    }

    return (context.packageManager.getInstalledApplications(0) ?: ArrayList<ApplicationInfo>())
            .filter { appInfo -> context.packageManager.getLaunchIntentForPackage(appInfo.packageName) != null }
            .map { appInfo ->
                val app = AppModel(context, appInfo)
                app.loadLabel(context)
                return@map app
            }
            .sortedWith(ALPHA_COMPARATOR).toList()



}