package pl.mik.itemstorage.ui.main.items

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import pl.mik.itemstorage.database.App
import pl.mik.itemstorage.R
import pl.mik.itemstorage.ui.box.NewBoxActivity
import pl.mik.itemstorage.database.entities.Box
import pl.mik.itemstorage.database.entities.Item
import pl.mik.itemstorage.database.entities.Localization
import pl.mik.itemstorage.ui.item.NewItemActivity
import pl.mik.itemstorage.ui.localization.NewLocalizationActivity
import pl.mik.itemstorage.ui.main.boxes.BoxesGroupItemViewHolder
import java.util.*
import kotlin.collections.ArrayList

class ItemsRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {
    private val ITEM_VIEW = 0
    private val SECTION_VIEW = 1
    private var flattenList: ArrayList<Any> = ArrayList()
    private val emptyBox = Box("Others", null, null, App.session?.userId!!, 0)

    init {
        createList()
    }

    private fun createList() {
        flattenList = ArrayList()
        val boxes = App.database?.boxes()?.getAllByUserId(App.session!!.userId)?.sortedBy { it.name }
        if (boxes != null) {
            for (box in boxes) {
                flattenList.add(box)
                for (item in App.database?.items()?.getAllItemsByBoxId(box.id)!!.sortedBy { it.name }) {
                    flattenList.add(item)
                }
            }
        }
        if (!App.database?.items()?.getAll().isNullOrEmpty() && !flattenList.containsAll(App.database?.items()?.getAll()!!)) {
            flattenList.add(emptyBox)
            for (item in App.database?.items()?.getAll()!!) {
                if (!flattenList.contains(item)) {
                    flattenList.add(item)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (flattenList[position] is Box)
            SECTION_VIEW
        else
            ITEM_VIEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_VIEW)
            ItemsItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.listitem_item, parent, false)
            )
        else
            ItemsGroupItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.listitem_group, parent, false)
            )
    }

    override fun getItemCount(): Int {
        return flattenList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM_VIEW -> {
                val item = flattenList[position] as Item
                val itemHolder = holder as ItemsItemViewHolder
                itemHolder.bind(item)
                itemHolder.itemView.setOnClickListener {
                    it.context.startActivity(Intent(it.context, NewItemActivity::class.java).putExtra("Item", item))
                }

                itemHolder.delete.setOnClickListener {
                    showDeleteDialog(it.context, item, position)
                }

                itemHolder.itemView.setOnLongClickListener {
                    showDeleteDialog(it.context, item, position)
                    true
                }
            }

            SECTION_VIEW -> {
                val box = flattenList[position] as Box
                val sectionHolder = holder as ItemsGroupItemViewHolder
                sectionHolder.bind(box.name)
                sectionHolder.itemView.setOnClickListener {
                    if (box != emptyBox) {
                        it.context.startActivity(Intent(it.context, NewBoxActivity::class.java).putExtra("Box", box))
                    }
                }

//                sectionHolder.delete.setOnClickListener {
//                    showDeleteDialog(it.context, box, position)
//                }

                sectionHolder.itemView.setOnLongClickListener {
                    if (box != emptyBox) {
                        showDeleteDialog(it.context, box, position)
                    }
                    true
                }
            }
        }
    }

    private fun showDeleteDialog(context: Context, item: Any, position: Int) {
        when (item) {
            is Box -> {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to delete ${item.name} box?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        for (iitem in App.database?.items()?.getAllItemsByBoxId(item.id)!!) {
                            iitem.boxId = null
                            App.database?.items()?.update(iitem)
                        }
                        App.database?.boxes()?.delete(item)
                        createList()
                        notifyDataSetChanged()
                    }
                    .setNegativeButton("No") { dialog, id ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }
            is Item -> {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to delete ${item.name}?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        flattenList.removeAt(position)
                        if (flattenList.contains(emptyBox) && flattenList.size - 1 == flattenList.indexOf(emptyBox)) {
                            flattenList.removeAt(flattenList.indexOf(emptyBox))
                            notifyDataSetChanged()
                        } else {
                            notifyItemRemoved(position)
                            notifyItemRangeChanged(position, flattenList.size)
                        }
                        App.database?.items()?.delete(item)
                    }
                    .setNegativeButton("No") { dialog, id ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchString = constraint.toString().toLowerCase()
                if (constraint.isNullOrEmpty()) {
                    createList()
                }
                else {
                    createList()
                    var helpList = ArrayList<Any>()
                    loop@ for (item in flattenList) {
                        when (item) {
                            is Box -> {
                                continue@loop
                            }

                            is Item -> {
                                if ((item.ean_upc_code != null && item.ean_upc_code!!.toLowerCase().contains(searchString))
                                    || item.name.toLowerCase().contains(searchString)
                                    || item.description!!.toLowerCase().contains(searchString)
                                    || item.category!!.toLowerCase().contains(searchString)) {

                                    helpList.add(item)
                                }
                            }
                        }
                    }
                    flattenList = helpList
                }

                val filterResults = FilterResults()
                filterResults.values = flattenList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                flattenList = results!!.values as ArrayList<Any>
                notifyDataSetChanged()
            }
        }
    }
}