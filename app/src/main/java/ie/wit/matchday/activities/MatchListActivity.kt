package ie.wit.matchday.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.LinearLayoutManager
import ie.wit.matchday.R
import ie.wit.matchday.adapters.MatchAdapter
import ie.wit.matchday.adapters.MatchListener
import ie.wit.matchday.databinding.ActivityMatchListBinding
import ie.wit.matchday.main.MainApp
import ie.wit.matchday.models.MatchModel

class MatchListActivity : AppCompatActivity(), MatchListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityMatchListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMatchListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title
        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadMatches()
        registerRefreshCallback()


    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_match_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, MatchActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
            R.id.item_cancel -> {
                val launcherIntent = Intent(this, HomepageActivity::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onMatchClick(match: MatchModel) {
        val launcherIntent = Intent(this, MatchActivity::class.java)
        launcherIntent.putExtra("match_edit", match)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {
                loadMatches()
            }
    }

    private fun loadMatches() {
        showMatches(app.matches.findAll())
    }

    fun showMatches (matches: List<MatchModel>) {
        binding.recyclerView.adapter = MatchAdapter(matches, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
}
