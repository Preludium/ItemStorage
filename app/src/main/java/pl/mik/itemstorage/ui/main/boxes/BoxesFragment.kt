package pl.mik.itemstorage.ui.main.boxes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.mik.itemstorage.R
import pl.mik.itemstorage.ui.main.TabSwitch


class BoxesFragment : Fragment(), TabSwitch {

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_localizations, container, false)
        recyclerView = view.findViewById(R.id.localizations_recycler)
        recyclerView.layoutManager = LinearLayoutManager(activity)
//        recyclerView.addItemDecoration(DividerItemDecoration(context, (recyclerView.layoutManager as LinearLayoutManager).orientation))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = BoxesRecyclerViewAdapter()

        return view
    }

    override fun onResume() {
        super.onResume()
        recyclerView = view!!.findViewById(R.id.localizations_recycler)
        recyclerView.layoutManager = LinearLayoutManager(activity)
//        recyclerView.addItemDecoration(DividerItemDecoration(context, (recyclerView.layoutManager as LinearLayoutManager).orientation))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = BoxesRecyclerViewAdapter()
    }

    override fun fragmentBecameVisible() {
        recyclerView = view!!.findViewById(R.id.localizations_recycler)
        recyclerView.layoutManager = LinearLayoutManager(activity)
//        recyclerView.addItemDecoration(DividerItemDecoration(context, (recyclerView.layoutManager as LinearLayoutManager).orientation))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = BoxesRecyclerViewAdapter()
    }
}
