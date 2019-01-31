package frc.subsystem; 

import java.util.ArrayList;
import frc.subsystem.cargointake;

public class superstructure {
    private commands currentCommand;
    private commands wantedCommand;  
    private static superstructure instance = new superstructure();
    private boolean finished = false; 

    controller c = controller.getInstance();
    cargointake cintake = cargointake.getInstance();
    loopmanager loopMan = loopmanager.getInstance();

    public superstructure (){}

    public static superstructure getInstance(){
        return instance; 
    }

    public enum commands {
        INTAKE_CARGO,
        INTAKE_HATCH_CB,
        INTAKE_HATCH_G,
        TRANSFER_CARGO,
        TRANSFER_HATCH,
        DELIVER_CARGO,
        DELIVER_HATCH,
        OPEN_LOOP_HATCH,
        OPENLOOP_CARGO,
        VISION_TRACK,
        HOLDING
    }

    public void startUp(){}

    public void run(){}

    public void end(){}

    public void setCommand(commands wantedCommand){ this.wantedCommand = wantedCommand; }

    public void registerLoop(){

    }

    public void enactCommand(){
        switch(commandQueue.get(0)){
            case INTAKE_CARGO:
                if(cintake.deployIntake()){
                    if(c.getCargoIntake()){
                        cintake.runIntake(-.5);
                    }
                    else{
                        if(cintake.stowIntake()){
                            finished = true; 
                        }
                    }
                }
            case INTAKE_HATCH_CB:
            case INTAKE_HATCH_G:
            case TRANSFER_CARGO:
                if(cintake.stowIntake()){

                }
            case TRANSFER_HATCH:
            case DELIVER_CARGO:
            case DELIVER_HATCH:
            case OPEN_LOOP_HATCH:
            case OPEN_LOOP_ELEVATOR:
            case OPENLOOP_CARGO:
            case NEUTRAL:
                finished = true; 
        }
        if(finished){
            commandQueue.remove(0);
            finished = false; 
        }
    }


}