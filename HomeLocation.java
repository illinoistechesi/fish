import java.util.*;

public class HomeLocation extends Location {

    public HomeLocation(){
        
    }

    public HomeLocation(String name){
        super(name);
    }
    
    public HomeLocation(double lat, double lng, String name){
        super(lat, lng, name);
    }
    
    @Override
    public void doInteractions(List<Person> people){
        HashMap<Person.State, List<Person>> map = Person.getPeopleByState(people);
        for(Person personInf : map.get(Person.State.INFECTED)){
            List<Person> susceptibles = map.get(Person.State.SUSCEPTIBLE);
            Iterator<Person> iter = susceptibles.iterator();
            while(iter.hasNext()){
                Person personSus = iter.next();
                double inf = personInf.getInfectivity();
                double sus = personSus.getSusceptibility();
                if(inf > sus){
                    personSus.doInfect(personInf.getDisease());
                    iter.remove();
                }
            }
        }
    }

}