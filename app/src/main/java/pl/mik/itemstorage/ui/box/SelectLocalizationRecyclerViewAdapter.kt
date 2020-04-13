import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.listitem_radio_button.view.*
import pl.mik.itemstorage.database.App
import pl.mik.itemstorage.R
import pl.mik.itemstorage.ui.box.SelectLocalizationItemViewHolder
import pl.mik.itemstorage.database.entities.Localization


class SelectLocalizationRecyclerViewAdapter(selectedLocalization: Localization?) :
    RecyclerView.Adapter<SelectLocalizationItemViewHolder>() {

    private val locs: ArrayList<Localization> = ArrayList()
    private var lastChecked: Localization? = null
    private var lastCheckedPos = -1

    init {
        App.database?.localizations()?.getAll()?.let { locs.addAll(it) }
        selectedLocalization?.let {lastChecked}
        selectedLocalization?.let {lastCheckedPos = locs.indexOf(it)}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): SelectLocalizationItemViewHolder {
        return SelectLocalizationItemViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.listitem_radio_button, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return locs.size
    }

    override fun onBindViewHolder(holder: SelectLocalizationItemViewHolder, position: Int) {
        val loc: Localization = locs[position]
        holder.bind(locs[position].name, position == lastCheckedPos)
        holder.itemView.list_item_radio_button.isClickable = false

        holder.itemView.setOnClickListener{
            lastChecked = loc
            lastCheckedPos = position
            notifyDataSetChanged()
        }
    }

    fun getSelectedLocalization(): Localization? {
        return lastChecked
    }
}