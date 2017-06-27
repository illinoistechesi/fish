package fish;
import java.util.*;

public class DistanceComparator implements Comparator<Location> {
    
    private Location ref;
    
    public DistanceComparator(Location ref){
        this.ref = ref;    
    }
    
    public int compare(Location l1, Location l2){
        double d1 = Location.getDistance(ref, l1);
        double d2 = Location.getDistance(ref, l2);
        if(d1 == d2){
            return 0;
        }
        else if(d1 > d2){
            return 1;
        }
        else{
            return -1;
        }
    }
    
}