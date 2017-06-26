import java.util.*;

/*
 * F.I.S.H.
 * The Fictional Infection Simulation Host
 */
public class Main {
    
    public static int MAX_TURNS = 500;
    public static int FAMILY_SIZE = 4;

    public static String FILE_COORDS = "coords.txt";
    public static String FILE_SIR = "sir.txt";
    public static String FILE_GEO = "geo.txt";
    public static String FILE_PEOPLE = "people.txt";

    public static void main(String[] args){
        
        /*
         * Populate city with locations, people, and routines
         */
        City city = new City("Scise City");
        List<String[]> coords = Helper.readCoordsFromFile(FILE_COORDS);
        for(int lidx = 0; lidx < coords.size(); lidx++){
            String[] pair = coords.get(lidx);
            String id = lidx + "";
            String locName = pair[2];
            double lat = Double.parseDouble(pair[0]);
            double lng = Double.parseDouble(pair[1]);
            Location home = new HomeLocation(id, lat, lng, locName);
            for(int p = 0; p < FAMILY_SIZE; p++){
                double limit = 0.18;
                Routine routine = new NormalRoutine(limit);
                AgeGroup ageGroup;
                if(p < 2){
                    ageGroup = AgeGroup.ADULT;
                }
                else{
                    ageGroup = AgeGroup.CHILD;
                }
                Person person = new Person(ageGroup, routine, home);
                city.addPerson(person);
            }
            city.addLocation(home);
        }
        
        /*
         * Infect a single person
         */
        List<Person> people = city.getPeople();
        //int target = Helper.getRandom().nextInt(people.size());
        int target = 2;
        //Disease disease = new DeltaDisease();
        Disease disease = new GammaDisease();
        people.get(target).doInfect(disease);
        
        /*
         * Run outbreak to completion
         */
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
        Helper.printPeopleData(FILE_PEOPLE, city);
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