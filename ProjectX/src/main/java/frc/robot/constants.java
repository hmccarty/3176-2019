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
	public static int HATCH_INTAKE_ROLLER = 2;       /*PWM*/  

	/****************\
	|* Piston Ports *|
	\****************/	

	public static int CROSSBOW_OUTER_FRONT = 3; 
	public static int CROSSBOW_OUTER_BACK = 4; 
	public static int CROSSBOW_INNER_FRONT = 2; 
	public static int CROSSBOW_INNER_BACK = 5; 
	public static int HATCH_INTAKE_ACTUATOR_FRONT = 5; 
	public static int HATCH_INTAKE_ACTUATOR_BACK = 6; 

	/****************\
	|* Sensor Ports *|
	\****************/

	public static int CARGO_INTAKE_ENCODER[] = {0,1}; 
	public static int CARGO_IN_INTAKE = 2;   // DIO port for switch that detects when cargo is in intake
	public static int CARGO_INTAKE_STOWED = 3;  // DIO port that detects when the cargo intake is 
	public static int CARGO_INTAKE_DOWN = 4;
	public static int HATCH_IN_INTAKE = 5; 

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
	public static double OFFSETS[] = {2601,1135,3468,1762}; // Pod Absolute Offsets (Pod 1-4)
	public static double DRIVETRAINLENGTH = 30.5; // inches
	public static double DRIVETRAINWIDTH = 29.5; // inches
	public static final double WHEELDIAMETER = 3.5;
	public static double DRIVETRAINMAXWHEELSPEED = 13.5; // ft/s
	public static double DRIVETRAINMAXROTATIONSPEED = 5.0; // radians/s
	public static double ENCODER_UNITS = 4096.0; // encoder units
	
	/***********************\
	|* Swervepod Constants *|
	\***********************/

	public static final double SWERVE_KP[] = {4.0, 4.0, 4.0, 4.0}; // proportional gain for turning each pod
	public static final double SWERVE_KI[] = {0.0023, 0.0023, 0.0023, 0.0023}; // integral gain for turning each pod
	public static final double SWERVE_KD[] = {210.0, 210.0, 210.0, 210.0}; // derivative gain for turning each pod
	public static final double SWERVE_KF = 0.0;
	public static final int SWERVE_ALLOWABLE_ERROR = 5; 

	public static final double DRIVE_KP = .4; // proportional gain for driving each pod
	public static final double DRIVE_KI = 0.0001; // integral gain for driving each pod
	public static final double DRIVE_KD = 9.33; // derivative gain for driving each pod
	public static final double DRIVE_KF = 0.130654611; // feed forward gain for driving each pod
	public static final int DRIVE_IZONE = 200; // integral zone for driving each pod
	public static double fps2ups = 12.0 /(constants.WHEELDIAMETER * Math.PI) * 4096.0/10.0 *48.0/30.0; // converts fps to ups
	public static final int DRIVE_ALLOWABLE_ERROR = 50;

	public static final double NEO_KP = 0.000095;
	public static final double NEO_KI = 0.0000009325;
	public static final double NEO_KD = 0;
	public static final double NEO_FF = 0;
	public static final double NEO_IZ = 0;
	public static final double NEO_MAX_ACCEL = 5;
	public static final double DRIVE_GEAR_RATI0 = 17/54;
	public static double fps2rpm = 12.0 * (1.0/(WHEELDIAMETER*Math.PI)) * (48.0/30.0) * (54.0/17.0) * (60.0);
	public static double rev2ft = (17.0/54.0)*(30.0/48.0)*(WHEELDIAMETER*Math.PI)*(1.0/12.0);
	public static final double NEO_MAX_VEL = 6000;
	
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
	
	public static final int DEPLOYED_HEIGHT = 40000; //encoder ticks
    public static final int STOWED_HEIGHT = 0;
	public static final int ROCKET_HEIGHT = 14500;
}