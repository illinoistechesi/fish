import java.util.*;

public class NormalRoutine extends Routine {
    
    private double limit;
    
    public NormalRoutine(double limit){
        this.limit = limit;
    }
    
    public Location getNextLocation(Person person, City city){
        Location nextLoc = person.getLocation();
        if(person.getAgeGroup() == AgeGroup.ADULT){
            Location current = person.getLocation();
            List<Location> locs = city.getLocations();
            List<Location> nearby = Location.getWithin(current, limit, locs);
            int rand = (int)(Math.abs(Helper.nextSeed()) * nearby.size());
            //int rand = Helper.getRandom().nextInt(nearby.size());
            nextLoc = nearby.get(rand);
        }
        return nextLoc;
    }
    
}