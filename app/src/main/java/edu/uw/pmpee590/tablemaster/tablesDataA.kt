package edu.uw.pmpee590.tablemaster

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class TablesDataA {

    //object properties
    var tableID:String=""
    var tableUserID:String=""
    var tableTableNUM:String=""
    var tableBusy:Boolean=false
    var tableFinished:Boolean=false
    var constructonComplete:Boolean=false

    //additional constructors
    constructor(){
        constructonComplete=true
    }

    //reads table document from table where ID  is equal to the passed value and fills in object properties
    constructor(ID:String){
        val db = FirebaseFirestore.getInstance()
        db.collection("table").document(ID)
            .get()
            .addOnSuccessListener { result ->
                tableID=result.id
                tableUserID=result["userID"].toString()
                tableTableNUM=result["tableNUM"].toString()
                tableBusy=result["busy"].toString().toBoolean()
                tableFinished=result["finished"].toString().toBoolean()
                constructonComplete=true
            }
            .addOnFailureListener { exception ->
                Log.w("0", "Error reading documents.", exception)
            }
    }

    //reads table document from table where email address is equal to the passed value and fills in object properties
    constructor(userID:String,busy:Boolean){
        val db = FirebaseFirestore.getInstance()
        db.collection("table").whereEqualTo("userID",userID).whereEqualTo("busy",busy)
            .get()
            .addOnSuccessListener {  result ->
                for (document in result) {
                tableID=document.id
                tableUserID=document["userID"].toString()
                tableTableNUM=document["tableNUM"].toString()
                tableBusy=document["busy"].toString().toBoolean()
                tableFinished=document["finished"].toString().toBoolean()
                constructonComplete=true
            }}
            .addOnFailureListener { exception ->
                Log.w("0", "Error reading documents.", exception)
            }
    }

    //reads table document from table where email address is equal to the passed value and fills in object properties
    constructor(tableNUM:String,busy:Boolean,finished:Boolean){
        val db = FirebaseFirestore.getInstance()
        db.collection("table").whereEqualTo("tableNUM",tableNUM).whereEqualTo("busy",busy).whereEqualTo("finished",finished)
            .get()
            .addOnSuccessListener {  result ->
                for (document in result) {
                    tableID=document.id
                    tableUserID=document["userID"].toString()
                    tableTableNUM=document["tableNUM"].toString()
                    tableBusy=document["busy"].toString().toBoolean()
                    tableFinished=document["finished"].toString().toBoolean()
                    constructonComplete=true
                }}
            .addOnFailureListener { exception ->
                Log.w("0", "Error reading documents.", exception)
            }
    }

    //reads all table documents in collection and passes it to text view widget
    fun readAll(txtVals:android.widget.TextView){
        val db = FirebaseFirestore.getInstance()
        db.collection("table").orderBy("tableNUM")
            .get()
            .addOnSuccessListener { result ->
                txtVals.text=""
                for (document in result) {
                    txtVals.text=txtVals.text.toString()+document.id
                    txtVals.text=txtVals.text.toString()+"\t"
                    txtVals.text=txtVals.text.toString()+document.data["userID"].toString()
                    txtVals.text=txtVals.text.toString()+"\t"
                    txtVals.text=txtVals.text.toString()+document.data["tableNUM"].toString()
                    txtVals.text=txtVals.text.toString()+"\t"
                    txtVals.text=txtVals.text.toString()+document.data["busy"].toString()
                    txtVals.text=txtVals.text.toString()+"\t"
                    txtVals.text=txtVals.text.toString()+document.data["finished"].toString()
                    txtVals.text=txtVals.text.toString()+"\n"
                }
            }
            .addOnFailureListener { exception ->
                Log.w("0", "Error getting documents.", exception)
            }
    }
    fun readBusy(txtVals:android.widget.TextView){
        val db = FirebaseFirestore.getInstance()
        var status=true
        db.collection("table").whereEqualTo("busy",status)
            .get()
            .addOnSuccessListener { result ->
                txtVals.text=""
                for (document in result) {
                    txtVals.text=txtVals.text.toString()+document.data["tableNUM"].toString()
                    txtVals.text=txtVals.text.toString()+"\n"
                }
            }
            .addOnFailureListener { exception ->
                Log.w("0", "Error getting documents.", exception)
            }
    }

    //updates table document in the table collection using passed values
    fun update(ID:String, userID:String,tableNUM:String,busy:Boolean,finished:Boolean) {
        val db = FirebaseFirestore.getInstance()
        val table = HashMap<String, Any>()
        table["userID"] = userID
        table["tableNUM"] = tableNUM
        table["busy"] = busy
        table["finished"] = finished
        db.collection("table").document(ID)
            .set(table)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
                Log.w("0", "Error updating document", e)
            }
    }

    //adds new document to table collection using passed values
    fun addNew( userID:String,tableNUM:String,busy:Boolean,finished:Boolean) {
        val db = FirebaseFirestore.getInstance()
        var ID=0
        db.collection("table").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if(ID<document.id.toInt())
                        ID=document.id.toInt()
                }
                val table = HashMap<String, Any>()
                table["userID"] = userID
                table["tableNUM"] = tableNUM
                table["busy"] = busy
                table["finished"] = finished
                db.collection("table").document((ID+1).toString())
                    .set(table)
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

    //deletes table document from collection where ID is equal to passed
    fun deleteID(ID:String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("table").document(ID).delete()
            .addOnSuccessListener { }
            .addOnFailureListener { e -> Log.w("0", "Error deleting document", e) }
    }

    // deletes object from table collection returns true if tableID is not empty
    fun deleteObjectFromDB():Boolean{
        if(tableID!="") {
            deleteID(tableID)
            return true
        }
        else {
            return false
        }
    }

    // updates object into users collection returns true if tableID is not empty
    fun updateObjectInDB():Boolean{
        if(tableID!="") {
            update(tableID, tableUserID, tableTableNUM, tableBusy, tableFinished)
            return true
        }
        else{
            return false
        }
    }

    // inserts object into table collection returns true if UserID field is not empty
    fun insertObjectInDB():Boolean{
        if(tableUserID!="") {
            addNew( tableUserID, tableTableNUM, tableBusy, tableFinished)
            return true
        }
        else{
            return false
        }
    }
}