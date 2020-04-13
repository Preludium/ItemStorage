package pl.mik.itemstorage.ui.main.boxes

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.mik.itemstorage.R
import pl.mik.itemstorage.database.entities.Box

class BoxesItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.list_item_title)
    val desc: TextView = itemView.findViewById(R.id.list_item_desc)
    val image: ImageView = itemView.findViewById(R.id.list_item_image)
    val delete: Button = itemView.findViewById(R.id.list_item_button)

    fun bind(box: Box) {
        this.title.text = box.name
        this.desc.text = box.description
    }
}