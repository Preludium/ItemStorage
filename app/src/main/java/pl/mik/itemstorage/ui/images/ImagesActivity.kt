package pl.mik.itemstorage.ui.images

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_images.*
import kotlinx.android.synthetic.main.content_images.*
import pl.mik.itemstorage.R
import pl.mik.itemstorage.database.entities.ImageBox
import pl.mik.itemstorage.database.entities.ImageItem
import java.io.ByteArrayOutputStream
import java.util.*
import kotlin.collections.ArrayList


class ImagesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private val CAMERA_RC = 0
    private val CAMERA_PERMISSION_RC = 1
    private val GALLERY_RC = 2
    private var extraName: String? = null
    private var cameraPermission: Boolean = false

    var images: ArrayList<Any> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)
        setSupportActionBar(toolbar)


        when {
            intent.hasExtra("BoxImages") -> {
                extraName = "BoxImages"
                images.addAll(intent.getSerializableExtra("BoxImages") as ArrayList<ImageBox>)
            }

            intent.hasExtra("ItemImages") -> {
                extraName = "ItemImages"
                images.addAll(intent.getSerializableExtra("ItemImages") as ArrayList<ImageItem>)
            }
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = ImagesRecyclerViewAdapter(images)

        recyclerView = images_recycler_view.apply {
            layoutManager = viewManager
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
            adapter = viewAdapter
        }

        fab.setOnClickListener {
            val resultIntent = Intent().putExtra("IMAGES", images)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        viewAdapter.notifyDataSetChanged()
    }

    override fun onStart() {
        super.onStart()
        if (viewAdapter.itemCount == 0)
            images_text_info.visibility = View.VISIBLE
        else
            images_text_info.visibility = View.INVISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            CAMERA_RC -> { //h - 252 w - 142
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val image = data.extras?.get("data") as Bitmap
                    val stream = ByteArrayOutputStream()
                    image.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    when (extraName) {
                        "BoxImages" -> {
                            images.add(ImageBox(image = stream.toByteArray(), createdAt = Date()))
                        }

                        "ItemImages" -> {
                            images.add(ImageItem(image = stream.toByteArray(), createdAt = Date()))
                        }
                    }
//                    Toast.makeText(this, "Image created", Toast.LENGTH_SHORT).show()
                }
            }

            GALLERY_RC -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val imageUri = data.data as Uri
                    var bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                    bitmap = Bitmap.createScaledBitmap(bitmap, 142, 252, true)
                    val stream = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    when (extraName) {
                        "BoxImages" -> {
                            images.add(ImageBox(image = stream.toByteArray(), createdAt = Date()))
                        }

                        "ItemImages" -> {
                            images.add(ImageItem(image = stream.toByteArray(), createdAt = Date()))
                        }
                    }
                }
            }

            else -> {
                Toast.makeText(this, "Bad request code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_photo -> {
                val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (callCameraIntent.resolveActivity(packageManager) != null) {
                    checkPermission()
                    if (cameraPermission)
                        startActivityForResult(callCameraIntent, CAMERA_RC)
                }
                return true
            }

            R.id.action_gallery -> {
                startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), GALLERY_RC)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_RC)
        else
            this.cameraPermission = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_RC && permissions[0] == Manifest.permission.CAMERA) {
            this.cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (callCameraIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(callCameraIntent, CAMERA_RC)
            }
        }
    }
}
