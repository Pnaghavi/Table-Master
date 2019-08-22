package edu.uw.pmpee590.tablemaster

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import edu.uw.pmpee590.tablemaster.UsersDataA
import kotlinx.android.synthetic.main.activity_register_new_user.*
import com.google.firebase.auth.FirebaseAuth

class RegisterNewUserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_new_user)

        btn_register.setOnClickListener {
            var newUser= UsersDataA()
            newUser.userName= edittext_firstName.text.toString()
            newUser.userLastName= edittext_lastName.text.toString()
            newUser.userEmail=edittext_email.text.toString()
            newUser.userSupervisor=checkBox_Supervisor.isChecked
            newUser.insertObjectInDB()
            val email = edittext_email.text.toString()
            val password = edittext_password.text.toString()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener{
                    if(!it.isSuccessful) return@addOnCompleteListener
                }
                // if successful

            Toast.makeText(this,"Successfully added: $email",Toast.LENGTH_LONG).show()
                edittext_email.text.clear()
                edittext_password.text.clear()
                edittext_firstName.text.clear()
                edittext_lastName.text.clear()
                checkBox_Supervisor.isChecked=false
        }

        btn_finish_add_staff.setOnClickListener {
            finish()
        }
    }
}
