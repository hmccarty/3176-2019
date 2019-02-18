#include <SPI.h>

void setup() {
  // put your setup code here, to run once:
  SPI.begin();
}

void loop() {
  // put your main code here, to run repeatedly:
  int data = 5;
  SPI.transfer(data);
}
