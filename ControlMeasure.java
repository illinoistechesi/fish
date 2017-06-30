package fish;

abstract public class ControlMeasure {
    
    String name = "Unnamed Control Measure";
    int[] range = {0, 0};
    
    public ControlMeasure(){
        
    }
    
    abstract public Location applyMeasure(City city, Person person, Location nextLoc);
    
    public int[] getTimeRange(){
        return this.range;
    }
}