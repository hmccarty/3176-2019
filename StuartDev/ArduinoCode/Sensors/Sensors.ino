int sensorPin = A0;
int ledPin = 13;
int irValues[] = {-1, -1, -1, -1, -1, -1};
int lastVal = -1;
int currentVal = -1;
int differenceThreshold;
boolean hasBall = false;

void irStates(char val, int i){
  switch (val) {
    case 'A': {
      digitalWrite(ledPin, HIGH);
      irValues[i] = analogRead(sensorPin);
      break;
    }
    case 'B': {
      digitalWrite(ledPin, LOW);
      irValues[i] = analogRead(sensorPin);
      break;
    }
  }
}

void shouldTest() {
  if(abs(currentVal-lastVal) > differenceThreshold){
    testBall;
  }
}

void testBall() {
  irStates('A', 0);
  delay(50);
  irStates('B', 1);
  delay(50);
  irStates('A', 2);
  delay(50);
  irStates('B', 3);
  delay(50);
  irStates('A', 4);
  delay(50);
  irStates('B', 5);
  for(int i = 0; i < sizeof(irValues)-1; i++) {                 //need logic help pls
    if(abs(irValues[i] - irValues[i+1]) < differenceThreshold){
      hasBall = false;
    }
  }
}

void setup() {
  Serial.begin(9600);
  pinMode(ledPin, OUTPUT);
}

void loop() {
  shouldTest();
  Serial.println(hasBall);
}
