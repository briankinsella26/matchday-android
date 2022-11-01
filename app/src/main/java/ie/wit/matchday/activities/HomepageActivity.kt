package ie.wit.matchday.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import ie.wit.matchday.R
import ie.wit.matchday.databinding.ActivityHomepageBinding
import ie.wit.matchday.main.MainApp
import timber.log.Timber.i

@GlideModule
class HomepageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomepageBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp
        i("Homepage activity started")

        registerRefreshCallback()

//        Glide
//            .with(this)
//            .load("http://via.placeholder.com/300.png")
//            .into(binding.homepageHero)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, MatchActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_matches -> {
                val launcherIntent = Intent(this, MatchListActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {}
    }
}