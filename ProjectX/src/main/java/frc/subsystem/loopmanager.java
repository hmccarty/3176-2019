package frc.subsystem; 

import frc.util.loop; 
import java.util.ArrayList;

/**
 * Handles state of each subsystem, ensuring they are all either starting, looping, or stopping
 */
public class loopmanager {
    private static loopmanager instance = new loopmanager();
    private ArrayList<loop> subsystems = new ArrayList<loop>();
    

    public loopmanager() {}

    public static loopmanager getInstance(){
        return instance; 
    }

    public void addLoop(loop l){
        subsystems.add(l);
    }

    public void startLoops(){
        for(loop l : subsystems){
            l.onStart();
        }
    }

    public void runLoops(){
        for(loop l : subsystems){
            l.onLoop();
        }
    }

    public void stopLoops(){
        for(loop l : subsystems){
            l.onStop();
        }
    }
}