import java.util.*;

public class NormalRoutine extends Routine {
    
    private double limit;
    
    public NormalRoutine(double limit){
        this.limit = limit;
    }
    
    public Location getNextLocation(Person person, City city){
        Location current = person.getLocation();
        List<Location> locs = city.getLocations();
        List<Location> nearby = Location.getWithin(current, limit, locs);
        int rand = Helper.getRandom().nextInt(nearby.size());
        return nearby.get(rand);
    }
    
}