double dato1, dato2, dato3, dato4;


void setup() {

  Serial.begin(9600);

}

void loop() {

  dato1 = random(0,1024);
  dato2 = random(0,1024);
  dato3 = random(0,1024);
  dato4 = random(0,1024);

  String sl = String((dato1/1024.0)*8.0);
  String sw = String((dato2/1024.0)*5.0);
  String pl = String((dato3/1024.0)*7.0);
  String pw = String((dato4/1024.0)*3.0);

  Serial.println("{\"sl\":" + sl + ", \"sw\":" + sw +", \"pl\":" + pl + ", \"pw\":" + pw + "}");

  delay(3000);  

}
