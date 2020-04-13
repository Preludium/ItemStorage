package pl.mik.itemstorage.ui.item

import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.mik.itemstorage.R

class SelectBoxItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val radioButton: RadioButton = itemView.findViewById(R.id.list_item_radio_button)
    private val title: TextView = itemView.findViewById(R.id.list_item_radio_button_title)

    fun bind(title: String, selected: Boolean) {
        this.title.text = title
        this.radioButton.isChecked = selected
    }
}