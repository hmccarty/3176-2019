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
  pinMode(ledPin, OUTPUT);
}

void loop() {
  blink();
  blink();
  blink();
  if(numTrues >= numChecks-1){
    hasBall = true;
  }
  else{
    hasBall = false;
  }
  //Serial.println(String(numTrues) + "|" + String(numChecks) + "|" + String(onAvg) + "|" + String(offAvg) + "|" + String(difference));
  Serial.println(hasBall);
  numTrues = 0;
  numChecks = 0;
//  digitalWrite(ledPin, HIGH);
//  delay(1000);
//  digitalWrite(ledPin, LOW);
//  for(int i = 0; i < 30; i++){
//    Serial.println(String(analogRead(sensorPin)));
//  }
}
