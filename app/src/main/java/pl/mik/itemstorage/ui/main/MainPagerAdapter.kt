package pl.mik.itemstorage.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import pl.mik.itemstorage.ui.main.boxes.BoxesFragment
import pl.mik.itemstorage.ui.main.items.ItemsFragment

class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> ItemsFragment()
            else -> BoxesFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Items"
            else -> "Boxes"
        }
    }

    override fun getCount(): Int {
        return 2
    }
}