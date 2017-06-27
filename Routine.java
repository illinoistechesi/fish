package fish;

abstract public class Routine {
    
    public Routine(){
        
    }
    
    abstract public Location getNextLocation(Person person, City city);
    
}