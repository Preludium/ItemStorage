package pl.mik.itemstorage.ui.item

import SelectBoxRecyclerViewAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import pl.mik.itemstorage.R

import kotlinx.android.synthetic.main.activity_new_item.*
import kotlinx.android.synthetic.main.content_new_item.*
import kotlinx.android.synthetic.main.select_dialog.view.*
import pl.mik.itemstorage.database.App
import pl.mik.itemstorage.database.entities.Box
import pl.mik.itemstorage.database.entities.ImageItem
import pl.mik.itemstorage.database.entities.Item
import pl.mik.itemstorage.ui.images.ImagesActivity
import pl.mik.itemstorage.ui.scanner.ScannerActivity
import kotlin.collections.ArrayList

class NewItemActivity : AppCompatActivity() {
    private val IMAGES_RC: Int = 0
    private val SCANNER_RC: Int = 1

    private var images: ArrayList<ImageItem> = ArrayList()
    private var updateItem: Item? = null
    private var selectedBox: Box? = null
    private var scannedCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_item)
        updateTextView()

        updateItem = intent.getSerializableExtra("Item") as? Item
        if (updateItem != null) {
            new_item_name.setText(updateItem!!.name)
            images = App.database?.imagesItem()?.getImagesByItemId(updateItem!!.id) as ArrayList<ImageItem>
            selectedBox = App.database?.boxes()?.getBoxById(updateItem!!.boxId)
            scannedCode = updateItem!!.ean_upc_code
            new_item_description.setText(updateItem!!.description)
            new_item_category.setText(updateItem!!.category)
            updateTextView()
        }

        fab.setOnClickListener {it ->
            when {
                new_item_name.text.isEmpty() -> {
                    Snackbar.make(it, "Fill name box", Snackbar.LENGTH_SHORT).show()
                }

                updateItem == null
                        && App.database?.items()?.getItemByName(new_item_name.text.toString()) != null -> {
                    Snackbar.make(it, "Item name already exists", Snackbar.LENGTH_SHORT).show()
                }

                updateItem != null
                        && App.database?.items()?.getTheRestOfNames(updateItem!!.name)!!.contains(new_item_name.text.toString()) -> {
                    Snackbar.make(it, "Item name already exists", Snackbar.LENGTH_SHORT).show()
                }

                selectedBox == null -> {
                    Snackbar.make(it, "Choose box", Snackbar.LENGTH_SHORT).show()
                }

                updateItem == null
                        && scannedCode != null
                        && App.database?.items()?.getAllEanUpcCodes()!!.contains(scannedCode!!) -> {
                    Snackbar.make(it, "This EAN/UPC code exists", Snackbar.LENGTH_SHORT).show()
                }

                updateItem != null
                        && scannedCode != null
                        && App.database?.items()?.getTheRestOfEanUpcCodes(new_item_code.text.toString())!!.contains(scannedCode!!) -> {
                    Snackbar.make(it, "This EAN/UPC code exists", Snackbar.LENGTH_SHORT).show()
                }
                else -> {
                    if (new_item_code.text.isNotEmpty())
                        scannedCode = new_item_code.text.toString()
                    if (updateItem != null) {
                        updateItem!!.name = new_item_name.text.toString()
                        updateItem!!.description = new_item_description.text.toString()
                        updateItem!!.boxId = selectedBox!!.id
                        updateItem!!.ean_upc_code = scannedCode
                        updateItem!!.category = new_item_category.text.toString()
                        App.database?.items()?.update(updateItem!!)
                    } else
                        App.database?.items()?.insert(Item(
                            new_item_name.text.toString(),
                            new_item_category.text.toString(),
                            new_item_description.text.toString(),
                            selectedBox!!.id,
                            scannedCode
                            ))
                    images.forEach {
                        it.itemId = App.database?.items()?.getItemByName(new_item_name.text.toString())?.id!!
                }
                    App.database?.imagesItem()?.insertAll(images)
                    Toast.makeText(it.context, "Success", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        new_item_code.setOnClickListener {
            startActivityForResult(Intent(applicationContext, ScannerActivity::class.java), SCANNER_RC)
        }

        new_item_code.setOnLongClickListener {
            val builder = android.app.AlertDialog.Builder(it.context)
            builder.setMessage("Are you sure you want to delete scanned code?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    scannedCode = null
                    updateTextView()
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
            true
        }

        new_item_images.setOnClickListener {
            startActivityForResult(Intent(applicationContext, ImagesActivity::class.java).putExtra("ItemImages", images), IMAGES_RC)
        }

        new_item_images.setOnLongClickListener {
            val builder = android.app.AlertDialog.Builder(it.context)
            builder.setMessage("Are you sure you want to delete all images?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    images = ArrayList()
                    updateTextView()
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
            true
        }

        new_item_box.setOnClickListener {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.select_dialog, null)
            val builder = AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("\t\t\t\t\t\t\t\t\tSelect Box")

            val recyclerView: RecyclerView = dialogView.findViewById(R.id.select_dialog_recycler)
            recyclerView.adapter = SelectBoxRecyclerViewAdapter(selectedBox)
            recyclerView.layoutManager = LinearLayoutManager(
                applicationContext,
                LinearLayoutManager.VERTICAL,
                false
            )
//            recyclerView.addItemDecoration(DividerItemDecoration(it.context, (recyclerView.layoutManager as LinearLayoutManager).orientation))

            val alertDialog = builder.show()

            dialogView.select_dialog_save.setOnClickListener {
                selectedBox = (recyclerView.adapter as SelectBoxRecyclerViewAdapter).getSelectedBox()
                updateTextView()
                alertDialog.dismiss()
            }

            dialogView.select_dialog_cancel.setOnClickListener {
                alertDialog.dismiss()
            }
        }

        new_item_box.setOnLongClickListener {
            val builder = android.app.AlertDialog.Builder(it.context)
            builder.setMessage("Are you sure you want to delete selected box?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    selectedBox = null
                    updateTextView()
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGES_RC -> {
                    images = data?.extras?.get("IMAGES") as ArrayList<ImageItem>
                }

                SCANNER_RC -> {
                    scannedCode = data?.extras?.get("CODE") as String
                }
            }
            updateTextView()
        }
    }

    private fun updateTextView() {
        val message = "${images.size} images added"
        if (images.size > 0)
            new_item_images.setText(message)
        else
            new_item_images.setText("Select Images")

        if (scannedCode != null)
            new_item_code.setText(scannedCode)
        else
            new_item_code.setText("Scan Code")

        if (selectedBox != null)
            new_item_box.setText(selectedBox!!.name)
        else
            new_item_box.setText("Select Box")
    }
}
