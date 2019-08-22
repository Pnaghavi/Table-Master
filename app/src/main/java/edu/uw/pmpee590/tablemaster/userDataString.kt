package edu.uw.pmpee590.tablemaster

class userDataString {

    var userEmail:String=""
    var userFirstName: String = ""
    var userLastName: String = ""
    var userRunningAVE: String = ""
    var supervisorStatus: String = ""

    //additional constructors
    constructor(email:String,firstName:String,lastName:String,runningAVE:String,supervisor:String){
        userEmail = email
        userFirstName = firstName
        userLastName = lastName
        userRunningAVE = runningAVE
        supervisorStatus = supervisor
    }
}