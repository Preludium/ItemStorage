import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.listitem_radio_button.view.*
import pl.mik.itemstorage.database.App
import pl.mik.itemstorage.R
import pl.mik.itemstorage.database.entities.Box
import pl.mik.itemstorage.ui.box.SelectLocalizationItemViewHolder
import pl.mik.itemstorage.database.entities.Localization
import pl.mik.itemstorage.ui.item.SelectBoxItemViewHolder


class SelectBoxRecyclerViewAdapter(selectedBox: Box?) :
    RecyclerView.Adapter<SelectBoxItemViewHolder>() {

    private val boxes: ArrayList<Box> = ArrayList()
    private var lastChecked: Box? = null
    private var lastCheckedPos = -1

    init {
        boxes.addAll(App.database!!.boxes().getAllByUserId(App.session!!.userId).sortedBy { it.name })
        if (selectedBox != null) {
            lastChecked = boxes.find { it.id == selectedBox.id}
            lastCheckedPos = boxes.indexOf(selectedBox)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): SelectBoxItemViewHolder {
        return SelectBoxItemViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.listitem_radio_button, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return boxes.size
    }

    override fun onBindViewHolder(holder: SelectBoxItemViewHolder, position: Int) {
        val loc: Box = boxes[position]
        holder.bind(boxes[position].name, position == lastCheckedPos)
        holder.itemView.list_item_radio_button.isClickable = false

        holder.itemView.setOnClickListener{
            lastChecked = loc
            lastCheckedPos = position
            notifyDataSetChanged()
        }
    }

    fun getSelectedBox(): Box? {
        return lastChecked
    }
}