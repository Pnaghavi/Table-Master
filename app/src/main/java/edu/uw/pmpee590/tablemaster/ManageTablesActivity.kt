package edu.uw.pmpee590.tablemaster

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_manage_tables.*
class ManageTablesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_tables)
        var allTables= TablesDataA()
        allTables.readBusy(tv_busyTables)

        btn_back_mgr_act.setOnClickListener {
            finish()
        }
    }
}
