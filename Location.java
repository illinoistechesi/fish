import java.util.*;

public class Location {
    
    protected double lat;
    protected double lng;
    protected String id;
    protected String name = "Unnamed Location";
    
    public Location(String id){
        this.id = id;
    }
    
    public Location(String id, String name){
        this.id = id;
        this.name = name;
    }

    public Location(String id, double lat, double lng){
        this.id = id;
        this.lat = lat;
        this.lng = lng;
    }
    
    public Location(String id, double lat, double lng, String name){
        this.id = id;
        this.lat = lat;
        this.lng = lng;
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
    
    public static List<Location> sortByDistanceFrom(Location ref, List<Location> locations){
        Collections.sort(locations, new DistanceComparator(ref));
        return locations;
    }
    
    public static List<Location> getWithin(Location ref, double limit, List<Location> locations){
        List<Location> res = new ArrayList<Location>();
        Collections.sort(locations, new DistanceComparator(ref));
        for(int i = 0; i < locations.size(); i++){
            Location loc = locations.get(i);
            double dist = Location.getDistance(ref, loc);
            if(dist <= limit){
                res.add(loc);
            }
            else{
                break;
            }
        }
        return res;
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
    
    public String getID(){
        return this.id;
    }
    
    @Override
    public String toString(){
        return this.getName();
    }

}