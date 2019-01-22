package Subsystem;

import java.util.ArrayList;

/**
 * Manages onStart and onLoop of each subsystem.
 * @author Jonathan Heidegger
 */

public class Loop_Manager {
	
	private static Loop_Manager instance = new Loop_Manager();
	private ArrayList<Loop> Loops;

	//private constructor
	private Loop_Manager()
	{
		Loops = new ArrayList<Loop>();
	}
	
	// So there is only ever one instance of the loop manager
	public static Loop_Manager getInstance()
	{
		return instance;
	}
	
	public void addLoop(Loop L)
	{
		Loops.add(L);
	}
	
	public void startLoops()
	{
		for(Loop L:Loops)
		{
			L.onStart();
		}
	}
	
	public void runLoops()
	{
		for(Loop L:Loops)
		{
			L.onloop();
		}
	}
	
	public void endLoops()
	{
		for(Loop L:Loops)
		{
			L.stop();
		}
	}
	

}
