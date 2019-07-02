
package frc.auton;

import frc.auton.autonmanager;
import frc.util.loop;

public class auto {
	private loop autoLoop;
	private autonmanager manager;
	public static String gameData;
	private  boolean firstTime = true;
	public auto() {}
	/**
	 * registers the loop given to be run with the {@link #run()}
	 * @param loop Loop to be registered
	 */
	public void registerLoop(loop loop) {
		autoLoop = loop;
	}
	/**
	 * registers a manager to run commands
	 * @param manager Manager already with the commands registered to it
	 */
	public void registerManager(autonmanager manager) {
		this.manager = manager;
	}
	/**
	 * @param gD Game Data From FMS
	 */
	public static void setGameData(String gD) {
		gameData = gD;
	}
	/**
	 * @return the game Data from FMS example "LRL"
	 */
	public static String getGameData() {
		return gameData;
	}
	/**
	 * if there is a manager is runs the {@link AutoManager#run()} or runs a registered auto loop
	 */
	public void run() {
		if(autoLoop == null) {
			manager.run();
		}
		else {
			if(firstTime) {
				autoLoop.onStart();
				firstTime = false;
			}
			else {
				autoLoop.onLoop();
			}
		}
		
	}	
}


