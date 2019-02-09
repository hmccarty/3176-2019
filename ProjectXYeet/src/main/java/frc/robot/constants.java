package frc.robot; 

public class constants {
    public static int CARGO_INTAKE_DOWN = 0; 
    public static int CARGO_INTAKE_UP = 1;
    public static int CARGO_INTAKE_ACTUATOR = 2; 
    public static int CARGO_INTAKE_ROLLER = 3; 

	/****************************\
	|* Driver Station Constants *|
	\****************************/
	public static int DRIVE_JOYSTICK = 0; 
	public static int GEAR_JOYSTICK = 1;
	public static int BUTTON_MONKEY = 2;
	
	/************************\
	|* Drivetrain Constants *|
	\************************/
	//Gear Facing Right
	public static double OFFSETS[] = {9173,5870,1730,5423};
	public static double OFFSETS_P[] = {268.0,3228.0,1184.0,2160.0};
	public static double DRIVETRAINLENGTH = 17.5;
	public static double DRIVETRAINWIDTH = 13.0;
	public static final double WHEELDIAMETER = 3.0;
	public static double DRIVETRAINMAXWHEELSPEED = 13.5; // ft/s
	public static double DRIVETRAINMAXROTATIONSPEED = 5.0; // radians/s
	public static double ENCODER_UNITS = 4096.0;
	
	/***********************\
	|* Swervepod Constants *|
	\***********************/
	public static final double SWERVE_kP = 4.5;
	public static final double SWERVE_kI = 0.0023;
	public static final double SWERVE_kD = 210.0;
	public static final double SWERVE_kF = 0.0;
	public static final int SWERVE_ALLOWABLE_ERROR = 5;
	/*
	public static final double DRIVE_kP = .096;//Practice Values .096;
	public static final double DRIVE_kI = 0.0;//Practice Values 0.0;
	public static final double DRIVE_kD = 9.33;//Practice Values: 9.33;
	public static final double DRIVE_kF = 0.110654611;//Practice Value 0.09654611
	*/
	//as of 3/25/3018
	public static final double DRIVE_kP = .4;//Practice Values .096;
	public static final double DRIVE_kI = 0.0001;//Practice Values 0.0;
	public static final double DRIVE_kD = 9.33;//Practice Values: 9.33;
	public static final double DRIVE_kF = 0.130654611;//Practice Value 0.09654611
	public static final int DRIVE_IZONE = 200;
	public static double fps2ups = 12.0 /(constants.WHEELDIAMETER * Math.PI) * 4096.0/10.0 *48.0/30.0;
	public static final int DRIVE_ALLOWABLE_ERROR = 50;
	
	public static final double MAXSLOWPERCENTSPEED = .4;
	public static final double DRIVE_RAMPRATE = .3;
	public static final double DRIVEGEARREDUCTION = 30.0/48.0;
	public static final double DRIVEMAXENCODERSPEED = 10000.0;
}