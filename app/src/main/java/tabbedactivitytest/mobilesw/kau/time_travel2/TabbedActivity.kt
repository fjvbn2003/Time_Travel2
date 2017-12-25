package tabbedactivitytest.mobilesw.kau.time_travel2

import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import kotlinx.android.synthetic.main.activity_tabbed.*


class TabbedActivity : AppCompatActivity() {

    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    lateinit var mAuth: FirebaseAuth
    var mUser: FirebaseUser? = null
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabbed)

        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth.currentUser
        val floatButton = findViewById<FloatingActionButton>(R.id.floatingButton)
        floatButton.setOnClickListener {
            val intent = Intent(this, AddPostActivity::class.java)
            startActivity(intent)
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_tabbed, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_signOut) {
            mAuth.signOut()
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {



        override fun getItem(position: Int): Fragment {
            if(position == 0){
                return HistoryFragment()
            }
            else if(position == 1){
                return FriendsFragment()
            }
            else{
                return SettingFragment()
            }
        }

        override fun getCount(): Int {
            // Show 3 total pages.
            return 3
        }
    }


}
