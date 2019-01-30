package frc.util; 

public abstract class subsystem {
    
    public subsystem() {}

    public abstract void zeroAllSensors();
    
    public abstract void outputToSmartDashboard();

    public abstract void registerLoop();
}