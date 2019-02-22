package frc.robot; 

public class constants {
	/***************\
	|* Motor Ports *|
	\***************/
	public static int DRIVE_ONE = 1;
	public static int DRIVE_TWO = 2;
	public static int DRIVE_THREE = 3;
	public static int DRIVE_FOUR = 4; 

	public static int STEER_ONE = 11;
	public static int STEER_TWO = 22; 
	public static int STEER_THREE = 33; 
	public static int STEER_FOUR = 44; 

	public static int ELEVATOR_LEFT = 5; 
	public static int ELEVATOR_RIGHT = 9; 

	public static int CARGO_INTAKE_ACTUATOR = 0; 
	public static int CARGO_INTAKE_ROLLER = 1; 
	
	/****************\
	|* Sensor Ports *|
	\****************/

    public static int CARGO_INTAKE_DOWN = 0; 
    public static int CARGO_INTAKE_UP = 1;

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
	public static double OFFSETS[] = {2663,-363,-329,3331};
	public static double OFFSETS_P[] = {268.0,3228.0,1184.0,2160.0};
	public static double DRIVETRAINLENGTH = 23.5;
	public static double DRIVETRAINWIDTH = 24.0;
	public static double DRIVETRAINWIDTH_2 = 26.0;
	public static final double WHEELDIAMETER = 3.0;
	public static double DRIVETRAINMAXWHEELSPEED = 13.5; // ft/s
	public static double DRIVETRAINMAXROTATIONSPEED = 5.0; // radians/s
	public static double ENCODER_UNITS = 4096.0;
	
	/***********************\
	|* Swervepod Constants *|
	\***********************/

	public static final double SWERVE_kP = 4.0;
	public static final double SWERVE_kI = 0.0023;
	public static final double SWERVE_kD = 210.0;
	public static final double SWERVE_kF = 0.0;
	public static final int SWERVE_ALLOWABLE_ERROR = 5;

	public static final double NEO_kP = 0.000095;
	public static final double NEO_kI = 0.0000009325;
	public static final double NEO_kD = 0;
	public static final double NEO_FF = 0;
	public static final double NEO_IZ = 0;
	
	public static final double DRIVE_GEAR_RATI0 = 17/54;
	
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
	public static double fps2rpm = 12/(3*Math.PI)*(48/30)*(54/17)*60;
	public static double rev2ft = (17/54)*(30/48)*3*Math.PI*(1/12);
	public static final int DRIVE_ALLOWABLE_ERROR = 50;
	
	public static final double MAXSLOWPERCENTSPEED = .4;
	public static final double DRIVE_RAMPRATE = .3;
	public static final double DRIVEGEARREDUCTION = 30.0/48.0;
	public static final double DRIVEMAXENCODERSPEED = 10000.0;

	/**********************\
	|* Elevator Constants *|
	\**********************/

}