package Util;




import java.util.ArrayList;

import Auton.Waypoint;




public class Trajectory1D {
	private double kMaxVelocity = 4.0;
	private double kMaxAcceleration=4.0;
	private double timeStep = .1;
	private double simTime = 0.0;
	private double timeToComplete;
	private boolean normaldirection = true;
	private ArrayList<Waypoint> points;
	private ArrayList<Double> speed;
	private ArrayList<Double> position;
	public Trajectory1D() {
		points = new ArrayList<Waypoint>();
		speed = new ArrayList<Double>();
		position = new ArrayList<Double>();
	}
	public Trajectory1D(double MaxVel, double MaxAccel) {
		points = new ArrayList<Waypoint>();
		speed = new ArrayList<Double>();
		position = new ArrayList<Double>();
		kMaxVelocity = MaxVel;
		kMaxAcceleration = MaxAccel;
	}
	public void addWaypoint(Waypoint w)
	{
		points.add(w);
	}
	public void calculateTrajectory()
	{
		double currSpeed = 0.0;
		double currX = points.get(0).getX();
		double endSpeed = points.get(1).getSpeed();
		int waypointIdx = 1;
		boolean isSlowing = false;
		if(currX-points.get(waypointIdx).getX() < 0)
		{
			normaldirection = true;
		}
		else
		{
			normaldirection = false;
		}
		while(simTime < 10.0)
		{
			double stoppingDistance = (Math.pow(endSpeed, 2)-Math.pow(currSpeed,2))/(-2.0*kMaxAcceleration);
			double distanceToWaypoint = currX-points.get(waypointIdx).getX();
			if(stoppingDistance>Math.abs(distanceToWaypoint))
			{
				isSlowing = true;
			}
			if(Math.abs(currSpeed) <= kMaxVelocity && !isSlowing)
			{
				if((kMaxVelocity - Math.abs(currSpeed)) < kMaxAcceleration*timeStep)
				{
					if(normaldirection)
					{
						currSpeed = kMaxVelocity;
					}
					else
					{
						currSpeed = -kMaxVelocity;
					}
							
				}
				else
				{
					if(normaldirection)
					{
						currSpeed = currSpeed + kMaxAcceleration * timeStep;
					}
					else
					{
						currSpeed = currSpeed - kMaxAcceleration * timeStep;
					}
				}
			}
			else
			{
				if(normaldirection)
				{
					currSpeed = currSpeed - kMaxAcceleration * timeStep;
				}
				else
				{
					currSpeed = currSpeed + kMaxAcceleration * timeStep;
				}
				if((normaldirection && currSpeed<endSpeed)||(!normaldirection && currSpeed>endSpeed))
				{
					currSpeed = endSpeed;
					if(waypointIdx < points.size()-1)
					{
						waypointIdx++;
						isSlowing = false;
						endSpeed = points.get(waypointIdx).getSpeed();
					}
				}		
			}
			speed.add(currSpeed);
			//System.out.println("waypointIdx" + waypointIdx);
			currX += currSpeed * timeStep;
			position.add(currX);
			//System.out.println("X: " + currX);
			//System.out.println(currY);
			//System.out.println("stop: " + stoppingDistance);
			//System.out.println("dtw " + distanceToWaypoint);
			//System.out.println(currSpeed);
			//System.out.println("time: " + simTime+ " x: "+  currX  + " y: " +currY + " sp: " + currSpeed +" angle " + (currAngle * 180.0/Math.PI) +  " currway " + waypointIdx );
			System.out.println( currX  + ":"  + currSpeed +":" + stoppingDistance + ":" + distanceToWaypoint);
			simTime+= timeStep;
		}
		int idx = 1;
		while(speed.get(idx) != 0.0)
		{
			idx++;
		}
		timeToComplete = idx * timeStep;
	}
	public double getSpeed(double Time)
	{
		if(Time/timeStep < speed.size())
		{
			return speed.get((int)(Time/timeStep));
		}
		else
		{
			return 0.0;
		}
	}
	public double getPosition(double Time)
	{
		if(Time/timeStep < speed.size())
		{
			return position.get((int)(Time/timeStep));
		}
		else
		{
			return 0.0;
		}
	}
	public double getTimeToComplete()
	{
		return timeToComplete;
	}
	public void print()
	{
	    for (int i = 0; i<speed.size(); i++)
	    {
	    	System.out.println("index: "+ i + " speed: " + speed.get(i) + " time: " + i*timeStep);
	    }
	}
	public void setWantedPos() {
		
	}
}


