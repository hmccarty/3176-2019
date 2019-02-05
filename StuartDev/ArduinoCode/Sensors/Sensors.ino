int sensorPin = A0;
int ledPin = 3;
int differenceThreshold = 0;
boolean hasBall = false;
int onAvg = 0;
int offAvg = 0;
int numTrues = 0;
int numChecks = 0;
int difference = 0;


void irStates(char val) {
  switch (val) {
    case 'A': {
      digitalWrite(ledPin, HIGH);
      analogRead(sensorPin);
      int total = analogRead(sensorPin);
      total += analogRead(sensorPin);
      total += analogRead(sensorPin);
      onAvg = total/3;
      break;
    }
    case 'B': {
      digitalWrite(ledPin, LOW);
      analogRead(sensorPin);
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
  difference = abs(onAvg- offAvg);
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
  Serial.println(String(numTrues) + "|" + String(numChecks) + "|" + String(onAvg) + "|" + String(offAvg) + "|" + String(difference));
  numTrues = 0;
  numChecks = 0;
  //Serial.println(hasBall);
}
