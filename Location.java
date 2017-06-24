import java.util.*;

abstract public class Location {
    
    private String name = "Unnamed Location";

    public Location(){
        
    }

    public Location(String name){
        this.name = name;
    }
    
    public String getName(){
        return this.name;
    }
    
    abstract void doInteractions(List<Person> people);

}