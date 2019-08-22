package edu.uw.pmpee590.tablemaster

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList

class UsersDataA {

    //object properties

    var userID:String=""
    var userEmail:String=""
    var userName:String=""
    var userLastName:String=""
    var userTotalTablesServedNUM:Int=0
    var userRunningAVE:Double=0.0
    var userSupervisor:Boolean=false
    var constructonComplete:Boolean=false
    var allUsers=ArrayList<UsersDataA>()

    //additional constructors
    constructor(){
        constructonComplete=true
    }
    //reads user document from users where ID  is equal to the passed value and fills in object properties
    constructor(ID:String){
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(ID)
            .get()
            .addOnSuccessListener { result ->
                    userID=result.id
                    userEmail=result["email"].toString()
                    userName=result["name"].toString()
                    userLastName=result["lastName"].toString()
                    userTotalTablesServedNUM=result["totalTablesServedNUM"].toString().toInt()
                    userRunningAVE=result["runningAVE"].toString().toDouble()
                    userSupervisor=result["supervisor"].toString().toBoolean()
                    constructonComplete=true
                }
            .addOnFailureListener { exception ->
                Log.w("0", "Error reading documents.", exception)
            }
    }

    //reads user document from users where email address is equal to the passed value and fills in object properties
    constructor(email:String,supervisor:Boolean){
        val db = FirebaseFirestore.getInstance()
        db.collection("users").whereEqualTo("email",email).whereEqualTo("supervisor",supervisor)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    userID=document.id
                    userEmail=document["email"].toString()
                    userName=document["name"].toString()
                    userLastName=document["lastName"].toString()
                    userTotalTablesServedNUM=document["totalTablesServedNUM"].toString().toInt()
                    userRunningAVE=document["runningAVE"].toString().toDouble()
                    userSupervisor=document["supervisor"].toString().toBoolean()
                    constructonComplete=true
                }}
            .addOnFailureListener { exception ->
                Log.w("0", "Error reading documents.", exception)
            }
    }

    //reads user document from users where email address is equal to the passed value and fills in object properties
    fun constructByEmailOnly(email:String){
        val db = FirebaseFirestore.getInstance()
        db.collection("users").whereEqualTo("email",email)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    userID=document.id
                    userEmail=document["email"].toString()
                    userName=document["name"].toString()
                    userLastName=document["lastName"].toString()
                    userTotalTablesServedNUM=document["totalTablesServedNUM"].toString().toInt()
                    userRunningAVE=document["runningAVE"].toString().toDouble()
                    userSupervisor=document["supervisor"].toString().toBoolean()
                    constructonComplete=true
                }}
            .addOnFailureListener { exception ->
                Log.w("0", "Error reading documents.", exception)
            }
    }
    //reads all user documents from users and populates an arrayList
    fun allUsers(){
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var newUser=UsersDataA()
                    newUser.userID=document.id
                    newUser.userEmail=document["email"].toString()
                    newUser.userName=document["name"].toString()
                    newUser.userLastName=document["lastName"].toString()
                    newUser.userTotalTablesServedNUM=document["totalTablesServedNUM"].toString().toInt()
                    newUser.userRunningAVE=document["runningAVE"].toString().toDouble()
                    newUser.userSupervisor=document["supervisor"].toString().toBoolean()
                    newUser.constructonComplete=true
                    this.allUsers.add(newUser)
                }}
            .addOnFailureListener { exception ->
                Log.w("0", "Error reading documents.", exception)
            }
    }
    fun isSupervisor(email:String):Boolean {
        for (userData in this.allUsers) {
            if(userData.userEmail==email&&userData.userSupervisor){
                return true
            }
        }
        return false
    }
    //reads user document from users where name and last name is equal to the passed values and fills in object properties
    constructor(name:String, lastName:String){
        val db = FirebaseFirestore.getInstance()
        db.collection("users").whereEqualTo("name",name).whereEqualTo("lastName",lastName)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    userID=document.id
                    userEmail=document["email"].toString()
                    userName=document["name"].toString()
                    userLastName=document["lastName"].toString()
                    userTotalTablesServedNUM=document["totalTablesServedNUM"].toString().toInt()
                    userRunningAVE=document["runningAVE"].toString().toDouble()
                    userSupervisor=document["supervisor"].toString().toBoolean()
                    constructonComplete=true
                }}
            .addOnFailureListener { exception ->
                Log.w("0", "Error reading documents.", exception)
            }
    }

    //reads all user documents in collection and passes it to text view widget
    fun readAll(txtVals:android.widget.TextView){
        val db = FirebaseFirestore.getInstance()
        db.collection("users").orderBy("lastName")
            .get()
            .addOnSuccessListener { result ->
                txtVals.text=""
                for (document in result) {
                    txtVals.text=txtVals.text.toString()+document.id
                    txtVals.text=txtVals.text.toString()+"\t"
                    txtVals.text=txtVals.text.toString()+document.data["email"].toString()
                    txtVals.text=txtVals.text.toString()+"\t"
                    txtVals.text=txtVals.text.toString()+document.data["name"].toString()
                    txtVals.text=txtVals.text.toString()+"\t"
                    txtVals.text=txtVals.text.toString()+document.data["lastName"].toString()
                    txtVals.text=txtVals.text.toString()+"\t"
                    txtVals.text=txtVals.text.toString()+document.data["totalTablesServedNUM"].toString()
                    txtVals.text=txtVals.text.toString()+"\t"
                    txtVals.text=txtVals.text.toString()+document.data["runningAVE"].toString()
                    txtVals.text=txtVals.text.toString()+"\t"
                    txtVals.text=txtVals.text.toString()+document.data["supervisor"].toString()
                    txtVals.text=txtVals.text.toString()+"\n"
                }
            }
            .addOnFailureListener { exception ->
                Log.w("0", "Error getting documents.", exception)
            }
    }

    //reads all user documents in collection and passes it to list view widget
    fun readAll( myadaptor:MyCustomAdapter){
        this.allUsers.clear()
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    var newUser=UsersDataA()
                    newUser.userID=document.id
                    newUser.userEmail=document["email"].toString()
                    newUser.userName=document["name"].toString()
                    newUser.userLastName=document["lastName"].toString()
                    newUser.userTotalTablesServedNUM=document["totalTablesServedNUM"].toString().toInt()
                    newUser.userRunningAVE=document["runningAVE"].toString().toDouble()
                    newUser.userSupervisor=document["supervisor"].toString().toBoolean()
                    newUser.constructonComplete=true
                    this.allUsers.add(newUser)
                }
                myadaptor.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.w("0", "Error reading documents.", exception)
            }
    }

    //updates user document in the users collection using passed values
    fun update(ID:String, email:String,name:String,lastName:String,totalTablesServedNUM:Int,runningAVE:Double, supervisor:Boolean) {
        val db = FirebaseFirestore.getInstance()
        val user = HashMap<String, Any>()
        user["email"] = email
        user["name"] = name
        user["lastName"] = lastName
        user["totalTablesServedNUM"] = totalTablesServedNUM
        user["runningAVE"] = runningAVE
        user["supervisor"] = supervisor
        db.collection("users").document(ID)
            .set(user)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
                Log.w("0", "Error updating document", e)
            }

    }

    //adds new document to users collection using passed values
    fun addNew( email:String,name:String,lastName:String,totalTablesServedNUM:Int,runningAVE:Double, supervisor:Boolean) {
        val db = FirebaseFirestore.getInstance()
        var ID=0
        db.collection("users").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(ID<document.id.toInt())
                        ID=document.id.toInt()
                }
                val user = HashMap<String, Any>()
                user["email"] =email
                user["name"] =name
                user["lastName"] =lastName
                user["totalTablesServedNUM"] =totalTablesServedNUM
                user["runningAVE"] =runningAVE
                user["supervisor"] =supervisor
                db.collection("users").document((ID+1).toString())
                    .set(user)
                    .addOnSuccessListener {
                    }
                    .addOnFailureListener { e ->
                        Log.w("0", "Error adding document", e)
                    }
            }
            .addOnFailureListener { exception ->
                Log.w("0", "Error getting documents.", exception)
            }


    }

    //deletes user document from collection where ID is equal to passed
    fun deleteID(ID:String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(ID).delete()
            .addOnSuccessListener { }
            .addOnFailureListener { e -> Log.w("0", "Error deleting document", e) }
    }

    //deletes user document from collection where email is equal to passed
    fun deleteEmail(email:String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").whereEqualTo("email", email).get().addOnSuccessListener { result ->
            for (document in result) {
                deleteID(document.id)
            }
        }
            .addOnFailureListener { exception ->
                Log.w("0", "Error deleting documents.", exception)
            }
    }

    //deletes user document from collection where name and lastName is equal to passed
    fun deleteNameLastName(name:String,LastName:String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").whereEqualTo("name", name).whereEqualTo("lastName",LastName).get().addOnSuccessListener { result ->
            for (document in result) {
                deleteID(document.id)
            }
        }
            .addOnFailureListener { exception ->
                Log.w("0", "Error deleting documents.", exception)
            }
    }

    // updates running average of the user
    fun updateAVE(ID:String,newNumber:Double) {
        val db = FirebaseFirestore.getInstance()
        var newAve=0.0
        db.collection("users").document(ID).get()
            .addOnSuccessListener{ document->
                newAve=(document["runningAVE"].toString().toDouble()*document["totalTablesServedNUM"].toString().toInt()+newNumber)/(document["totalTablesServedNUM"].toString().toInt()+1)
                db.collection("users").document(ID).update("totalTablesServedNUM",document["totalTablesServedNUM"].toString().toInt()+1,"runningAVE",newAve).addOnSuccessListener { }
                    .addOnFailureListener { exception ->

                        Log.w("0", "Error updating documents.", exception)
                    }

        }
            .addOnFailureListener { exception ->
                Log.w("0", "Error updating documents.", exception)
            }

    }

    // deletes object from users collection returns true if userID is not empty
    fun deleteObjectFromDB():Boolean{
        if(userID!="") {
            deleteID(userID)
            return true
        }
        else {
            return false
        }
    }

    // updates object into users collection returns true if userID is not empty
    fun updateObjectInDB():Boolean{
        if(userID!="") {

            update(userID, userEmail, userName, userLastName, userTotalTablesServedNUM, userRunningAVE, userSupervisor)
            return true
        }
        else{
            return false
        }
    }

    // inserts object into users collection returns true if email is not empty
    fun insertObjectInDB():Boolean{
        if(userEmail!="") {
            addNew(userEmail, userName, userLastName, userTotalTablesServedNUM, userRunningAVE, userSupervisor)
            return true
        }
        else{
            return false
        }
    }
}