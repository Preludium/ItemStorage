package pl.mik.itemstorage.ui.images

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.listitem_image.view.*
import pl.mik.itemstorage.database.App
import pl.mik.itemstorage.R
import pl.mik.itemstorage.database.entities.ImageBox
import pl.mik.itemstorage.database.entities.ImageItem

class ImagesRecyclerViewAdapter(val images: ArrayList<Any>) : RecyclerView.Adapter<ImagesItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesItemViewHolder {
        return ImagesItemViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.listitem_image, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: ImagesItemViewHolder, position: Int) {
        holder.bind(images[position])
        holder.itemView.image_delete.setOnClickListener {
            val builder = AlertDialog.Builder(it.context)
            builder.setMessage("Are you sure you want to this image?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    val image = images.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, images.size)
                    when (image){
                        is ImageBox -> {
                            App.database?.imagesBox()?.delete(image)
                        }

                        is ImageItem -> {
                            App.database?.imagesItem()?.delete(image)
                        }
                    }

                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }
    }
}