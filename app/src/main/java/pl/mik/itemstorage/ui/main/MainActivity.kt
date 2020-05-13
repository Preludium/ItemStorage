package pl.mik.itemstorage.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*
import pl.mik.itemstorage.R
import pl.mik.itemstorage.ui.box.NewBoxActivity
import pl.mik.itemstorage.ui.item.NewItemActivity
import pl.mik.itemstorage.ui.localization.NewLocalizationActivity
import pl.mik.itemstorage.ui.main.boxes.BoxesFragment
import pl.mik.itemstorage.ui.main.items.ItemsFragment


class MainActivity : AppCompatActivity() {
    lateinit var searchView: SearchView
    lateinit var itemsFragment: ItemsFragment
    lateinit var boxesFragment: BoxesFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        view_pager.adapter = MainPagerAdapter(supportFragmentManager)
        tabs.setupWithViewPager(view_pager)

        new_localization_fab.hide()
        main_fab.show()

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if(position == 0) {
                    new_localization_fab.hide()
                    main_fab.setImageResource(android.R.drawable.ic_input_add)
                    val fragment = (view_pager.adapter as MainPagerAdapter).instantiateItem(view_pager, position) as TabSwitch
                    fragment.fragmentBecameVisible()
                } else {
                    new_localization_fab.show()
                    main_fab.setImageResource(R.drawable.ic_box_white_18dp)
                    val fragment = (view_pager.adapter as MainPagerAdapter).instantiateItem(view_pager, position) as TabSwitch
                    fragment.fragmentBecameVisible()
                }
            }
        })

        new_localization_fab.setOnClickListener {
            startActivity(Intent(applicationContext, NewLocalizationActivity::class.java))
        }

        main_fab.setOnClickListener {
            if (view_pager.currentItem == 0)
                startActivity(Intent(applicationContext, NewItemActivity::class.java))
            else
                startActivity(Intent(applicationContext, NewBoxActivity::class.java))
        }
    }

    override fun onStop() {
        super.onStop()
        searchView.setQuery("", false);
        searchView.clearFocus();
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        searchView = menu?.findItem(R.id.menu_search)?.actionView as SearchView
        configureSearch(searchView)
        return true
    }

    private fun configureSearch(searchView: SearchView)  {
        searchView.setOnSearchClickListener {
            view_pager.currentItem = 0
        }

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (view_pager.currentItem != 0)
                    view_pager.currentItem = 0

                itemsFragment.getAdapter().filter.filter(newText)
                return false
            }
        })
    }

    inner class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

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

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val createdFragment = super.instantiateItem(container, position) as Fragment
            when (position) {
                0 -> itemsFragment = createdFragment as ItemsFragment
                1 -> boxesFragment = createdFragment as BoxesFragment
            }
            return createdFragment
        }
    }
}
