package com.albasil.finalprojectkotlinbootcamp.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.albasil.finalprojectkotlinbootcamp.databinding.FragmentHomePageBinding

class TabAdapter(supportFragmentManager: FragmentManager):
FragmentPagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){


    private val mFragmentList=ArrayList<Fragment>()
    private val mFragmentTitleList=ArrayList<String>()
    override fun getCount(): Int {

        return mFragmentList.size
    }

    override fun getItem(position: Int): Fragment {


        return mFragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
      //  return super.getPageTitle(position)
        return mFragmentTitleList[position]
    }

    fun addFragment(fragment: Fragment,title:String){

        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)

    }


}