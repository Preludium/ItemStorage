package pl.mik.itemstorage.ui.main.boxes

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.mik.itemstorage.R

class BoxesGroupItemViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.list_item_group_title)
    val delete: Button = itemView.findViewById(R.id.list_item_group_button)

    fun bind(title: String) {
        this.title.text = title
    }
}