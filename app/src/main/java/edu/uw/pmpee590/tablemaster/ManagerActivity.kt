package edu.uw.pmpee590.tablemaster

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_manager.*
import java.util.ArrayList

class ManagerActivity : AppCompatActivity() {
    //var myUsers:UsersDataA=UsersDataA()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setTheme(R.style.ManagerTheme)
        setContentView(R.layout.activity_manager)

        val curUser = intent.getStringExtra("curMgrUser")
        tv_logged_in_as_mgr.text = curUser

        btn_manage_staff.setOnClickListener {
            val intent: Intent = Intent(applicationContext, ManageUsersActivity::class.java)
            startActivity(intent)
        }

        btn_manage_tables.setOnClickListener {
            val intent: Intent = Intent(applicationContext, ManageTablesActivity::class.java)
            startActivity(intent)
        }

        btn_back_to_login.setOnClickListener {
            finish()
        }
    }
}