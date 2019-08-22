# Table-Master
TableMaster is an Android app for restaurants to monitor tables and respond to customers who are requesting service. There are two branches of the app, one for supervisors and one for staff. An Arduino sits on each table and is connected to the staff’s phone over BLE. 
For the staff activity, the customer can request service by pressing the button on the Arduino. This will notify the phone and a timer starts. At 30 seconds the phone will get and alert, then again at 60 seconds, and again at 90 seconds. The staff member or customer can stop the timer by pressing the button on the Arduino, or the staff member can stop the timer from the app. After each run of the timer the average time taken to respond to tables is recalculated and saved in the database. The temperature sensor on the Arduino is used to send the table’s temperature and display it in the app. 
Supervisors have a separate activity where they can add new staff members to give them login access to the app, as well as edit their name and status of being a supervisor or not. The Supervisor can see a list of all current staff members and their average time to respond to tables. Supervisors can also monitor which tables have customers.
Authentication to the app is done through Firebase Auth, using email address and password for login. Password resets can be done from the Firebase console, which sends an email to the staff person with a reset link. An initial password is assigned when adding a new staff member, which is done through the app.

![App Logo](/images/heading.PNG)
![App Structure](/images/structure.PNG)
![App FireStore](/images/firestore.PNG)
![App noSQL](/images/SQLandNoSQL.PNG)
![App Data](/images/data.PNG)
![App Staff Layout](/images/staffLayout.PNG)
![App Supervisor Layout](/images/SupervisorLayout.PNG)
