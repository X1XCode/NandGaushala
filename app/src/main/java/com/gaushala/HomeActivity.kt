package com.gaushala

import addFragment
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gaushala.fragments.dashboard.DashboardFragment
import com.gaushala.utils.AppPreference
import hideKeyboard

class HomeActivity : AppCompatActivity() {
    lateinit var appPref: AppPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        appPref = AppPreference(this)

        //addFragment(UserListFragment(), true, false, animationType = AnimationType.fadeInfadeOut)
       // nav_view.setNavigationItemSelectedListener(this)
        /*val previousPosition = currentlySelected()
        if (nav_view!=null) {
            if (nav_view.menu!=null) {
                nav_view.menu.clear()
                nav_view.inflateMenu(R.menu.menu_home_drawer)
                if (previousPosition >= 0) {
                    nav_view.menu.getItem(previousPosition).isChecked = true
                }
                val username = appPref.getLoggedUserName()
               // nav_view.getHeaderView(0).tvUserName.text = "Hello, $username"
                prepareNavigationData()
            }
        }*/
        gotoDashBoard()
    }

    /*private fun prepareNavigationData() {
        gotoDashBoard()
    }*/

    /*override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val previousSelected = currentlySelected()
        when(item.itemId){
            R.id.nav_dash -> {
                gotoDashBoard()
            }
            R.id.nav_profile -> {
                addFragment(ProfileFragment(), true, true, animationType = AnimationType.fadeInfadeOut)
            }
            R.id.nav_logout -> {
                appPref.clearPreferences()
                finish()
                val intent = Intent(this@HomeActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
        //drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }*/

    /*fun currentlySelected(): Int{
        var k = -1
        for (i: Int in 0 until nav_view.menu.size()){
            if (nav_view.menu.getItem(i).isChecked) {
                k=i
            }
        }
        return k
    }*/


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try{
            getVisibleFragment()?.onActivityResult(requestCode, resultCode, data)
        }catch(e: Exception){
            e.printStackTrace()
        }
    }

    private fun getVisibleFragment() : Fragment?{
        try{
            val manager = supportFragmentManager
            val fragments = manager.fragments
            if (fragments != null){
                for (fragment in fragments){
                    if (fragment != null && fragment.isVisible){
                        return fragment
                    }
                }
            }
        }catch (e: Exception){
            e.printStackTrace()
        }
        return null
    }

    override fun onBackPressed() {
        hideKeyboard()
        val currentFragment : Fragment? =
                supportFragmentManager.findFragmentById(R.id.frame_container)!!
        if (currentFragment is DashboardFragment){
            finish()
        }else{
            super.onBackPressed()
        }
    }

    private fun gotoDashBoard() {
        addFragment(DashboardFragment(), true, false, animationType = AnimationType.fadeInfadeOut)
    }

    /*fun openDrawer() {
       // drawer_layout.openDrawer(GravityCompat.START)
    }

    fun closeDrawer(){
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        }
    }*/

    /*fun checkNavigationItem(position: Int) {
        nav_view.menu.getItem(position).isChecked = true
    }*/

}