#include <RelayBoard.h>
#include <Adafruit_CC3000.h>
#include <SPI.h>
#include "utility/debug.h"
#include "utility/socket.h"

// These are the interrupt and control pins
#define ADAFRUIT_CC3000_IRQ   3  // MUST be an interrupt pin!
// These can be any two pins
#define ADAFRUIT_CC3000_VBAT  5
#define ADAFRUIT_CC3000_CS    10
// Use hardware SPI for the remaining pins
// On an UNO, SCK = 13, MISO = 12, and MOSI = 11

#define data    9 //Define a palavra data como 6 para o pino D6 ser utilizado como o pino do DATA
#define strobe  8 //Define a palavra strobe como 4 para o pino D4 ser utilizado como o pino do STROBE
#define clock   7 //Define a palavra clock como 2 para o pino D2 ser utilizado como o pino do CLOCK
#define numberboards 1 //Define a palavra numberboards como 2, onde é definido quantas RelayBoard há no circuito

RelayBoard relay(data, strobe, clock, numberboards); 

Adafruit_CC3000 cc3000 = Adafruit_CC3000(ADAFRUIT_CC3000_CS, ADAFRUIT_CC3000_IRQ, ADAFRUIT_CC3000_VBAT,
                                         SPI_CLOCK_DIVIDER); // you can change this clock speed

#define WLAN_SSID       "teste2"   // cannot be longer than 32 characters!
#define WLAN_PASS       "123456789"
// Security can be WLAN_SEC_UNSEC, WLAN_SEC_WEP, WLAN_SEC_WPA or WLAN_SEC_WPA2
#define WLAN_SECURITY   WLAN_SEC_WPA2

#define LISTEN_PORT           80      // What TCP port to listen on for connections.
                                      // The HTTP protocol uses port 80 by default.

#define MAX_ACTION            10      // Maximum length of the HTTP action that can be parsed.

#define MAX_PATH              64      // Maximum length of the HTTP request path that can be parsed.
                                      // There isn't much memory available so keep this short!

#define BUFFER_SIZE           MAX_ACTION + MAX_PATH + 20  // Size of buffer for incoming request data.
                                                          // Since only the first line is parsed this
                                                          // needs to be as large as the maximum action
                                                          // and path plus a little for whitespace and
                                                          // HTTP version.

#define TIMEOUT_MS            500    // Amount of time in milliseconds to wait for
                                     // an incoming request to finish.  Don't set this
                                     // too high or your server could be slow to respond.

Adafruit_CC3000_Server httpServer(LISTEN_PORT);
uint8_t buffer[BUFFER_SIZE+1];
int bufindex = 0;
char action[MAX_ACTION+1];
char path[MAX_PATH+1];
int led = 2;
int analogValue = 0;
int analogValue2 = 0;
int analogValue3 = 0;
int analogValue4 = 0;
int analogValue5 = 0;
int analogValue6 = 0;
int mVperAmp = 185;
int ACSoffset = 2500;
void setup(void)
{

  Serial.begin(115200);
  Serial.println(F("Hello, CC3000!\n"));
  pinMode(led, OUTPUT);
  pinMode(A0, INPUT);
  pinMode(A1,INPUT);
  pinMode(A2,INPUT);
  pinMode(A3,INPUT);
  pinMode(A4,INPUT);
  pinMode(A5,INPUT);
  Serial.print("Free RAM: "); Serial.println(getFreeRam(), DEC);

  // Initialise the module
  Serial.println(F("\nInitializing..."));
  if (!cc3000.begin())
  {
    Serial.println(F("Couldn't begin()! Check your wiring?"));
    while(1);
  }

  Serial.print(F("\nAttempting to connect to ")); Serial.println(WLAN_SSID);
  if (!cc3000.connectToAP(WLAN_SSID, WLAN_PASS, WLAN_SECURITY)) {
    Serial.println(F("Failed!"));
    while(1);
  }

  Serial.println(F("Connected!"));

  Serial.println(F("Request DHCP"));
  while (!cc3000.checkDHCP())
  {
    delay(100); // ToDo: Insert a DHCP timeout!
  }

  // Display the IP address DNS, Gateway, etc.
  while (! displayConnectionDetails()) {
    delay(1000);
  }

  // ******************************************************
  // You can safely remove this to save some flash memory!
  // ******************************************************
  Serial.println(F("\r\nNOTE: This sketch may cause problems with other sketches"));
  Serial.println(F("since the .disconnect() function is never called, so the"));
  Serial.println(F("AP may refuse connection requests from the CC3000 until a"));
  Serial.println(F("timeout period passes.  This is normal behaviour since"));
  Serial.println(F("there isn't an obvious moment to disconnect with a server.\r\n"));

  // Start listening for connections
  httpServer.begin();

  Serial.println(F("Listening for connections..."));
}

void loop(void)
{
  // Try to get a client which is connected.
  Adafruit_CC3000_ClientRef client = httpServer.available();

  if (client) {
    Serial.println(F("Client connected."));
    // Process this request until it completes or times out.
    // Note that this is explicitly limited to handling one request at a time!

    // Clear the incoming data buffer and point to the beginning of it.
    bufindex = 0;
    memset(&buffer, 0, sizeof(buffer));

    // Clear action and path strings.
    memset(&action, 0, sizeof(action));
    memset(&path,   0, sizeof(path));

    // Set a timeout for reading all the incoming data.
    unsigned long endtime = millis() + TIMEOUT_MS;

    // Read all the incoming data until it can be parsed or the timeout expires.
    bool parsed = false;
    while (!parsed && (millis() < endtime) && (bufindex < BUFFER_SIZE)) {
      if (client.available()) {
        buffer[bufindex++] = client.read();
      }
      parsed = parseRequest(buffer, bufindex, action, path);
    }

    // Handle the request if it was parsed.
    if (parsed) {
      Serial.println(F("Processing request"));
      Serial.print(F("Action: ")); Serial.println(action);
      Serial.print(F("Path: ")); Serial.println(path);
      // Check the action to see if it was a GET request.
      if (strcmp(action, "GET") == 0) {
        // Respond with the path that was accessed.
        // First send the success response code.
        client.fastrprintln(F("HTTP/1.1 200 OK"));
        // Then send a few headers to identify the type of data returned and that
        // the connection will not be held open.
        client.fastrprintln(F("Content-Type: text/plain"));
        client.fastrprintln(F("Connection: close"));
        client.fastrprintln(F("Server: Adafruit CC3000"));
        // Send an empty line to signal start of body.
        client.fastrprintln(F(""));
        // Now send the response data.
        //client.fastrprintln(F("Hello world!"));
        //client.fastrprint(F("You accessed path: ")); client.fastrprintln(path);
        
        if(strcmp("/corrente",path) == 0){
            
          analogValue = (int) ((((analogRead(A0)/ 1024.0) * 5000)- ACSoffset)/mVperAmp);
          analogValue2 = (int) ((((analogRead(A1)/ 1024.0) * 5000)- ACSoffset)/mVperAmp);
          analogValue3 = (int) ((((analogRead(A2)/ 1024.0) * 5000)- ACSoffset)/mVperAmp);
          analogValue4 = (int) ((((analogRead(A3)/ 1024.0) * 5000)- ACSoffset)/mVperAmp);
          analogValue5 = (int) ((((analogRead(A4)/ 1024.0) * 5000)- ACSoffset)/mVperAmp);
          analogValue6 = (int) ((((analogRead(A5)/ 1024.0) * 5000)- ACSoffset)/mVperAmp);
          
          Serial.println(analogRead(A0));
          Serial.println(analogRead(A1));
          Serial.println(analogRead(A2));
          
            
            String response = "{\"sensores\":[";
            String aux = "]}";
            String coma = ",";
            String teste="";
            client.print(response+analogValue+coma+analogValue2+coma+analogValue3+coma+analogValue4+coma+analogValue5+coma+analogValue6+aux);
           // root.printTo(teste);
            //client.print(teste);
          }
          if(strcmp("/ledOn",path) == 0){
              digitalWrite(led, HIGH);
            }
        if(strcmp("/ledOff",path)==0){
          digitalWrite(led, LOW);
          }

        if(strcmp("/Sensor1Off",path)==0){
          digitalWrite(led, LOW);
          relay.set(0,1,0);
          }
        if(strcmp("/Sensor1On",path)==0){
          digitalWrite(led, HIGH);
          relay.set(0,1,1);
          }
        if(strcmp("/Sensor2Off",path)==0){
          digitalWrite(led, LOW);
          relay.set(0,2,0);
          }
        if(strcmp("/Sensor2On",path)==0){
          digitalWrite(led, HIGH);
          relay.set(0,2,1);
          }
          if(strcmp("/Sensor3Off",path)==0){
          digitalWrite(led, LOW);
          relay.set(0,3,0);
          }
        if(strcmp("/Sensor3On",path)==0){
          digitalWrite(led, HIGH);
          relay.set(0,3,1);
          }
    
        if(strcmp("/Sensor4Off",path)==0){
          digitalWrite(led, LOW);
          relay.set(0,4,0);
          }
        if(strcmp("/Sensor4On",path)==0){
          digitalWrite(led, HIGH);
          relay.set(0,4,1);
          }

          if(strcmp("/Sensor5Off",path)==0){
          digitalWrite(led, LOW);
          relay.set(0,5,0);
          }
        if(strcmp("/Sensor5On",path)==0){
          digitalWrite(led, HIGH);
          relay.set(0,5,1);
          } 

        if(strcmp("/Sensor6Off",path)==0){
          digitalWrite(led, LOW);
          relay.set(0,6,0);
          }
        if(strcmp("/Sensor6On",path)==0){
          digitalWrite(led, HIGH);
          relay.set(0,6,1);
          }              
      }
      else {
        // Unsupported action, respond with an HTTP 405 method not allowed error.
        client.fastrprintln(F("HTTP/1.1 405 Method Not Allowed"));
        client.fastrprintln(F(""));
      }
    }

    delay(100);

    // Close the connection when done.
    Serial.println(F("Client disconnected"));
    client.close();
  }
}

bool parseRequest(uint8_t* buf, int bufSize, char* action, char* path) {
  // Check if the request ends with \r\n to signal end of first line.
  if (bufSize < 2)
    return false;
  if (buf[bufSize-2] == '\r' && buf[bufSize-1] == '\n') {
    parseFirstLine((char*)buf, action, path);
    return true;
  }
  return false;
}

// Parse the action and path from the first line of an HTTP request.
void parseFirstLine(char* line, char* action, char* path) {
  // Parse first word up to whitespace as action.
  char* lineaction = strtok(line, " ");
  if (lineaction != NULL)
    strncpy(action, lineaction, MAX_ACTION);
  // Parse second word up to whitespace as path.
  char* linepath = strtok(NULL, " ");
  if (linepath != NULL)
    strncpy(path, linepath, MAX_PATH);
}

// Tries to read the IP address and other connection details
bool displayConnectionDetails(void)
{
  uint32_t ipAddress, netmask, gateway, dhcpserv, dnsserv;

  if(!cc3000.getIPAddress(&ipAddress, &netmask, &gateway, &dhcpserv, &dnsserv))
  {
    Serial.println(F("Unable to retrieve the IP Address!\r\n"));
    return false;
  }
  else
  {
    Serial.print(F("\nIP Addr: ")); cc3000.printIPdotsRev(ipAddress);
    Serial.println();
    return true;
  }
}


