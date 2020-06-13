package pl.mik.itemstorage.ui.box

import SelectBoxRecyclerViewAdapter
import SelectLocalizationRecyclerViewAdapter
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
import kotlinx.android.synthetic.main.activity_new_box.*
import kotlinx.android.synthetic.main.content_new_box.*
import kotlinx.android.synthetic.main.content_new_item.*
import kotlinx.android.synthetic.main.select_dialog.view.*
import pl.mik.itemstorage.database.App
import pl.mik.itemstorage.R
import pl.mik.itemstorage.ui.scanner.ScannerActivity
import pl.mik.itemstorage.database.entities.Box
import pl.mik.itemstorage.database.entities.ImageBox
import pl.mik.itemstorage.database.entities.Localization
import pl.mik.itemstorage.ui.images.ImagesActivity
import pl.mik.itemstorage.ui.searchScanner.SearchScannerActivity
import kotlin.collections.ArrayList


class NewBoxActivity : AppCompatActivity() {

    private val IMAGES_RC: Int = 0
    private val SCANNER_RC: Int = 1
    private var images: ArrayList<ImageBox> = ArrayList()
    private var selectedLocalization: Localization? = null
    private var scannedCode: String? = null
    private var updateBox: Box? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_box)
        updateTextView()

        updateBox = intent.getSerializableExtra("Box") as? Box
        if (updateBox != null) {
            new_box_name.setText(updateBox!!.name)
            images = App.database?.imagesBox()?.getImagesByBoxId(updateBox!!.id) as ArrayList<ImageBox>
            selectedLocalization = App.database?.localizations()?.getLocalizationById(updateBox!!.localizationId)
            scannedCode = updateBox!!.qrCode
            new_box_description.setText(updateBox!!.description)
            updateTextView()
        }

        fab.setOnClickListener { it ->
            when {
                new_box_name.text.isEmpty() -> {
                    Snackbar.make(it, "Fill name box", Snackbar.LENGTH_SHORT).show()
                }

                updateBox == null && App.database?.boxes()?.getBoxByName(new_box_name.text.toString()) != null -> {
                    Snackbar.make(it, "Box name already exists", Snackbar.LENGTH_SHORT).show()
                }

                updateBox != null && App.database?.boxes()?.getTheRestOfNames(updateBox!!.name)!!.contains(new_box_name.text.toString()) -> {
                    Snackbar.make(it, "Box name already exists", Snackbar.LENGTH_SHORT).show()
                }

                selectedLocalization == null -> {
                    Snackbar.make(it, "Choose localization", Snackbar.LENGTH_SHORT).show()
                }

                updateBox == null && scannedCode != null && App.database?.boxes()?.getAllQRCodes()!!.contains(scannedCode!!) -> {
                    Snackbar.make(it, "This QR code exists", Snackbar.LENGTH_SHORT).show()
                }

                updateBox != null && scannedCode != null && App.database?.boxes()?.getTheRestOfQRCodes(new_box_code.text.toString())!!.contains(scannedCode!!) -> {
                    Snackbar.make(it, "This QR code exists", Snackbar.LENGTH_SHORT).show()
                }

                else -> {
                    scannedCode = if (new_box_code.text.isNotEmpty() && new_box_code.text.toString() != "Scan Code")
                        new_box_code.text.toString()
                    else
                        null

                    if (updateBox != null) {
                        updateBox!!.name = new_box_name.text.toString()
                        updateBox!!.description = new_box_description.text.toString()
                        updateBox!!.localizationId = selectedLocalization!!.id
                        updateBox!!.qrCode = scannedCode
                        App.database?.boxes()?.update(updateBox!!)
                    } else
                        App.database?.boxes()?.insert(Box(
                            new_box_name.text.toString(),
                            scannedCode,
                            new_box_description.text.toString(),
                            App.session?.userId!!,
                            selectedLocalization!!.id
                        ))
                    images.forEach {
                        it.boxId = App.database?.boxes()?.getBoxByName(new_box_name.text.toString())?.id!!
                    }
                    App.database?.imagesBox()?.insertAll(images)
                    finish()
                }
            }
        }

        new_box_code.setOnClickListener {
            startActivityForResult(Intent(applicationContext, ScannerActivity::class.java), SCANNER_RC)
        }

        new_box_code.setOnLongClickListener {
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

        new_box_images.setOnClickListener {
            startActivityForResult(Intent(applicationContext, ImagesActivity::class.java).putExtra("BoxImages", images), IMAGES_RC)
        }

        new_box_images.setOnLongClickListener {
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
        
        new_box_localization.setOnClickListener {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.select_dialog, null)
            val builder = AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("\t\t\t\t\t\t\t\t\tSelect Localization")

            val recyclerView: RecyclerView = dialogView.findViewById(R.id.select_dialog_recycler)
            recyclerView.layoutManager = LinearLayoutManager(
                applicationContext,
                LinearLayoutManager.VERTICAL,
                false
            )
            recyclerView.addItemDecoration(DividerItemDecoration(it.context, DividerItemDecoration.VERTICAL))
            recyclerView.adapter = SelectLocalizationRecyclerViewAdapter(selectedLocalization)

            val alertDialog = builder.show()

            dialogView.select_dialog_save.setOnClickListener{
                selectedLocalization = (recyclerView.adapter as SelectLocalizationRecyclerViewAdapter).getSelectedLocalization()
                updateTextView()
                alertDialog.dismiss()
            }

            dialogView.select_dialog_cancel.setOnClickListener{
                alertDialog.dismiss()
            }
        }

        new_box_localization.setOnLongClickListener {
            val builder = android.app.AlertDialog.Builder(it.context)
            builder.setMessage("Are you sure you want to delete selected localization?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    selectedLocalization = null
                    updateTextView()
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
            true
        }

        search_scanner.setOnClickListener {
            if (scannedCode != null) {
                startActivity(Intent(it.context, SearchScannerActivity::class.java).putExtra("ScannedCode", scannedCode))
            } else {
                Snackbar.make(it, "Provide Code to search it", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGES_RC -> {
                    images = data?.extras?.get("IMAGES") as ArrayList<ImageBox>
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
            new_box_images.setText(message)
        else
            new_box_images.setText("Select Images")

        if (scannedCode != null)
            new_box_code.setText(scannedCode)
        else
            new_box_code.setText("Scan Code")

        if (selectedLocalization != null)
            new_box_localization.setText(selectedLocalization!!.name)
        else
            new_box_localization.setText("Select Localization")
    }
}
