import java.util.*;
import java.io.*;

/*
 * F.I.S.H.
 * The Fictional Infection Simulation Host
 */
public class Main {
    
    public static int TURNS = 30;
    public static int GRID_SIZE = 20;
    public static int FAMILY_SIZE = 4;

    public static void main(String[] args){
        
        City city = new City("Scise City");
        
        for(int x = 0; x < GRID_SIZE; x++){
            for(int y = 0; y < GRID_SIZE; y++){
                GridLocation home = new GridLocation("G" + x + "/" + y, x, y);
                for(int p = 0; p < FAMILY_SIZE; p++){
                    Routine routine = new NormalRoutine();
                    Person person = new Person(routine, home);
                    city.addPerson(person);
                }
                city.addLocation(home);
            }
        }
        
        List<Person> people = city.getPeople();
        int randomInt = Main.getRandom().nextInt(people.size());
        Disease disease = new DeltaDisease();
        people.get(randomInt).doInfect(disease);
        
        openFile();
        
        //Main.printCitySummary(city);
        //Main.printLocationDetails(city);
        Main.printCityLine(city);
        
        for(int i = 1; i < TURNS; i++){
            city.doTurn();
            //Main.printLocationDetails(city);
            Main.printCityLine(city);
        }
        
        //Main.printCitySummary(city);
        
        closeFile();
        
    }
    
    private static final String FILENAME = "output.txt";
    private static BufferedWriter bw = null;
    private static FileWriter fw = null;
    
    public static void openFile(){
        try{
            fw = new FileWriter(FILENAME);
            bw = new BufferedWriter(fw);
        }
        catch(IOException e){
            System.out.println("Error in openFile()");
            e.printStackTrace();
        }
    }
    
    public static void writeFileLine(String content){
        try{
            bw.write(content + "\n");
        }
        catch(IOException e){
            System.out.println("Error in writeFileLine()");
            e.printStackTrace();
        }
    }
    
    public static void closeFile(){
        try{
            if(bw != null){
                bw.close();
            }
            if(fw != null){
                fw.close();
            }
        }
        catch(IOException e){
            System.out.println("Error in closeFile()");
            e.printStackTrace();
        }
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
        Main.writeFileLine(out);
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