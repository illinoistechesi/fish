import java.util.*;

public class Location {
    
    protected double lat;
    protected double lng;
    protected String name = "Unnamed Location";
    
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
    
    private static double EARTH_RADIUS = 3961.0; // in miles
    public static double getDistance(Location l1, Location l2){
        double dLng = Math.toRadians(l2.getLng() - l1.getLng());
        double dLat = Math.toRadians(l2.getLat() - l1.getLat());
        double a11 = Math.pow(Math.sin(dLat/2), 2);
        double a21 = Math.cos(Math.toRadians(l1.getLat()));
        double a22 = Math.cos(Math.toRadians(l2.getLat()));
        double a23 = Math.pow(Math.sin(dLng/2), 2);
        double a = a11 + a21 * a22 * a23;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = EARTH_RADIUS * c;
        return d;
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