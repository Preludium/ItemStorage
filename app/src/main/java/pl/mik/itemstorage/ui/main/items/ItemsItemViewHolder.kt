package pl.mik.itemstorage.ui.main.items

import android.graphics.BitmapFactory
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.mik.itemstorage.R
import pl.mik.itemstorage.database.App
import pl.mik.itemstorage.database.entities.Box
import pl.mik.itemstorage.database.entities.ImageItem
import pl.mik.itemstorage.database.entities.Item

class ItemsItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.list_item_title)
    val desc: TextView = itemView.findViewById(R.id.list_item_desc)
    val image: ImageView = itemView.findViewById(R.id.list_item_image)
    val delete: Button = itemView.findViewById(R.id.list_item_button)

    fun bind(item: Item) {
        this.title.text = item.name
        this.desc.text = item.description
        val images = App.database?.imagesItem()?.getImagesByItemId(item.id) as List<ImageItem>
        if (images.isNotEmpty()) {
            this.image.setImageBitmap(
                BitmapFactory.decodeByteArray(
                    images[0].image,
                    0,
                    images[0].image.size
                )
            )
        }
        else {
            this.image.setImageResource(R.mipmap.ic_launcher)
        }
    }
}