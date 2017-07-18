package ru.worksolutions.worksolutionslauncher

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import ru.worksolutions.worksolutionslauncher.HomeActivity.Companion.fragments
import android.R.attr.data




class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fmt_launcher)

    if (savedInstanceState == null)
        supportFragmentManager.beginTransaction().replace(R.id.container, HomeFragment()).commit()
    }

    companion object {
        lateinit var fragments: List<TableAppsFragment>
    }

}

class LauncherPagerAdapter(private val fragmentManager: FragmentManager,
                           private val pageCount: Int) : FragmentPagerAdapter(fragmentManager) {

    init {
        fragments = (0..pageCount - 1).map { pageNumber -> TableAppsFragment.newInstance(pageNumber) }
    }


    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int = fragments.size
}


