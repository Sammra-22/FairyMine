package com.nordic.fairymine.common.adapter


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


/**
 * Created by Sam22 on 1/6/18.
 */
class AppPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = LinkedHashMap<String, Fragment>()

    override fun getItem(position: Int): Fragment? {
        val pageTitle: CharSequence? = getPageTitle(position)
        return pageTitle?.let {
            fragments[pageTitle.toString()]
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragments.keys.toList()[position]
    }

    override fun getCount(): Int = fragments.size

    fun addFragment(fragment: Fragment, title: String) {
        fragments.put(title, fragment)
        notifyDataSetChanged()
    }

    fun getFragment(title: String): Fragment? {
        return fragments[title]
    }

    fun getAllFragments(): List<Fragment> = fragments.values.toList()

}