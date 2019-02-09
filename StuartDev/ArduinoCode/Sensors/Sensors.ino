#include <Wire.h>

int sensorPin = A0;
int ledPin = 3;
int differenceThreshold = 20;
boolean hasBall = false;
int onAvg = 0;
int offAvg = 0;
int numTrues = 0;
int numChecks = 0;
int difference = 0;
int onThrowouts = 10;
int offThrowouts = 15;
String piOutput = "none";
String input = "blank";

void irStates(char val) {
  switch (val) {
    case 'A': {
      digitalWrite(ledPin, HIGH);
      for(int i = 0; i < onThrowouts; i++){
        analogRead(sensorPin);
      }
      int total = analogRead(sensorPin);
      total += analogRead(sensorPin);
      total += analogRead(sensorPin);
      onAvg = total/3;
      break;
    }
    case 'B': {
      digitalWrite(ledPin, LOW);
      for(int i = 0; i < offThrowouts; i++){
        analogRead(sensorPin);
      }
      int total = analogRead(sensorPin);
      total += analogRead(sensorPin);
      total += analogRead(sensorPin);
      offAvg = total/3;
      break;
    }
  }
}

//turns the sensor on and off
void blink() {
  irStates('A');
  check();
  irStates('B');
  check();
}

void check() {
  difference = abs(onAvg - offAvg);
  if (difference > differenceThreshold) {
    numTrues++;
  }
  numChecks++;
}

void setup() {
  Serial.begin(9600);
  Wire.begin(4);
  pinMode(ledPin, OUTPUT);
}

void loop() {
  blink();
  blink();
  blink();
  //If number of trues is atleast 1 under the number of checks
  if(numTrues >= numChecks-1){
    hasBall = true;
  }
  else{
    hasBall = false;
  }
  Serial.println(String(numTrues) + "|" + String(numChecks) + "|" + String(onAvg) + "|" + String(offAvg) + "|" + String(difference));
  //Serial.println(hasBall);
  piOutput = String(hasBall);
  numTrues = 0;
  numChecks = 0;
//  digitalWrite(ledPin, HIGH);
//  delay(1000);
//  digitalWrite(ledPin, LOW);
//  for(int i = 0; i < 30; i++){
//    Serial.println(String(analogRead(sensorPin)));
//  }
}

void requestEvent(){//called when RoboRIO request a message from this device
  Wire.write(piOutput.c_str()); //writes data to the RoboRIO, converts it to string
}

void receiveEvent(int bytes){//called when RoboRIO "gives" this device a message
}


