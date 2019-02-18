package Auton.Autos.Deprecated;
import Auton.PathFollower;
import Auton.Trajectory;
import Auton.Waypoint;
import Auton.Autos.Auto;
import Subsystem.Loop;

public class TrajectoryTest extends Auto {
		public static TrajectoryTest main = new TrajectoryTest();
		private static Trajectory t;
		private static PathFollower p;
		public TrajectoryTest() {
			super.registerLoop(new Loop()
			{

				@Override
				public void onStart() {
					t = new Trajectory();
					t.addWaypoint(new Waypoint(0.0, 0.0, 0.0));
					t.addWaypoint(new Waypoint(0.0, -20.0, Math.PI));
					t.addWaypoint(new Waypoint(0.0,20.0,0.0));
					//t.addWaypoint(new Waypoint(0.0, -2.0, 0.0));
					t.calculateTrajectory();
					p = new PathFollower(t);
					p.init();
					p.run();
				}

				@Override
				public void onloop() {
					p.run();
				}

				@Override
				public void stop() {
					// TODO Auto-generated method stub
					
				}
		
			});
		}
			

}

