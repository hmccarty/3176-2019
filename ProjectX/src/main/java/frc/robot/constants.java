package frc.robot;

import java.lang.Math;

public class constants {
	private static boolean isCompBot = false;

	public static void whichRobot(boolean setBot){
		isCompBot = setBot;
	}

	/***********\
	|* CAN IDs *|
	\***********/	
												
	public static int DRIVE_ONE = 1;			     
	public static int DRIVE_TWO = 2;    		     
	public static int DRIVE_THREE = 3;			     
	public static int DRIVE_FOUR = 4;			     

	public static int STEER_ONE = 11;			     
	public static int STEER_TWO = 22;			     
	public static int STEER_THREE = 33;			     
	public static int STEER_FOUR = 44;			     

	public static int ELEVATOR_LEFT = 5;		     
	public static int ELEVATOR_RIGHT = 6; 			 

	/*************\
	|* PWM Ports *|
	\*************/	

	public static int CARGO_INTAKE_ROLLER = 0;       
	public static int CARGO_INTAKE_ACTUATOR = 1;     
	public static int HATCH_INTAKE_ROLLER = 2;        

	/*************\
	|* PCM Ports *|
	\*************/	
	
	public static int CLAW_PINCHER_FRONT = 7; 
	public static int CLAW_PINCHER_BACK = 0;
	public static int CROSSBOW_OUTER_FRONT = 1; 
	public static int CROSSBOW_OUTER_BACK = 6; 
	public static int CROSSBOW_INNER_FRONT = 2; 
	public static int CROSSBOW_INNER_BACK = 5; 
	public static int CLAW_EXTENDER_FRONT = 4; 
	public static int CLAW_EXTENDER_BACK = 3; 

	/*************\
	|* DIO Ports *|
	\*************/

	public static int CARGO_INTAKE_ENCODER[] = {0,1}; 
	public static int CARGO_IN_ROLLER = 2;  
	public static int CARGO_INTAKE_STOWED = 3;  
	public static int CARGO_INTAKE_DOWN = 4;
	public static int HATCH_IN_INTAKE = 5; 

	/************************\
	|* Controller Constants *|
	\************************/

	public static int DRIVE_JOYSTICK = 0; 
	public static int SPIN_JOYSTICK = 1;
	public static int OPERATOR = 2;

	public static double TRANSLATIONAL_DEADBAND = 0.03; 
	public static double TRANSLATIONAL_SCALE = 0.55; 
	public static double TRANSLATIONAL_OFFSET = 1.0 / (TRANSLATIONAL_SCALE * Math.pow((1.0 - TRANSLATIONAL_DEADBAND), 3) + (1.0 - TRANSLATIONAL_SCALE) * (1.0 - TRANSLATIONAL_SCALE));
	
	public static double ROTATIONAL_DEADBAND = 0.03; 
	public static double ROTATIONAL_SCALE = 0.5; 
	public static double ROTATIONAL_OFFSET = 1.0 / (TRANSLATIONAL_SCALE * Math.pow((1.0 - TRANSLATIONAL_DEADBAND), 3) + (1.0 - TRANSLATIONAL_SCALE) * (1.0 - TRANSLATIONAL_SCALE)); 
	
	/************************\
	|* Drivetrain Constants *|
	\************************/
	//public static double OFFSETS[] = {2802,2426,2599,2997};
	public static double OFFSETS[] = {1857,3780,1180,1230}; //Gears Facing Right
	public static double DRIVETRAIN_LENGTH = 30.5; // inches
	public static double DRIVETRAIN_WIDTH = 29.5; // inches
	public static final double WHEEL_DIAMETER = 3.25;
	public static double DRIVETRAIN_MAX_WHEEL_SPEED = 13.5; // ft/s
	public static double DRIVETRAIN_MAX_ROTATION_SPEED = 5.0; // radians/s
	public static double ENCODER_UNITS = 4096.0; // encoder units
	
	/***********************\
	|* Swervepod Constants *|
	\***********************/
											//{4.5, 2, 2, 4.5};
	public static final double SWERVE_KP[] = {2, 2, 2, 4.5}; // proportional gain for turning each pod
	public static final double SWERVE_KI[] = {0.0023, 0.00001, 0.00001, 0.0023}; // integral gain for turning each pod
	public static final double SWERVE_KD[] = {190.0, 170, 170, 190.0}; // derivative gain for turning each pod
	public static final double SWERVE_KF = 0.00001;
	public static final int SWERVE_ALLOWABLE_ERROR = 5; 

	public static final double DRIVE_KP = .4; // proportional gain for driving each pod
	public static final double DRIVE_KI = 0.0001; // integral gain for driving each pod
	public static final double DRIVE_KD = 9.33; // derivative gain for driving each pod
	public static final double DRIVE_KF = 0.130654611; // feed forward gain for driving each pod
	public static final int DRIVE_IZONE = 200; // integral zone for driving each pod
	public static double FPS_TO_UPS = 12.0 /(constants.WHEEL_DIAMETER * Math.PI) * 4096.0/10.0 *48.0/30.0; // converts fps to ups
	public static final int DRIVE_ALLOWABLE_ERROR = 50;

	public static final double NEO_KP = 0.000095;
	public static final double NEO_KI = 0.0000009325;
	public static final double NEO_KD = 0;
	public static final double NEO_FF = 0;
	public static final double NEO_IZ = 0;
	public static final double NEO_MAX_ACCEL = 5;
	public static final double DRIVE_GEAR_RATI0 = 17.0/54.0;
	public static double FPS_TO_RPM= 12.0 * (1.0/(WHEEL_DIAMETER*Math.PI)) * (48.0/30.0) * (54.0/17.0) * (60.0);
	public static double REV_TO_FT = (17.0/54.0)*(30.0/48.0)*(WHEEL_DIAMETER*Math.PI)*(1.0/12.0);
	public static final double NEO_MAX_VEL = 6000;
	
	public static final double MAX_SLOW_PERCENT_SPEED = .6;
	public static final double DRIVE_RAMP_RATE = .3;
	public static final double DRIVE_GEAR_REDUCTION = 30.0/48.0;
	public static final double DRIVE_MAX_ENCODER_SPEED = 10000.0;

	/**********************\
	|* Elevator Constants *|
	\**********************/

	public static final double[] ELEVATOR_PID_COMPETITION = { /*kP*/ 0.01, /*kI*/ 0.0, /*kD*/ 0.0, 
														   /*I-Zone*/ 0.0, 
														   /*Output Range Min*/ 0.0, /*Output Range Max*/ 0.0, 
														   /*kF*/ 0.0};

	public static final double[] ELEVATOR_PID_PRACTICE = { /*kP*/ 0.07, /*kI*/ 0.0, /*kD*/ 0.0, 
														   /*I-Zone*/ 0.0, 
														   /*Output Range Min*/ -0.1, /*Output Range Max*/ 1.0, 
														   /*kF*/ 0.002};

	public static final double[] ELEVATOR_PID_CONFIG = (isCompBot) ? ELEVATOR_PID_COMPETITION : ELEVATOR_PID_PRACTICE;

	public static final double[] ELEVATOR_MOTION_CONFIG = { /*Max Velocity*/ 0.0, /*Min Velocity*/ 0.0, 
															/*Max Acceleration*/ 0.0, 
															/*Allowed Error*/ 0.0 };
	public static final int SMART_CURRENT_LIMIT = 75;
	public static final int LEFT_BUMP_SWITCH = 6;
	public static final int RIGHT_BUMP_SWITCH = 5;

	/**************************\
	|* Cargo Intake Constants *|
	\**************************/
	
	public static final int DEPLOYED_HEIGHT = 48000;//48000; //encoder ticks
    public static final int STOWED_HEIGHT = 3000;
	public static final int ROCKET_HEIGHT = 14500;
	public static final int TRANSFER_HEIGHT = 0;
}