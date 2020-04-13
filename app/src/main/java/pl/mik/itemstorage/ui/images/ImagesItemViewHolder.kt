package pl.mik.itemstorage.ui.images

import android.graphics.BitmapFactory
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pl.mik.itemstorage.R
import pl.mik.itemstorage.database.entities.ImageBox
import pl.mik.itemstorage.database.entities.ImageItem

class ImagesItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.item_image_view)
    private val createdDate: TextView = itemView.findViewById(R.id.image_date)

    fun bind(image: Any) {
        when (image) {
            is ImageBox -> {
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(image.image, 0, image.image.size))
                createdDate.text = image.createdAt.toLocaleString()
            }
            is ImageItem -> {
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(image.image, 0, image.image.size))
                createdDate.text = image.createdAt.toLocaleString()
            }
        }
    }
}