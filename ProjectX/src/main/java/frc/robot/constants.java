package frc.robot; 

public class constants {
	/***************\
	|* Motor Ports *|
	\***************/	
												/***PORT TYPE***/
	public static int DRIVE_ONE = 1;			     /*CAN*/
	public static int DRIVE_TWO = 2;    		     /*CAN*/
	public static int DRIVE_THREE = 3;			     /*CAN*/
	public static int DRIVE_FOUR = 4;			     /*CAN*/

	public static int STEER_ONE = 11;			     /*CAN*/
	public static int STEER_TWO = 22;			     /*CAN*/
	public static int STEER_THREE = 33;			     /*CAN*/
	public static int STEER_FOUR = 44;			     /*CAN*/

	public static int ELEVATOR = 5;				     /*CAN*/

	public static int CARGO_INTAKE_ROLLER = 0;       /*PWM*/
	public static int CARGO_INTAKE_ACTUATOR = 1;     /*PWM*/

	
	/****************\
	|* Sensor Ports *|
	\****************/

	public static int CARGO_INTAKE_ENCODER[] = {0,1}; 
	public static int CARGO_INTAKE_DOWN = 3; 
	public static int CARGO_IN_INTAKE = 2; 

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
	public static double OFFSETS[] = {1945,1058,878,2363};
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

	public static final double SWERVE_KP = 4.0;
	public static final double SWERVE_KI = 0.0023;
	public static final double SWERVE_KD = 210.0;
	public static final double SWERVE_KF = 0.0;
	public static final int SWERVE_ALLOWABLE_ERROR = 5;

	public static final double DRIVE_KP = .4;
	public static final double DRIVE_KI = 0.0001;
	public static final double DRIVE_KD = 9.33;
	public static final double DRIVE_KF = 0.130654611;
	public static final int DRIVE_IZONE = 200;
	public static double fps2ups = 12.0 /(constants.WHEELDIAMETER * Math.PI) * 4096.0/10.0 *48.0/30.0;
	public static final int DRIVE_ALLOWABLE_ERROR = 50;
	
	public static final double MAXSLOWPERCENTSPEED = .4;
	public static final double DRIVE_RAMPRATE = .3;
	public static final double DRIVEGEARREDUCTION = 30.0/48.0;
	public static final double DRIVEMAXENCODERSPEED = 10000.0;

	/**********************\
	|* Elevator Constants *|
	\**********************/

	public static final double[] ELEVATOR_PID_CONFIG = { /*kP*/ 0.0, /*kI*/ 0.0, /*kD*/ 0.0, 
														 /*I-Zone*/ 0.0, 
														 /*Output Range Min*/ 0.0, /*Output Range Max*/ 0.0, 
														 /*kF*/ 0.0};
	public static final double[] ELEVATOR_MOTION_CONFIG = { /*Max Velocity*/ 0.0, /*Min Velocity*/ 0.0, 
															/*Max Acceleration*/ 0.0, 
															/*Allowed Error*/ 0.0 };

	/**************************\
	|* Cargo Intake Constants *|
	\**************************/
	
	public static final int DEPLOYED_HEIGHT = 10;
    public static final int STOWED_HEIGHT = 10000;
	public static final int ROCKET_HEIGHT = 24000;
}