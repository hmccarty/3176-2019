package frc.robot;

import java.lang.Math;

/**
 * Defined characterics of the physical robot. 
 * This includes variables like port numbers, closed loop gains, etc. 
 */
public class constants {
	private static boolean isCompBot = false;

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
	public static final int CARRIAGE_BOTTOM_BUMP_SWITCH = 5;
	public static final int STAGE_BOTTOM_BUMP_SWITCH = 7;

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

	//Absolute offsets of pod mag encoders, see online documentation for determining these values. 
	public static double OFFSETS_COMPETITION[] = {2818, 2459, 2483, 2995};
	public static double OFFSETS_PRACTICE[] = {1857,3780,1180,1230};
	public static double OFFSETS[] = (isCompBot) ? OFFSETS_COMPETITION : OFFSETS_PRACTICE;

	public static double DRIVETRAIN_LENGTH = 30.5; // inches
	public static double DRIVETRAIN_WIDTH = 29.5; // inches
	public static final double WHEEL_DIAMETER = 3.25; // inches
	public static double DRIVETRAIN_MAX_WHEEL_SPEED = 13.5; // ft/s
	public static double DRIVETRAIN_MAX_ROTATION_SPEED = 5.0; // radians/s
	public static double ENCODER_UNITS = 4096.0; // encoder units
	
	/***********************\
	|* Swervepod Constants *|
	\***********************/

	public static final double[][] SPIN_PID_COMPETITION = { /*kP*/ {4.5, 1.0, 1.0, 4.5}, 
															/*kI*/ {0.0023, 0.00000, 0.00000, 0.0023},
															/*kD*/ {190.0, 400.0, 400.0, 190.0},
															/*kF*/ {0.00001, 0.00001, 0.00001, 0.00001}};

	public static final double[][] SPIN_PID_PRACTICE = { /*kP*/ {2, 2, 2, 4.5}, 
														 /*kI*/ {0.0023, 0.00001, 0.00001, 0.0023},
														 /*kD*/ {190.0, 170, 170, 190.0},
														 /*kF*/ {0.00000, 0.00000, 0.00000, 0.00000}};

	public static final double[][] SPIN_PID_CONFIG = (isCompBot) ? SPIN_PID_COMPETITION : SPIN_PID_PRACTICE;

	public static final int SWERVE_ALLOWABLE_ERROR = 5; 

	public static final double[] DRIVE_PID_COMPETITION = { /*kP*/ 0.000095, 
														   /*kI*/ 0.0000009325,
														   /*kD*/ 0.0,
														   /*kF*/ 0.0,
														   /*I-Zone*/ 0.0};

	public static final double[] DRIVE_PID_PRACTICE = { /*kP*/ 0.000095, 
														/*kI*/ 0.0000009325,
														/*kD*/ 0.0,
														/*kF*/ 0.0,
														/*I-Zone*/ 0.0};

	public static final double[] DRIVE_PID_CONFIG = (isCompBot) ? DRIVE_PID_COMPETITION : DRIVE_PID_PRACTICE;

	public static final int DRIVE_ALLOWABLE_ERROR = 50;

	public static final double DRIVE_MAX_VEL = 6000;
	public static final double DRIVE_MAX_ACCEL = 5;

	public static final double DRIVE_GEAR_RATI0 = 17.0/54.0;
	public static double FPS_TO_RPM = 12.0 * (1.0/(WHEEL_DIAMETER*Math.PI)) * (48.0/30.0) * (54.0/17.0) * (60.0);
	public static double REV_TO_FT = (17.0/54.0)*(30.0/48.0)*(WHEEL_DIAMETER*Math.PI)*(1.0/12.0);
	
	public static final double MAX_SLOW_PERCENT_SPEED = 0.6;
	public static final double DRIVE_RAMP_RATE = 0.2;
	public static final int DRIVE_CURRENT_LIMIT = 40; 
	public static final double DRIVE_GEAR_REDUCTION = 30.0/48.0;
	public static final double DRIVE_MAX_ENCODER_SPEED = 10000.0;

	/**********************\
	|* Elevator Constants *|
	\**********************/

	public static final double[] ELEVATOR_POSITION_PID_COMPETITION = { /*kP*/ 0.08, /*kI*/ 0.0, /*kD*/ 0.0, /*kF*/ 0.002,
														   	  /*I-Zone*/ 0.0, 
														      /*Output Range Min*/ -0.1, /*Output Range Max*/ 1.0};

	public static final double[] ELEVATOR_POSITION_PID_PRACTICE = { /*kP*/ 0.07, /*kI*/ 0.0, /*kD*/ 0.0, /*kF*/ 0.002,
														   /*I-Zone*/ 0.0, 
														   /*Output Range Min*/ -0.1, /*Output Range Max*/ 1.0};

	public static final double[] ELEVATOR_POSITION_PID_CONFIG = (isCompBot) ? ELEVATOR_POSITION_PID_COMPETITION : ELEVATOR_POSITION_PID_PRACTICE;

	public static final double[] ELEVATOR_SPEED_PID_COMPETITION = { /*kP*/ 0.007, /*kI*/ 0.0, /*kD*/ 0.0, /*kF*/ 0.002, 
																	/*Min Speed*/ -0.1, /*Max Speed*/ 0.8, 
																 	/*I-Zone*/ 0.0};

	public static final double[] ELEVATOR_SPEED_PID_PRACTICE = { /*kP*/ 0.007, /*kI*/ 0.0, /*kD*/ 0.0, /*kF*/ 0.002, 
																 /*Min Speed*/ -0.1, /*Max Speed*/ 0.8, 
																 /*I-Zone*/ 0.0};

	public static final double[] ELEVATOR_SPEED_PID_CONFIG = (isCompBot) ? ELEVATOR_SPEED_PID_COMPETITION : ELEVATOR_SPEED_PID_PRACTICE;

	public static final double[] ELEVATOR_MOTION_CONFIG = { /*Max Velocity*/ 0.0, /*Min Velocity*/ 0.0, 
															/*Max Acceleration*/ 0.0, 
															/*Allowed Error*/ 0.0 };
	public static final int SMART_CURRENT_LIMIT = 75;

	public static final double  RAMP_RATE = 0.3; 

	/**************************\
	|* Cargo Intake Constants *|
	\**************************/
	
	public static final double[] CARGO_INTAKE_PID_CONFIG = { /*kP*/ 0.00015, /*kI*/ 0.0, /*kD*/ 0.0, /*kF*/ 0.000, 
															 /*Min Speed*/ -0.3, /*Max Speed*/ 0.7, 
															 /*I-Zone*/ 0.0};

	public static final int DEPLOYED_HEIGHT = 48000; //encoder ticks
    public static final int STOWED_HEIGHT = 4500;
	public static final int ROCKET_HEIGHT = 14500;
	public static final int TRANSFER_HEIGHT = 0;
}