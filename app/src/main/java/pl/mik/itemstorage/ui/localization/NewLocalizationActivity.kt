package pl.mik.itemstorage.ui.localization

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import pl.mik.itemstorage.R

import kotlinx.android.synthetic.main.activity_new_localization.*
import kotlinx.android.synthetic.main.content_new_localization.*
import pl.mik.itemstorage.database.App
import pl.mik.itemstorage.database.entities.Localization

class NewLocalizationActivity : AppCompatActivity() {

    private var updateLoc: Localization? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_localization)

        updateLoc = intent.getSerializableExtra("Localization") as? Localization
        if (updateLoc != null) {
            new_localization_name.setText(updateLoc!!.name)
        }

        fab.setOnClickListener {
            when {
                new_localization_name.text.isEmpty() -> {
                    Snackbar.make(it, "Fill name box", Snackbar.LENGTH_SHORT).show()
                }

                updateLoc != null
                        && App.database?.localizations()?.getTheRestOfNames(updateLoc!!.name)!!.contains(new_localization_name.text.toString()) -> {
                    Snackbar.make(it, "Localization name already exists", Snackbar.LENGTH_SHORT).show()
                }

                updateLoc == null
                        && App.database?.localizations()?.getLocalizationByName(new_localization_name.text.toString()) != null -> {
                    Snackbar.make(it, "Localization name already exists", Snackbar.LENGTH_SHORT).show()
                }

                else -> {
                    if (updateLoc == null)
                        App.database?.localizations()?.insert(Localization(new_localization_name.text.toString(), App.session?.userId!!))
                    else {
                        updateLoc!!.name = new_localization_name.text.toString()
                        App.database?.localizations()?.update(updateLoc!!)
                    }
                    finish()
                }
            }
        }
    }

}
