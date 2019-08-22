package edu.uw.pmpee590.tablemaster

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_manage_users.*
import java.math.RoundingMode
import java.text.DecimalFormat

var myUsers:UsersDataA=UsersDataA()

class ManageUsersActivity : AppCompatActivity() {
    private var adapter = MyCustomAdapter(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_users)

        refreshArrayList()
        adapter.notifyDataSetChanged()

        val listView = findViewById<ListView>(R.id.staff_list)
        //listView.adapter = MyCustomAdapter(this)
        listView.setAdapter(this.adapter)

        btn_add_new_user.setOnClickListener {
            val intent: Intent = Intent(applicationContext, RegisterNewUserActivity::class.java)
            startActivity(intent)
        }

        btn_refresh_list.setOnClickListener {
            refreshArrayList()
        }

        btn_back_mgr.setOnClickListener {
            finish()
        }
    }

    fun refreshArrayList() {
        adapter = MyCustomAdapter(this)
        val listView = findViewById<ListView>(R.id.staff_list)
        listView.setAdapter(this.adapter)
        myUsers.readAll(adapter)

    }
}
class MyCustomAdapter(context: Context) : BaseAdapter() {

    private val mContext: Context
    val df = DecimalFormat("#.##")

    init {
        mContext = context
    }

    override fun getCount(): Int {
        return myUsers.allUsers.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return "test"
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {
        var convertView = convertView
        var viewHolder:ViewHolder

        val layoutInflater = LayoutInflater.from(mContext)
        val rowMain = layoutInflater.inflate(R.layout.activity_list_users_row_item, viewGroup, false)

        viewHolder = ViewHolder()
        viewHolder.btn_edit = rowMain.findViewById(R.id.btn_edit_user) as Button
        //val listUser = myUsers.allUsers[position]

        val tvFullName = rowMain.findViewById<TextView>(R.id.tv_full_name)
        val tvEmailAddr = rowMain.findViewById<TextView>(R.id.tv_email_addr)
        val tvRunningAVE = rowMain.findViewById<TextView>(R.id.tv_ave_time)
        val tvManagerStatus = rowMain.findViewById<TextView>(R.id.tv_is_manager)

        tvFullName.text = myUsers.allUsers[position].userName + " " + myUsers.allUsers[position].userLastName
        //tvFullName.text = listUser.userFirstName + " " + listUser.userLastName
        tvEmailAddr.text = myUsers.allUsers[position].userEmail
        var tempTvRunAVE = myUsers.allUsers[position].userRunningAVE
        tvRunningAVE.text = df.format(tempTvRunAVE).toString()
        //tvRunningAVE.text = myUsers.allUsers[position].userRunningAVE.toString()

        if(myUsers.allUsers[position].userSupervisor == true){
            tvManagerStatus.text = "Supervisor"
            tvManagerStatus.setBackgroundColor(Color.YELLOW)
            tvManagerStatus.setTextColor(Color.BLACK)
        }
        else{
            tvManagerStatus.text = "Staff"
            tvManagerStatus.setBackgroundColor(Color.GREEN)
            tvManagerStatus.setTextColor(Color.BLACK)
        }

        viewHolder.btn_edit!!.setOnClickListener {
            val intent = Intent(mContext, EditUserActivity::class.java)
            intent.putExtra("staffID",myUsers.allUsers[position].userID)
            intent.putExtra("staffEmail", myUsers.allUsers[position].userEmail)
            intent.putExtra("firstName",myUsers.allUsers[position].userName)
            intent.putExtra("lastName",myUsers.allUsers[position].userLastName)
            intent.putExtra("runningAVE",myUsers.allUsers[position].userRunningAVE)
            intent.putExtra("tableNum",myUsers.allUsers[position].userTotalTablesServedNUM)
            intent.putExtra("isSupervisor",myUsers.allUsers[position].userSupervisor)
            mContext.startActivity(intent)
        }

        return rowMain
    }

    private inner class ViewHolder {
        var btn_edit: Button? = null
    }
}


