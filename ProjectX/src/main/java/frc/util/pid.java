package frc.util;

import edu.wpi.first.wpilibj.Timer;

/**
 * Custom PID Loop Class
 * @author Harrison McCarty
 */

public class pid {
	private double kP, kI, kD, kF;
	private double kMaxSpeed, kMinSpeed; 
    
	private double error, prevError, integral, derivative, output, kIntegralMax; 
	private double currTime;
	private double lastTime = Timer.getFPGATimestamp();
	private double deltaTime;

	public pid (double kP, double kI, double kD, double kF, double kMaxSpeed, double kMinSpeed, double kIntegralMax) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		this.kF = kF; 

		this.kMaxSpeed = kMaxSpeed;
		this.kMinSpeed = kMinSpeed;

		this.kIntegralMax = kIntegralMax; 
	}


	public double returnOutput(double current, double setpoint) {
		lastTime = currTime;
		currTime = Timer.getFPGATimestamp();
		deltaTime = currTime - lastTime;

		integral += (error * deltaTime);
		if(kIntegralMax != 0.0) { 
			if(integral > kIntegralMax) {
				integral = kIntegralMax;
			}
			else if(integral < -kIntegralMax) {
				integral = -kIntegralMax;
			}
		}

		derivative = (error - prevError)/deltaTime;
		prevError = error;

		output = (kP * error) + (kI * integral) + (kD * derivative) + (kF * setpoint);

		if(output > kMaxSpeed) {
			output = kMaxSpeed;
		} else if(output < kMinSpeed) {
			output = kMinSpeed;
		}
		return output;
	}

	//getters and setters for everything
	public double getkP() {return kP;}
	public void setkP(double kP) {this.kP = kP;}

	public double getkI() {return kI;}
	public void setkI(double kI) {this.kI = kI;}

	public double getkD() {return kD;}
	public void setkD(double kD) {this.kD = kD;}

	public double getkF() {return kF;}
	public void setkF(double kF) {this.kF = kF;}

	public double getIntegralMax() {return kIntegralMax;}
	public void setIntegralMax(double integralMax) {this.kIntegralMax = integralMax;}

	public double getMax_speed() {return kMaxSpeed;}
	public void setMax_speed(double max_speed) {this.kMaxSpeed = max_speed;}
}