package pl.mik.itemstorage.ui.main.items

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.mik.itemstorage.R
import pl.mik.itemstorage.ui.main.TabSwitch

class ItemsFragment : Fragment(), TabSwitch{

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_localizations, container, false)
        recyclerView = view.findViewById(R.id.localizations_recycler)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        recyclerView.adapter = ItemsRecyclerViewAdapter()
        return view
    }

    override fun onResume() {
        super.onResume()
        recyclerView.adapter = ItemsRecyclerViewAdapter()

    }

    override fun fragmentBecameVisible() {
        recyclerView.adapter = ItemsRecyclerViewAdapter()
    }

    fun getAdapter(): ItemsRecyclerViewAdapter {
        return recyclerView.adapter as ItemsRecyclerViewAdapter
    }
}
