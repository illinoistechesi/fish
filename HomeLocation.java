package fish;
import java.util.*;

public class HomeLocation extends Location {

    public HomeLocation(String id){
        super(id);
    }

    public HomeLocation(String id, String name){
        super(id, name);
    }
    
    public HomeLocation(String id, double lat, double lng, String name){
        super(id, lat, lng, name);
    }
    
    @Override
    public void doInteractions(List<Person> people){
        List<Person> contagious = Person.getContagious(people);
        List<Person> vulnerable = Person.getVulnerable(people);
        for(Person personCon : contagious){
            Iterator<Person> iter = vulnerable.iterator();
            while(iter.hasNext()){
                Person personVul = iter.next();
                personVul.doExposure(personCon.getPathogen());
                if(personVul.getPathogen() != null){
                    iter.remove();
                }
            }
        }
    }

}