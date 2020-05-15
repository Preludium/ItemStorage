package pl.mik.itemstorage.ui.main.boxes

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
import pl.mik.itemstorage.database.entities.Localization
import pl.mik.itemstorage.ui.localization.NewLocalizationActivity
import kotlin.collections.ArrayList

class BoxesRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ITEM_VIEW = 0
    private val SECTION_VIEW = 1
    private var flattenList: ArrayList<Any> = ArrayList()

    init {
        createList()
    }

    private fun createList() {
        flattenList = ArrayList()
        val locals = App.database?.localizations()?.getAllByUserId(App.session!!.userId)?.sortedBy { it.name }
        if (locals != null) {
            for (item in locals) {
                flattenList.add(item)
                for (box in App.database?.boxes()?.getAllBoxesByLocalizationId(item.id)!!.sortedBy { it.name }) {
                    flattenList.add(box)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (flattenList[position] is Localization)
            SECTION_VIEW
        else
            ITEM_VIEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_VIEW)
            BoxesItemViewHolder(
                LayoutInflater.from(
                    parent.context
                ).inflate(R.layout.listitem_item, parent, false)
            )
        else
            BoxesGroupItemViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.listitem_group, parent, false)
            )
    }

    override fun getItemCount(): Int {
        return flattenList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM_VIEW -> {
                val box = flattenList[position] as Box
                val itemHolder = holder as BoxesItemViewHolder
                itemHolder.bind(box)
                itemHolder.itemView.setOnClickListener {
                    it.context.startActivity(Intent(it.context, NewBoxActivity::class.java).putExtra("Box", box))
                }

                itemHolder.delete.setOnClickListener {
                    showDeleteDialog(it.context, box, position)
                }

                itemHolder.itemView.setOnLongClickListener {
                    showDeleteDialog(it.context, box, position)
                    true
                }
            }

            SECTION_VIEW -> {
                val local = flattenList[position] as Localization
                val sectionHolder = holder as BoxesGroupItemViewHolder
                sectionHolder.bind(local.name)

                sectionHolder.itemView.setOnClickListener {
                    it.context.startActivity(Intent(it.context, NewLocalizationActivity::class.java).putExtra("Localization", local))
                }

//                sectionHolder.delete.setOnClickListener {
//                    showDeleteDialog(it.context, local, position)
//                }

                sectionHolder.itemView.setOnLongClickListener {
                    showDeleteDialog(it.context, local, position)
                    true
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
                        flattenList.removeAt(position)
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
            is Localization -> {
                val builder = AlertDialog.Builder(context)
                builder.setMessage("Are you sure you want to delete ${item.name} localization with all boxes in it?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        App.database?.localizations()?.delete(item)
                        createList()
                        notifyDataSetChanged()
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

