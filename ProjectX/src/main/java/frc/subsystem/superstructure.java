package frc.subsystem; 

import java.util.ArrayList;
import frc.subsystem.cargointake;

public class superstructure {
    private ArrayList<commands> commandQueue; 
    private static superstructure instance = new superstructure();
    private boolean finished = false; 

    controller c = controller.getInstance();
    cargointake cintake = cargointake.getInstance();

    public superstructure (){
        commandQueue = new ArrayList<commands>();
    }

    public static superstructure getInstance(){
        return instance; 
    }

    public enum commands {
        INTAKE_CARGO,
        INTAKE_HATCH_CB,
        INTAKE_HATCH_G,
        TRANSFER_CARGO,
        TRANSFER_HATCH,
        FLOOR_CHANGE,
        DELIVER_CARGO,
        DELIVER_HATCH,
        OPEN_LOOP_HATCH,
        OPEN_LOOP_ELEVATOR,
        OPENLOOP_CARGO,
        VISION_TRACK,
        NEUTRAL
    }

    public void startUp(){}

    public void run(){}

    public void end(){}

    public void addCommand(commands command){}

    public void enactCommand(){
        if(!commandQueue.isEmpty()){
            switch(commandQueue.get(0)){
                case INTAKE_CARGO:
                    if(cintake.deployIntake()){
                        if(c.getCargoIntake()){
                            cintake.runIntake();
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
                case TRANSFER_HATCH:
                case FLOOR_CHANGE:
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
        else{
            commandQueue.add(commands.NEUTRAL);
        }
    }


}