package frc.robot;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Elevator {
   private CANSparkMax motor;
   private CANEncoder encoder;

   public Elevator(int busNum) {
        motor = new CANSparkMax(busNum, MotorType.kBrushless);
        encoder = motor.getEncoder();
   }

   public void run(double speed) {
       motor.set(speed);
   }

   public void getPostition() {
       System.out.println(encoder.getPosition());
   }
}
