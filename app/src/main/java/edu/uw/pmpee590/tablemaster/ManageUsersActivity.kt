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
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_manage_users.*


var staffList = ArrayList<userDataString>()
//var adapter: Adapter = ManageUsersActivity.MyCustomAdapter
var myUsers:UsersDataA=UsersDataA()

class ManageUsersActivity : AppCompatActivity() {
    private var adapter = MyCustomAdapter(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_users)
        //myUsers.allUsers()

        //adapter = MyCustomAdapter(this)

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
            //refreshArrayList()
            //Thread.sleep(1000)
            refreshArrayList()
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
        val layoutInflater = LayoutInflater.from(mContext)
        val rowMain = layoutInflater.inflate(R.layout.activity_list_users_row_item, viewGroup, false)

        val listUser = myUsers.allUsers[position]

        val tvFullName = rowMain.findViewById<TextView>(R.id.tv_full_name)
        val tvEmailAddr = rowMain.findViewById<TextView>(R.id.tv_email_addr)
        val tvRunningAVE = rowMain.findViewById<TextView>(R.id.tv_ave_time)
        val tvManagerStatus = rowMain.findViewById<TextView>(R.id.tv_is_manager)

        tvFullName.text = myUsers.allUsers[position].userName + " " + myUsers.allUsers[position].userLastName
        //tvFullName.text = listUser.userFirstName + " " + listUser.userLastName
        tvEmailAddr.text = myUsers.allUsers[position].userEmail
        tvRunningAVE.text = myUsers.allUsers[position].userRunningAVE.toString()
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

        return rowMain
    }
}


