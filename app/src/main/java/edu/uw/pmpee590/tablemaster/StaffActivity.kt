package edu.uw.pmpee590.tablemaster

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.TextView
import edu.uw.pmpee590.tablemaster.TablesDataA
import edu.uw.pmpee590.tablemaster.UsersDataA
import com.firebase.ui.auth.AuthUI
import edu.uw.pmpee590.arduinoandroid.BLE
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_staff.*

class StaffActivity : AppCompatActivity(), BLE.Callback {

    private var ble: BLE? = null
    private var messages: TextView? = null
    var timer_sec = 0
    var connec = true
    var discon = false
    var dataAcquire = ""
    var cero = 0
    var name=""
    var currentUser=UsersDataA()
    var currentTable=TablesDataA()
    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelId = "edu.uw.pmpee590.tablemaster"
    private val description = "Test notification"

    var device_name:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_staff)

        val curUser = intent.getStringExtra("curStaffUser")
        currentUser=UsersDataA(curUser.trim(),false)
        tv_staff_logged_in_as.text = curUser

        btn_staff_back_to_login.setOnClickListener {
            finish()
        }

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        val adapter: BluetoothAdapter?
        adapter = BluetoothAdapter.getDefaultAdapter()
        if (adapter != null) {
            if (!adapter.isEnabled) {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)

            }        }

        // Get Bluetooth
        messages = findViewById(R.id.bluetoothText)
        messages!!.movementMethod = ScrollingMovementMethod()
        //bluetoothText.text =
        ble = BLE(applicationContext, Settings.Global.DEVICE_NAME)

        // Check permissions
        ActivityCompat.requestPermissions(this,
            arrayOf( Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)

        //seconds.hint = "Seconds"
        //minutes.hint = "MIN"
        //hours.hint = "HOUR"
    }


    fun startCount (v: View){

        ble!!.send("done")

    }

    override fun onResume() {
        super.onResume()
        //updateButtons(false)
        ble!!.registerCallback(this)


    }

    override fun onStop() {
        super.onStop()
        ble!!.unregisterCallback(this)
        ble!!.disconnect()
    }

    fun Table1(v: View) {
        name ="TABLE 1"
        startScan(name)

    }

    fun Table2(v: View) {
        name ="TABLE 2"
        startScan(name)
    }

    fun Table3(v: View) {
        name ="TABLE 3"
        startScan(name)
    }

    fun exitTable(v: View) {
        ble!!.send("exit")
        currentTable.tableBusy=false
        if(currentTable.updateObjectInDB()) {
            writeLine(name + " is free.")
        }
        onStop()
        Thread.sleep(300)
        finish()
    }

    private fun startScan(s:String) {
        writeLine("Scanning for devices ...")
        ble!!.connectFirstAvailable(s)
    }

    /**
     * Writes a line to the messages textbox
     * @param text: the text that you want to write
     */
    private fun writeLine(text: CharSequence) {
        runOnUiThread {
            messages!!.append(text)
            messages!!.append("\n")
        }
    }

    /**
     * Called when a UART device is discovered (after calling startScan)
     * @param device: the BLE device
     */
    override fun onDeviceFound(device: BluetoothDevice) {
        writeLine("Found device : " + device.name)
        writeLine("Waiting for a connection ...")
    }

    /**
     * Prints the devices information
     */
    override fun onDeviceInfoAvailable() {
        writeLine(ble!!.deviceInfo)
    }

    /**
     * Called when UART device is connected and ready to send/receive data
     * @param ble: the BLE UART object
     */
    override fun onConnected(ble: BLE) {
        writeLine("Connected!")

        currentTable=TablesDataA()
        currentTable.tableTableNUM=name
        currentTable.tableUserID=currentUser.userID
        currentTable.tableBusy=true
        currentTable.tableFinished=false
        if(currentTable.insertObjectInDB()) {
            Thread.sleep(500)
            currentTable= TablesDataA(currentTable.tableTableNUM,currentTable.tableBusy,currentTable.tableFinished)
        }
    }

    /**
     * Called when some error occurred which prevented UART connection from completing
     * @param ble: the BLE UART object
     */
    override fun onConnectFailed(ble: BLE) {
        writeLine("Error connecting to device!")
    }

    /**
     * Called when the UART device disconnected
     * @param ble: the BLE UART object
     */
    override fun onDisconnected(ble: BLE) {
        writeLine("Disconnected!")
    }

    /**
     * Called when data is received by the UART
     * @param ble: the BLE UART object
     * @param rx: the received characteristic
     */
    override fun onReceive(ble: BLE, rx: BluetoothGattCharacteristic) { // IMPORTANT!!!
        //writeLine("receive " + rx.getStringValue(0))
        val dataAcquire = rx.getStringValue(0)
        var Status = ""
        var seconds = 0.0
        var secondsString = ""
        var tempString = ""
        //var temp = false
        for (i in 0..10) { //This part separates the data from the string to the STATUS
            val character = dataAcquire.get(i)
            if (character ==':'){
                break
            }
            Status += character
        }
        //writeLine("STATUS " + Status)
        //textSeconds.text = Status
        //***********************************************************************************
        // this part compares all the status
        if(Status == "START")
        {
            currentTable.tableBusy=true
            currentTable.tableFinished=false
            if(currentTable.updateObjectInDB()) {
                writeLine("START " + "id:"+currentTable.tableID)// this status means that the timer is running
            }

        }
        else if(Status == "WARNING1")//this status means 30 seconds have pass
        {
            var v =getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            v.vibrate(500)
            writeLine("WARNING1")
        }

        else if(Status == "WARNING2")//this status means 60 seconds have pass
        {
            var v =getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            v.vibrate(500)
            writeLine("WARNING2")
        }
        else if(Status == "FINISH")
        {
            var v =getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            v.vibrate(500)
            writeLine("FINISH")// this status means 90 seconds have pass
            currentTable.tableFinished=true
            var user=UsersDataA()
            user.updateAVE(currentTable.tableUserID,90.0)
        }
        else if(Status == "DONE")// this status means the waiter was able to answer the call in the time range
        {
            secondsString += dataAcquire.get(5)
            secondsString += dataAcquire.get(6)
            currentTable.tableFinished=true
            //writeLine("FINISH")// this status means 90 seconds have pass
            writeLine("id:"+currentTable.tableID)
            if(currentTable.updateObjectInDB()) {
                seconds = secondsString.toDouble() // This is the variable in seconds of the timer that was done
                writeLine(Status + " " + secondsString)
            }
            var user=UsersDataA()
            user.updateAVE(currentTable.tableUserID,seconds)
        }
        else if(Status == "TEMP")// this status means the waiter was able to answer the call in the time range
        {
            tempString += dataAcquire.get(5)
            tempString += dataAcquire.get(6)
            //seconds = secondsString.toInt() // This is the variable in seconds of the timer that was done
            temperature.text = "Temperature: " + tempString + "°C"
        }
//        else
//        temperature.text = "Temperature: " + rx.getStringValue(0) + "°C"

    }

    companion object {
        private val DEVICE_NAME = "TABLE 1"
        private val REQUEST_ENABLE_BT = 0
    }

}
