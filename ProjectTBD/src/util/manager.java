package util;

import java.util.ArrayList;

public class manager {
	private static manager instance = new manager(); 
	private ArrayList<loop> subsystemList;
	
	public manager() {
		subsystemList = new ArrayList();
	}
	
	public static manager getInstance() {
		return instance; 
	}
	
	public void addLoop(loop subsystem) {
		subsystemList.add(subsystem);
	}
	
	public void runStart() {
		for(loop subsystem : subsystemList) {
			subsystem.start();
		}
	}
	
	public void runStop() {
		for(loop subsystem : subsystemList) {
			subsystem.stop();
		}
	}
	
	public void runLoop() {
		for(loop subsystem : subsystemList) {
			subsystem.loop();
		}
	}
}
