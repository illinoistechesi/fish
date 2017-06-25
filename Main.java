import java.util.*;

/*
 * F.I.S.H.
 * The Fictional Infection Simulation Host
 */
public class Main {
    
    public static int TURNS = 30;
    public static int MAX_TURNS = 500;
    public static int GRID_SIZE = 5;
    public static int FAMILY_SIZE = 4;

    public static String FILE_SIR = "sir.txt";
    public static String FILE_GEO = "geo.txt";

    public static void main(String[] args){
        
        City city = new City("Scise City");
        
        List<double[]> coords = Helper.readCoordsFromFile("coords.txt");
        
        for(int lidx = 0; lidx < coords.size(); lidx++){
            double[] pair = coords.get(lidx);
            String id = lidx + "";
            String locName = "Location " + lidx;
            Location home = new HomeLocation(id, pair[0], pair[1], locName);
            for(int p = 0; p < FAMILY_SIZE; p++){
                Routine routine = new NormalRoutine();
                Person person = new Person(routine, home);
                city.addPerson(person);
            }
            city.addLocation(home);
        }
        
        List<Person> people = city.getPeople();
        int randomInt = Helper.getRandom().nextInt(people.size());
        Disease disease = new DeltaDisease();
        people.get(randomInt).doInfect(disease);
        
        Main.printCitySummary(city);
        Helper.printCityLine(FILE_SIR, city);
        Helper.printLocationLine(FILE_GEO, city);
        
        boolean outbreak = true;
        while(outbreak){
            city.doTurn();
            Helper.printCityLine(FILE_SIR, city);
            Helper.printLocationLine(FILE_GEO, city);
            int totalInfected = Person.groupPeopleByState(city.getPeople()).get(Person.State.INFECTED).size();
            if(totalInfected == 0){
                outbreak = false;
            }
            if(city.getTime() > MAX_TURNS){
                outbreak = false;
            }
        }
        
        System.out.println("Outbreak Length: " + city.getTime());
        
        Helper.closeAllFiles();
        
    }
    
    public static void printCitySummary(City city){
        List<Person> people = city.getPeople();
        int locations = city.getLocations().size();
        System.out.println(city.getName() + " at Time: " + city.getTime());
        System.out.println(locations + " locations");
        System.out.println(people.size() + " people");
        HashMap<Person.State, List<Person>> stateMap = Person.groupPeopleByState(people);
        for(Person.State state : Person.State.values()){
            String out = state.getName() + ": " + stateMap.get(state).size();
            System.out.println(out);
        }
    }

}