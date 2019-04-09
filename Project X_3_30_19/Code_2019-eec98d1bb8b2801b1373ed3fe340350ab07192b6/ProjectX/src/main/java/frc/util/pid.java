package frc.util;

import edu.wpi.first.wpilibj.Timer;

/**
 * Custom PID Loop Class
 * @author Harrison McCarty
 */

public class pid {
	private double kP;
	private double kI;
	private double kD;
    private double kF;
    private double kMaxSpeed = 1.0;
    
	private double iError, iPrevError, iIntegral, iDerivative, iOutput, kIntegralMax = 0;
	private double iCurrTime;
	private double iLastTime = Timer.getFPGATimestamp();
	private double iDeltaTime;

	public pid (double kP, double kI, double kD) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
	}

	public pid (double kP, double kI, double kD, double kMaxSpeed) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		this.kMaxSpeed = kMaxSpeed;
	}

	public pid (double kP, double kI, double kD, double kMaxSpeed, double kF) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		this.kMaxSpeed = kMaxSpeed;
		this.kF = kF;
	}

	public pid (double kP, double kI, double kD, double kMaxSpeed, double kF, double kIntegralMax) {
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
        this.kMaxSpeed = kMaxSpeed;
        this.kF = kF;
		this.kIntegralMax = kIntegralMax;
	}

	public double returnOutput(double current, double setpoint) {
		iError = setpoint - current;
		return returnOutput(iError);
	}

	public double returnOutput(double error) {
		iDeltaTime = .02;
		iIntegral += (error* iDeltaTime);
		if(kIntegralMax != 0.0) { 
			if(iIntegral>kIntegralMax)
			{
				iIntegral = kIntegralMax;
			}
			else if(iIntegral<-kIntegralMax)
			{
				iIntegral = -kIntegralMax;
			}
		}

		iDerivative = (error - iPrevError)/iDeltaTime;
		iPrevError = error;

		iOutput = (kP*error) + (kI*iIntegral) + (kD*iDerivative);

		if(iOutput>kMaxSpeed) {
			iOutput = kMaxSpeed;
		}
		else if(iOutput<-kMaxSpeed) {
			iOutput = -kMaxSpeed;
		}

		return iOutput;
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