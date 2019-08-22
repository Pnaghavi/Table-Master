package edu.uw.pmpee590.tablemaster

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var curUser: String? = null
    val managerLogin: String = "ecetablemaster@gmail.com"
    lateinit var providers: List<AuthUI.IdpConfig>
    val MY_REQUEST_CODE: Int = 7000  //any number
    var myUsers:UsersDataA=UsersDataA()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setTheme(R.style.Theme)
        setContentView(R.layout.activity_main)

        providers = Arrays.asList<AuthUI.IdpConfig>(
            AuthUI.IdpConfig.EmailBuilder().build() //,   // email address login (users in firebase)
            //AuthUI.IdpConfig.GoogleBuilder().build()   // google login
        )

        showSignInOptions()
        //setLaunchButton()

        btn_launch_activity.setOnClickListener {
            if(myUsers.isSupervisor(curUser!!.trim())){    //go to manager activity
                val intent: Intent = Intent(applicationContext, ManagerActivity::class.java)
                intent.putExtra("curMgrUser", curUser)
                //intent.putExtra("userList", myUsers.allUsers)
                startActivity(intent)
            }
            else {                          //not a manager
                val intent: Intent = Intent(applicationContext, StaffActivity::class.java)
                intent.putExtra("curStaffUser", curUser)
                startActivity(intent)
            }
        }

        btn_sign_out.setOnClickListener {
            AuthUI.getInstance().signOut(this)
                .addOnCompleteListener {
                    //btn_sign_out.isEnabled=false
                    showSignInOptions()
                }
            //.addOnFailureListener {}
        }
        myUsers.allUsers()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MY_REQUEST_CODE){
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK){
                val firebaseUser = FirebaseAuth.getInstance().currentUser //get current user
                curUser = firebaseUser!!.email
                tv_logged_in_as.text = curUser
                if(myUsers.isSupervisor(curUser!!.trim())){
                    val intent: Intent = Intent(applicationContext, ManagerActivity::class.java)
                    intent.putExtra("curMgrUser", curUser)
                    //intent.putExtra("userList", myUsers.allUsers)
                    startActivity(intent)
                }
                else {                          //not a manager
                    val intent: Intent = Intent(applicationContext, StaffActivity::class.java)
                    intent.putExtra("curStaffUser", curUser)
                    startActivity(intent)
                }
                setLaunchButtonText()
            }
            else{
                Toast.makeText(this,""+response!!.error!!.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun showSignInOptions(){
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.LoginTheme)
            .build(),MY_REQUEST_CODE)
    }

    fun setLaunchButtonText(){
        if(myUsers.isSupervisor(curUser!!.trim())){    //go to manager activity
            btn_launch_activity.text = "Go to Manager Activity"
        }
        else {                          //not a manager
            btn_launch_activity.text = "Go to Staff Activity"
        }
    }
}