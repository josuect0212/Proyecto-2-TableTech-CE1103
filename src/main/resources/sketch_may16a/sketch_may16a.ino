int displayPins[] = {6, 7, 8, 9, 10, 5, 4};
bool numberSegments[][7] = {
  {0, 0, 0, 0, 0, 0, 1}, // 0
  {1, 0, 0, 1, 1, 1, 1}, // 1
  {0, 0, 1, 0, 0, 1, 0}, // 2
  {0, 0, 0, 0, 1, 1, 0}, // 3
  {1, 0, 0, 1, 1, 0, 0}, // 4
  {0, 1, 0, 0, 1, 0, 0}, // 5
  {0, 1, 0, 0, 0, 0, 0}, // 6
  {0, 0, 0, 1, 1, 1, 1}, // 7
  {0, 0, 0, 0, 0, 0, 0}, // 8
  {0, 0, 0, 0, 1, 0, 0}  // 9
};

const int buzzerPin = 3;

void setup() {
  pinMode(13, OUTPUT);
  pinMode(12, OUTPUT);
  pinMode(11, OUTPUT);
  
  for (int i = 0; i < 7; i++) {
    pinMode(displayPins[i], OUTPUT);
  }

  pinMode(buzzerPin, OUTPUT);
}

void loop() {
  // The Arduino will listen for commands from Java
  if (Serial.available()) {
    char command = Serial.read();
    if (command == 'N') {
      int orders = Serial.parseInt();
      displayNumber(orders);
    }
  }
}

void displayNumber(int number) {
  if (number < 0 || number > 9) {
    return;
  }
  
  for (int i = 0; i < 7; i++) {
    digitalWrite(displayPins[i], numberSegments[number][i]);
  }
}

void playBuzzer() {
  digitalWrite(buzzerPin, HIGH);
  delay(1000);
  digitalWrite(buzzerPin, LOW);
  delay(2000);  
}