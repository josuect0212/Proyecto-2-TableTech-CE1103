const int PIN_A = 34;
const int PIN_B = 22;
const int PIN_C = 24;
const int PIN_D = 32;
const int PIN_E = 30;
const int PIN_F = 28;
const int PIN_G = 26;
const int PIN_LED_A = 13;
const int PIN_LED_B = 12;
const int PIN_LED_C = 11;
const int PIN_BUZZ = 3;

const byte DIGIT_PATTERNS[][7] = {
  {0, 0, 0, 1, 0, 0, 0},  // 0
  {1, 1, 0, 1, 1, 1, 0},  // 1
  {1, 0, 0, 0, 0, 0, 1}, // 2
  {1, 0, 0, 0, 1, 0, 0}, // 3
  {0, 1, 0, 0, 1, 1, 0}, // 4
  {0, 0, 1, 0, 1, 0, 0}, // 5
  {0, 0, 1, 0, 0, 0, 0}, // 6
  {1, 0, 0, 1, 1, 1, 0}, // 7
  {0, 0, 0, 0, 0, 0, 0}, // 8
  {0, 0, 0, 0, 1, 0, 0}, // 9
};

const int DIGIT_COUNT = 10;

void setup() {
  pinMode(PIN_A, OUTPUT);
  pinMode(PIN_B, OUTPUT);
  pinMode(PIN_C, OUTPUT);
  pinMode(PIN_D, OUTPUT);
  pinMode(PIN_E, OUTPUT);
  pinMode(PIN_F, OUTPUT);
  pinMode(PIN_G, OUTPUT);
  pinMode(PIN_LED_A, OUTPUT);
  pinMode(PIN_LED_B, OUTPUT);
  pinMode(PIN_LED_C, OUTPUT);
  pinMode(PIN_BUZZ, OUTPUT);

  Serial.begin(9600);
}

void loop() {
  if (Serial.available()) {
    String input = Serial.readString();
    int number = input.toInt();
    displayNumber(number);
    char command = Serial.read();
    handleCommand(command);
  }
}

void displayNumber(int number) {
  if (number < 0 || number >= DIGIT_COUNT) {
    return;
  }

  byte* segmentPattern = DIGIT_PATTERNS[number];

  digitalWrite(PIN_A, segmentPattern[0]);
  digitalWrite(PIN_B, segmentPattern[1]);
  digitalWrite(PIN_C, segmentPattern[2]);
  digitalWrite(PIN_D, segmentPattern[3]);
  digitalWrite(PIN_E, segmentPattern[4]);
  digitalWrite(PIN_F, segmentPattern[5]);
  digitalWrite(PIN_G, segmentPattern[6]);
}

void handleCommand(char command) {
  switch (command) {
    case 'A':
      digitalWrite(PIN_LED_A, HIGH);
      break;
    case 'B':
      digitalWrite(PIN_LED_B, HIGH);
      break;
    case 'C':
      digitalWrite(PIN_LED_C, HIGH);
      break;
    case 'S1':
      playBuzzer();
    case 'S2':
      playBuzzer2();
  }
}
void playBuzzer() {
  tone(PIN_BUZZ, 1000);
}
void playBuzzer2() {
  tone(PIN_BUZZ, 2000);
}