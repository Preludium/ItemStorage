package pl.mik.itemstorage.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*
import pl.mik.itemstorage.R
import pl.mik.itemstorage.ui.box.NewBoxActivity
import pl.mik.itemstorage.ui.item.NewItemActivity
import pl.mik.itemstorage.ui.localization.NewLocalizationActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        view_pager.adapter =
            MainPagerAdapter(supportFragmentManager)
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
                } else {
                    new_localization_fab.show()
                    main_fab.setImageResource(R.drawable.ic_box_white_18dp)
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
}
