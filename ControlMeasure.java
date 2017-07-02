package fish;
import java.util.*;

abstract public class ControlMeasure {
    
    String name = "Unnamed Control Measure";
    int[] range = {0, 0};
    
    public ControlMeasure(){
        
    }
    
    public ControlMeasure(String name){
        this.name = name;
    }
    
    public ControlMeasure(String name, int start, int end){
        this.name = name;
        this.range[0] = start;
        this.range[1] = end;
    }
    
    public ControlMeasure(int start, int end){
        this.range[0] = start;
        this.range[1] = end;
    }
    
    abstract public Location applyMeasure(City city, Person person, Location nextLoc);
    
    private static Map<Person, Integer> affected = new HashMap<Person, Integer>();
    private static double cost = 0;
    
    protected static void addAffectedPerson(Person person, int time){
        if(!affected.containsKey(person)){
            affected.put(person, time);
        }
    }
    
    protected static void addCost(double addedCost){
        cost += addedCost;
    }
    
    public static int getTotalAffected(){
        return affected.size();
    }
    
    public static double getTotalCost(){
        return cost;
    }
    
    public int[] getTimeRange(){
        return this.range;
    }
    
    public void setStartHour(int hour){
        this.range[0] = hour;
    }
    
    public void setEndHour(int hour){
        this.range[1] = hour;
    }
    
    public void setStartDay(int day){
        this.range[0] = (day * 24);
    }
    
    public void setEndDay(int day){
        this.range[1] = (day * 24);
    }
    
}