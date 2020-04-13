package pl.mik.itemstorage.ui.main.items

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
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
import kotlin.collections.ArrayList

class ItemsRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ITEM_VIEW = 0
    private val SECTION_VIEW = 1
    private var flattenList: ArrayList<Any> = ArrayList()

    init {
        val boxes = App.database?.boxes()?.getAll()
        if (boxes != null) {
            for (box in boxes) {
                flattenList.add(box)
                for (item in App.database?.items()?.getAllItemsByBoxId(box.id)!!) {
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
            }

            SECTION_VIEW -> {
                val box = flattenList[position] as Box
                val sectionHolder = holder as ItemsGroupItemViewHolder
                sectionHolder.bind(box.name)
                sectionHolder.itemView.setOnClickListener {
                    it.context.startActivity(Intent(it.context, NewBoxActivity::class.java).putExtra("Box", box))
                }

                sectionHolder.delete.setOnClickListener {
                    showDeleteDialog(it.context, box, position)
                }
            }
        }
    }

    private fun showDeleteDialog(context: Context, item: Any, position: Int) {
        when (item) {
            is Box -> {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to delete ${item.name} box with all items in it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        flattenList.removeAt(position) // TODO usunac wszystkie itemy dla danego boxa w flattenList
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, flattenList.size)
                        App.database?.boxes()?.delete(item)
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
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, flattenList.size)
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
}