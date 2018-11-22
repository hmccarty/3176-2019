package subsystem;

public class drivetrain {
	private drivetrain instance = new drivetrain("tank", 2);
	private int numberOfMotors; 
	private String driveBase; 
	
	public drivetrain(String driveBase, int numberOfMotors) {
		this.driveBase = driveBase;
		this.numberOfMotors = numberOfMotors; 
	}
	
	public void drive(double magnitude, double angularVelocity) {
		//NOT ACTUAL IMPLEMENTATION (HOLDING PLACE ONLY)
	}
	
}
