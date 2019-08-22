/*********************************************************************
  * Laura Arjona. PMPEE590
  * Example of simple interaction beteween Adafruit Circuit Playground
  * and Android App. Communication with BLE - uart
*********************************************************************/
#include <Arduino.h>
#include <SPI.h>
#include "Adafruit_BLE.h"
#include "Adafruit_BluefruitLE_SPI.h"
#include "Adafruit_BluefruitLE_UART.h"
#include <Adafruit_CircuitPlayground.h>

#include "BluefruitConfig.h"

#if SOFTWARE_SERIAL_AVAILABLE
  #include <SoftwareSerial.h>
#endif


// Strings to compare incoming BLE messages
String startCount = "done";
String stopCount = "stop";
String blue = "blue";
String red = "red";
String readtemp = "readtemp";
String stp = "stop";
String clearCount = "clear";

int  sensorTemp = 0;

// Colorful_Timer variables
const int ledPin=  LED_BUILTIN;         // the number of the LED pin
int ledState= LOW;                      // ledStateused to set the LED
unsigned long previousMillis= 0;        // will store last time LED was updated
unsigned long previousMillis2= 0;        // will store last time LED was updated
long interval = 1000;              // interval at which to blink (milliseconds)
long interval2 = 50;
long speed_light = 300;
uint8_t pixeln = 0;
uint8_t pixeln2 = 0;
bool temp = false;
bool temp2 = false;
bool start = true;
bool startleft = false;
bool startright = false;
bool leftButtonPressed;
bool rightButtonPressed;
bool nextnum = false;
bool finish = true;
int seconds = 0;
int minutes = 0;
String data;
int temperature = 2;

/*=========================================================================
    APPLICATION SETTINGS
    -----------------------------------------------------------------------*/
    #define FACTORYRESET_ENABLE         0
    #define MINIMUM_FIRMWARE_VERSION    "0.6.6"
    #define MODE_LED_BEHAVIOUR          "MODE"
/*=========================================================================*/

// Create the bluefruit object, either software serial...uncomment these lines

Adafruit_BluefruitLE_UART ble(BLUEFRUIT_HWSERIAL_NAME, BLUEFRUIT_UART_MODE_PIN);

/* ...hardware SPI, using SCK/MOSI/MISO hardware SPI pins and then user selected CS/IRQ/RST */
// Adafruit_BluefruitLE_SPI ble(BLUEFRUIT_SPI_CS, BLUEFRUIT_SPI_IRQ, BLUEFRUIT_SPI_RST);

/* ...software SPI, using SCK/MOSI/MISO user-defined SPI pins and then user selected CS/IRQ/RST */
//Adafruit_BluefruitLE_SPI ble(BLUEFRUIT_SPI_SCK, BLUEFRUIT_SPI_MISO,
//                             BLUEFRUIT_SPI_MOSI, BLUEFRUIT_SPI_CS,
//                             BLUEFRUIT_SPI_IRQ, BLUEFRUIT_SPI_RST);


// A small helper
void error(const __FlashStringHelper*err) {
  Serial.println(err);
  while (1);
}

/**************************************************************************/
/*!
    @brief  Sets up the HW an the BLE module (this function is called
            automatically on startup)
*/
/**************************************************************************/
void setup(void)
{
  CircuitPlayground.begin();
  

  Serial.begin(115200);
  Serial.println(F("Adafruit Bluefruit Command <-> Data Mode Example"));
  Serial.println(F("------------------------------------------------"));

  /* Initialise the module */
  Serial.print(F("Initialising the Bluefruit LE module: "));

  if ( !ble.begin(VERBOSE_MODE) )
  {
    error(F("Couldn't find Bluefruit, make sure it's in CoMmanD mode & check wiring?"));
  }
  Serial.println( F("OK!") );

  if ( FACTORYRESET_ENABLE )
  {
    /* Perform a factory reset to make sure everything is in a known state */
    Serial.println(F("Performing a factory reset: "));
    if ( ! ble.factoryReset() ){
      error(F("Couldn't factory reset"));
    }
  }

  /* Disable command echo from Bluefruit */
  ble.echo(false);

  Serial.println("Requesting Bluefruit info:");
  /* Print Bluefruit information */
  ble.info();

  Serial.println(F("Please use Adafruit Bluefruit LE app to connect in UART mode"));
  Serial.println(F("Then Enter characters to send to Bluefruit"));
  Serial.println();

  ble.verbose(false);  // debug info is a little annoying after this point!

  /* Wait for connection */
  while (! ble.isConnected()) {
      delay(500);
  }

  Serial.println(F("******************************"));

  // LED Activity command is only supported from 0.6.6
  if ( ble.isVersionAtLeast(MINIMUM_FIRMWARE_VERSION) )
  {
    // Change Mode LED Activity
    Serial.println(F("Change LED activity to " MODE_LED_BEHAVIOUR));
    ble.sendCommandCheckOK("AT+HWModeLED=" MODE_LED_BEHAVIOUR);
  }

  // Set module to DATA mode
  Serial.println( F("Switching to DATA mode!") );
  ble.setMode(BLUEFRUIT_MODE_DATA);

  Serial.println(F("******************************"));

  CircuitPlayground.setPixelColor(0,255,0,0);
 
  delay(1000);
}
/**************************************************************************/
/*!
    @brief  Constantly poll for new command or response data
*/
/**************************************************************************/
void loop(void)
{ 
  unsigned long currentMillis= millis();
  unsigned long currentMillis2= millis();
  rightButtonPressed= CircuitPlayground.rightButton();
  // Save received data to string
  String received = "";
  String secondsChar = "";
  while ( ble.available() )
  {
    int c = ble.read();
    //Serial.print((char)c);
    if((char)c==':')
      nextnum = true;
    else if(nextnum == false)
      received += (char)c;
    else if(nextnum == true)
      secondsChar += (char)c;
    //Serial.println(received);
    //Serial.println(secondsChar);
    delay(50);

  }

  if(received=="done"){
    Serial.println("RECEIVED done!");
    //sendinfo();
    //startright = true;
    delay(50);
    char output[8];
    data = "DONE:";
    if(pixeln<10){
      data += "0";
    }
    data += pixeln;
    Serial.println(data);
    data.toCharArray(output,8);
    ble.print(data);
    clearboard();
  }
  if(received=="exit"){
    Serial.println("RECEIVED exit!");
    //sendinfo();
    //startright = true;
    clearboard();
  }
 
//********************************
//start counter by rigth push button or BLUETOOTH    
  if (start== true){ 
  if (rightButtonPressed) {
     if(startright == false){
      temp = true;
      start = false;
     }     
     startright = true;
  }else {startright = false;}}

  if (start== false){
  if (rightButtonPressed) {
     if(startright == false){
        char output[8];
        data = "DONE:";
        if(pixeln<10){
          data += "0";
        }
        data += pixeln;
        Serial.println(data);
        data.toCharArray(output,8);
        ble.print(data);
        clearboard();
     }
     startright = true;     
  } else {startright = false;}}

  if (currentMillis2-previousMillis2>= interval2) {
    previousMillis2= currentMillis2;// save the last time you blinked the LED
    if (temp==true){
      CircuitPlayground.setPixelColor(pixeln2, CircuitPlayground.colorWheel(25));
      //Serial.println(pixeln2);
      pixeln2++;
    }
    if (temp2==true){
      CircuitPlayground.setPixelColor(pixeln2, 0, 0, 0);
      //Serial.println(pixeln2);
      pixeln2--;
    }
    if (pixeln2==10){
      pixeln2=10;
    }
    if (pixeln2==255){
      temp2=false;
      pixeln2=0;
    }      
  }

  if (currentMillis-previousMillis>= interval) {
    previousMillis= currentMillis;// save the last time you blinked the LED
    sensorTemp = CircuitPlayground.temperature(); // returns a floating point number in Centigrade
    if (temp == true){
      if(pixeln == 0){
        char output[8];
        data = "START:";
        //data += pixeln;
        Serial.println(data);
        data.toCharArray(output,8);
        ble.print(data);    
      }
      pixeln++;
      seconds++;
      Serial.println(pixeln);
    }
    if (pixeln == temperature)
    {
      char output[8];
      data = "TEMP:";
      data += sensorTemp;
      Serial.println(data);
      data.toCharArray(output,8);
      ble.print(data);
      temperature+=12;
    }
    if (pixeln == 60)
    {
      minutes = 1;
      seconds = 0;
      char output[8];
      data = "WARNING2:";
      //data += pixeln;
      Serial.println(data);
      data.toCharArray(output,8);
      ble.print(data);
    }
    if (pixeln == 30)
    {
      char output[8];
      data = "WARNING1:";
      //data += pixeln;
      Serial.println(data);
      data.toCharArray(output,8);
      ble.print(data);
    }
    if (pixeln == 90)
    {
      char output[8];
      data = "FINISH:";
      //data += pixeln;
      Serial.println(data);
      data.toCharArray(output,8);
      ble.print(data);
      clearboard();
    }
  }
  }
//
void clearboard(void){
    start = true;
    pixeln = 0;
    pixeln2 = 10; 
    minutes = 0;
    seconds = 0;
    temp = false;
    temp2 = true;
    temperature = 2;
    //CircuitPlayground.clearPixels();
  }


 
