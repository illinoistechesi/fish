import java.util.*;

public class Location {
    
    private double lat;
    private double lng;
    private String name = "Unnamed Location";
    
    public Location(){
        
    }
    
    public Location(String name){
        this.name = name;
    }

    public Location(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }
    
    public Location(double lat, double lng, String name){
        this.lat = lat;
        this.lng = lng;
        this.name = name;
    }
    
    public Location(String name, int x, int y){
        this.name = name;
    }
    
    public void doInteractions(List<Person> people){
        
    }
    
    public static double getDistance(Location l1, Location l2){
        return 0.0;   
    }
    
    public double getLat(){
        return this.lat;
    }
    
    public double getLng(){
        return this.lng;
    }
    
    public String getName(){
        return this.name;
    }

}