package pl.mik.itemstorage.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import pl.mik.itemstorage.ui.scanner.ScannerActivity


class MainActivity : AppCompatActivity() {
    private val SCANNER_RC = 0
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

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    new_localization_fab.hide()
                    main_fab.setImageResource(android.R.drawable.ic_input_add)
                    val fragment = (view_pager.adapter as MainPagerAdapter).instantiateItem(
                        view_pager,
                        position
                    ) as TabSwitch
                    fragment.fragmentBecameVisible()
                } else {
                    new_localization_fab.show()
                    main_fab.setImageResource(R.drawable.ic_box_white_18dp)
                    val fragment = (view_pager.adapter as MainPagerAdapter).instantiateItem(
                        view_pager,
                        position
                    ) as TabSwitch
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
        searchView.setQuery("", true);
        searchView.clearFocus();
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        searchView = menu?.findItem(R.id.menu_search)?.actionView as SearchView
        configureSearch(searchView)
        searchView.isIconifiedByDefault = true
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_qr_code -> {
                searchCode()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun searchCode() {
        startActivityForResult(Intent(applicationContext, ScannerActivity::class.java), SCANNER_RC)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SCANNER_RC) {
                searchView.isIconified = false
                searchView.setQuery(data?.extras?.get("CODE") as String, true)
                searchView.clearFocus()
            }
        }
    }

    private fun configureSearch(searchView: SearchView) {
        searchView.setOnSearchClickListener {
            view_pager.currentItem = 0
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (view_pager.currentItem != 0)
                    view_pager.currentItem = 0

                itemsFragment.getAdapter().filter.filter(query)
                searchView.clearFocus()
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
