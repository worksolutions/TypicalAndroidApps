package ru.worksolutions.worksolutionslauncher

import android.app.Activity.RESULT_OK
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import ru.worksolutions.worksolutionslauncher.HomeActivity.Companion.fragments

class HomeFragment : Fragment() {

    private lateinit var viewPager: ViewPager;

    private lateinit var mAppWidgetHost: AppWidgetHost
    private lateinit var mAppWidgetManager: AppWidgetManager

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fmt_home, container, false)
    }

    private val REQUEST_CREATE_APPWIDGET = 3
    private val REQUEST_PICK_APPWIDGET = 2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAppWidgetManager = AppWidgetManager.getInstance(context)
        mAppWidgetHost = AppWidgetHost(context, 1)

        val pageCount = (loadInBackground(context).size / 12) + 1

        view.findViewById<ImageView>(R.id.iv_menu).setOnClickListener {
            val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i, 100)
        }

        view.findViewById<ImageView>(R.id.iv_widgets).setOnClickListener {

            selectWidget()

//            val i = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(i, 200)
        }


        view.findViewById<SearchView>(R.id.sw_apps).setOnClickListener {
            activity.supportFragmentManager.beginTransaction()
                    .add(R.id.container, AppSearchFragment())
                    .addToBackStack(null)
                    .commit()
        }

        viewPager = view.findViewById<ViewPager>(R.id.vpager_launcher)
        viewPager.adapter = LauncherPagerAdapter(activity.supportFragmentManager, pageCount)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                LauncherPresenter.presenter.bindView(fragments[position], position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != RESULT_OK && data != null)
            return

        when (requestCode) {
            100 -> {
                val selectedImage: Uri = data!!.data;
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = context.contentResolver.query(selectedImage,filePathColumn, null, null, null);
                cursor.moveToFirst();
                val columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                val picturePath = cursor.getString(columnIndex);
                cursor.close();

                LauncherPresenter.presenter.onBackgroundSelect(Drawable.createFromPath(picturePath))
            }
            REQUEST_PICK_APPWIDGET -> configureWidget(data!!)
            REQUEST_CREATE_APPWIDGET -> createWidget(data!!)

        }
    }

    private fun configureWidget(data: Intent) {
        val extras = data.extras
        val appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        val appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId)
        if (appWidgetInfo.configure != null) {
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE)
            intent.component = appWidgetInfo.configure
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
//            startActivityForResult(intent, REQUEST_CREATE_APPWIDGET)
        } else {
            createWidget(data)
        }
    }

    fun selectWidget() {
        val appWidgetId = this.mAppWidgetHost.allocateAppWidgetId()
        val pickIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_PICK)
        pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        addEmptyData(pickIntent)
        startActivityForResult(pickIntent, REQUEST_PICK_APPWIDGET)
    }

    fun addEmptyData(pickIntent: Intent) {
        val customInfo = arrayListOf<Parcelable>()
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_INFO, customInfo)
        val customExtras = arrayListOf<Parcelable>()
        pickIntent.putParcelableArrayListExtra(AppWidgetManager.EXTRA_CUSTOM_EXTRAS, customExtras)
    };

    fun createWidget(data: Intent) {

        val appWidgetId = data.extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1)
        val appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId)
        val hostView = mAppWidgetHost.createView(context, appWidgetId, appWidgetInfo)
        hostView.setAppWidget(appWidgetId, appWidgetInfo)
        LauncherPresenter.presenter.addWidget(hostView)
    }

}
