package Auton.Autos.Deprecated;


	import Auton.Autos.Auto;
import Subsystem.Drivetrain;
	import Subsystem.Drivetrain.driveCoords;
	import Subsystem.Drivetrain.driveType;
	import Subsystem.Intake;
	import Subsystem.Loop;
	import edu.wpi.first.wpilibj.Timer;
	import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class leftSwitch extends Auto {
		public static leftSwitch main = new leftSwitch();
		private static double driveTime = 2.0;
		private static double spinTime = 1.0;
		private static double startTime;
		private static Loop loop = new Loop()
				{
					@Override
					public void onStart() {
						startTime = Timer.getFPGATimestamp();
					}

					@Override
					public void onloop() {
						SmartDashboard.putNumber("timer", Timer.getFPGATimestamp());
						if(Timer.getFPGATimestamp()-startTime<driveTime)
						{
							
							SmartDashboard.putBoolean("in drive", true);
							Drivetrain.getInstance().swerve(-.3, 0.0,0.0, driveCoords.FIELDCENTRIC, driveType.PERCENTPOWER);
							
						}
						else if(Timer.getFPGATimestamp()-startTime<driveTime+spinTime)
						{
							Drivetrain.getInstance().swerve(0.0, 0.0,-.2, driveCoords.FIELDCENTRIC, driveType.PERCENTPOWER);
						}
						else if(gameData.substring(0,1).equals("L") && Timer.getFPGATimestamp()-startTime < driveTime +spinTime+2.0)
						{
							Intake.getInstance().setWantedState(Subsystem.Intake.systemStates.Scoring);
							Drivetrain.getInstance().swerve(0.0,0.0, 0.0, driveCoords.FIELDCENTRIC, driveType.PERCENTPOWER);
						}
						else
						{
							Drivetrain.getInstance().swerve(0.0,0.0, 0.0, driveCoords.FIELDCENTRIC, driveType.PERCENTPOWER);
							Intake.getInstance().setWantedState(Subsystem.Intake.systemStates.Neutral);
						}
						
					}

					@Override
					public void stop() {
						// TODO Auto-generated method stub
						
					}
			
				};
		public leftSwitch() {
			super.registerLoop(loop);
		}
		

}