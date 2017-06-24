import java.util.*;

/*
 * F.I.S.H.
 * The Fictional Infection Simulation Host
 */
public class Main {
    
    public static int TURNS = 20;

    public static void main(String[] args){
        
        City city = new City("Scise City");
        
        for(int x = 0; x < 5; x++){
            for(int y = 0; y < 5; y++){
                GridLocation home = new GridLocation("G" + x + "/" + y, x, y);
                for(int p = 0; p < 4; p++){
                    Routine routine = new NormalRoutine();
                    Person person = new Person(routine, home);
                    city.addPerson(person);
                }
                city.addLocation(home);
            }
        }
        
        List<Person> people = city.getPeople();
        int randomInt = Main.getRandom().nextInt(people.size());
        people.get(randomInt).setState(Person.State.INFECTED);
        
        Main.printCitySummary(city);
        //Main.printLocationDetails(city);
        Main.printCityLine(city);
        
        for(int i = 1; i < TURNS; i++){
            city.doTurn();
            //Main.printLocationDetails(city);
            Main.printCityLine(city);
        }
        
        //Main.printCitySummary(city);
        
    }
    
    public static void printCitySummary(City city){
        List<Person> people = city.getPeople();
        int locations = city.getLocations().size();
        System.out.println(city.getName() + " at Time: " + city.getTime());
        System.out.println(locations + " locations");
        System.out.println(people.size() + " people");
        HashMap<Person.State, List<Person>> stateMap = Person.getPeopleByState(people);
        for(Person.State state : Person.State.values()){
            String out = state.getName() + ": " + stateMap.get(state).size();
            System.out.println(out);
        }
    }
    
    public static void printCityLine(City city){
        String out = "" + city.getTime();
        List<Person> people = city.getPeople();
        HashMap<Person.State, List<Person>> stateMap = Person.getPeopleByState(people);
        for(Person.State state : Person.State.values()){
            out += "," + stateMap.get(state).size();
        }
        System.out.println(out);
    }

    public static void printLocationDetails(City city){
        HashMap<Location, List<Person>> map = city.getPeopleByLocation();
        System.out.println(city.getName() + " at Time: " + city.getTime());
        for(Map.Entry<Location, List<Person>> entry : map.entrySet()){
            Location lc = entry.getKey();
            System.out.println(lc.getName() + ": " + entry.getValue().size());
        }
    }
    
    private static Random random = new Random();
    public static Random getRandom(){
        return random;
    }

}