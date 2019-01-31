package frc.subsystems; 

import frc.util.loop; 

public class loopmanager {
    private loopmanager instance = new loopmanager();
    private ArrayList<loop> subsystems = new ArrayList<loop>();

    public loopmanager() {}

    public loopmanager getInstance(){
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