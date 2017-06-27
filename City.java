package fish;
import java.util.*;

public class City {
    
    private int time = 0;
    private String name;
    private List<Location> locations;
    private List<Person> people;
    
    public City(String name){
        this.name = name;
        this.locations = new ArrayList<Location>();
        this.people = new ArrayList<Person>();
    }
    
    public void doTurn(){
        for(Person p : this.getPeople()){
            p.doTurn(this);
        }
        HashMap<Location, List<Person>> map = Person.groupPeopleByLocation(this.getPeople());
        for(Map.Entry<Location, List<Person>> entry : map.entrySet()){
            Location loc = entry.getKey();
            List<Person> people = entry.getValue();
            loc.doInteractions(people);
        }
        this.time++;
    }
    
    public void addLocation(Location loc){
        this.locations.add(loc);
    }
    
    public List<Location> getLocations(){
        return this.locations;
    }
    
    public void addPerson(Person person){
        this.people.add(person);
    }
    
    public List<Person> getPeople(){
        return this.people;
    }
    
    public int getTime(){
        return this.time;
    }
    
    public int getDay(){
        return (int)(this.time / 24);
    }
    
    public int getHour(){
        return this.time % 24;
    }
    
    public String getName(){
        return this.name;
    }
}