package com.albasil.finalprojectkotlinbootcamp.UI
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.albasil.finalprojectkotlinbootcamp.Adapter.TapLayOut
import com.albasil.finalprojectkotlinbootcamp.R
import com.albasil.finalprojectkotlinbootcamp.SecondFragment.FavoriteFragment
import com.albasil.finalprojectkotlinbootcamp.SecondFragment.AllUsersFragment
import com.google.android.material.tabs.TabLayout


    class TabBarFragment : Fragment() {
        var myFragment: View? = null
        var viewPager: ViewPager? = null
        var tabLayout: TabLayout? = null
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            myFragment = inflater.inflate(R.layout.fragment_tab_bar, container, false)
            viewPager = myFragment!!.findViewById(R.id.viewPager)
            tabLayout = myFragment!!.findViewById(R.id.tabLayout)
            return myFragment
        }

        //Call onActivity Create method
        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            setUpViewPager(viewPager)
            tabLayout!!.setupWithViewPager(viewPager)
            tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {}
                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }

        private fun setUpViewPager(viewPager: ViewPager?) {
            val adapter = TapLayOut(childFragmentManager)
            adapter.addFragment(HomePage(), getString(R.string.articles))
            adapter.addFragment(FavoriteFragment(), getString(R.string.myFavorite))
            adapter.addFragment(AllUsersFragment(),getString(R.string.users) )

            viewPager!!.adapter = adapter
        }

        companion object {
            val instance: TabBarFragment
                get() = TabBarFragment()
        }
    }
