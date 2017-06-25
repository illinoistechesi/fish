import java.util.*;

public class NormalRoutine extends Routine {
    
    private double limit = 0.18;
    
    public NormalRoutine(){
        
    }
    
    public Location getNextLocation(Person person, City city){
        Location current = person.getLocation();
        List<Location> locs = city.getLocations();
        List<Location> nearby = Location.getWithin(current, limit, locs);
        int rand = Main.getRandom().nextInt(nearby.size());
        return nearby.get(rand);
        //GridLocation current = (GridLocation)person.getLocation();
        //List<Location> adj = GridLocation.getAdjacentLocations(locs, current);
        //return adj.get(0);
        //return person.getLocation();
    }
    
}