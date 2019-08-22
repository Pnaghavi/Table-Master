package edu.uw.pmpee590.tablemaster

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_edit_user.*

class EditUserActivity : AppCompatActivity() {

    var editUser: UsersDataA = UsersDataA()
    var superChanged: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user)

        val editID = intent.getStringExtra("staffID")
        val editEmail = intent.getStringExtra("staffEmail")
        var editFirstName = intent.getStringExtra("firstName")
        var editLastName = intent.getStringExtra("lastName")
        val editRunAVE = intent.getDoubleExtra("runningAVE",0.0)
        val editTableNum = intent.getIntExtra("tableNum",0)
        var editIsSuper = intent.getBooleanExtra("isSupervisor", false)


        tv_edit_emailAddr.text = editEmail
        et_edit_firstName.hint = editFirstName
        et_edit_lastName.hint = editLastName
        checkBox_edit_super.isChecked = editIsSuper

        checkBox_edit_super.setOnClickListener {
            editIsSuper = checkBox_edit_super.isChecked
            superChanged = true
        }

        btn_done_edit.setOnClickListener {
            if(et_edit_firstName.text.isNotEmpty() || et_edit_lastName.text.isNotBlank() || superChanged) {
                et_edit_firstName.text.isNotBlank().apply {
                    editFirstName = et_edit_firstName.text.toString()
                }
                et_edit_lastName.text.isNotEmpty().apply {
                    editLastName = et_edit_lastName.text.toString()
                    println("-------${editLastName}")
                }
                editUser.update(editID,editEmail,editFirstName,editLastName,editTableNum,editRunAVE,editIsSuper)
                superChanged = false
                Toast.makeText(this,"Successfully updated: $editEmail",Toast.LENGTH_LONG).show()
            }
            else {
                //if (et_edit_firstName.text.isNotBlank() && et_edit_lastName.text.isNotBlank() && !superChanged){
                    Toast.makeText(this, "Nothing changed", Toast.LENGTH_SHORT).show()
                //}
            }
        }

        btn_cancel_edit.setOnClickListener {
            finish()
        }
    }
}
