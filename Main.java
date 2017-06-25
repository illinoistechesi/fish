import java.util.*;
import java.io.*;
import java.util.regex.*;

/*
 * F.I.S.H.
 * The Fictional Infection Simulation Host
 */
public class Main {
    
    public static int TURNS = 30;
    public static int MAX_TURNS = 500;
    public static int GRID_SIZE = 5;
    public static int FAMILY_SIZE = 4;

    public static void main(String[] args){
        
        City city = new City("Scise City");
        
        List<double[]> coords = new ArrayList<double[]>();
        String input = "41.25858160718231,-95.95922470092773$41.26109793431034,-95.95969676971436$41.26322705846289,-95.96094131469727$41.262162505063486,-95.96094131469727$41.26725930033859,-95.96579074859619$41.267356071083924,-95.96394538879395$41.2674850985213,-95.96240043640137$41.265420628926684,-95.96248626708984$41.26906566393623,-95.96085548400879$41.270162360309904,-95.95999717712402$41.263356094059326,-95.964674949646$41.26529159740976,-95.96733570098877$41.26645287188313,-95.96836566925049$41.26767863919927,-95.96716403961182$41.26816248838431,-95.97081184387207$41.26974303740114,-95.97085475921631$41.26861407772201,-95.9719705581665$41.270065593723864,-95.97244262695312$41.25712983589226,-95.96076965332031$41.2572911454069,-95.95883846282959$41.257258883535826,-95.96278667449951$41.25719435974593,-95.96493244171143$41.25551671883519,-95.96566200256348$41.25551671883519,-95.9644603729248$41.25551671883519,-95.96325874328613$41.25561350698203,-95.96192836761475$41.25480693470953,-95.96201419830322$41.25464561905991,-95.96304416656494$41.254613355882164,-95.96428871154785$41.254613355882164,-95.96544742584229$41.25370998043617,-95.96536159515381$41.253742244060085,-95.9644603729248$41.25370998043617,-95.96308708190918$41.25358092578119,-95.96188545227051$41.25358092578119,-95.96072673797607$41.25293564868198,-95.96072673797607$41.253871298396376,-95.95982551574707$41.253096968554324,-95.95969676971436$41.253871298396376,-95.95879554748535$41.25322602416538,-95.95866680145264$41.25548445608771,-95.95742225646973$41.254742408497464,-95.95737934112549$41.25554898156673,-95.95544815063477$41.25471014536756,-95.95519065856934$41.266227070130796,-95.96561908721924$41.26619481267385,-95.96415996551514$41.2653561132001,-95.96570491790771$41.2653561132001,-95.96420288085938$";
        String[] coordStrs = input.split(Pattern.quote("$"));
        for(String str : coordStrs){
            String[] pairStr = str.split(Pattern.quote(","));
            double[] pair = new double[2];
            pair[0] = Double.parseDouble(pairStr[0]);
            pair[1] = Double.parseDouble(pairStr[1]);
            coords.add(pair);
        }
        /*for(double[] pair : coords){
            System.out.println(pair[0] + " <---> " + pair[1]);
        }*/
        
        for(int lidx = 0; lidx < coords.size(); lidx++){
            double[] pair = coords.get(lidx);
            Location home = new HomeLocation(pair[0], pair[1], "Location " + lidx);
            for(int p = 0; p < FAMILY_SIZE; p++){
                Routine routine = new NormalRoutine();
                Person person = new Person(routine, home);
                city.addPerson(person);
            }
            city.addLocation(home);
        }
        
        /*List<Location> locations = city.getLocations();
        
        // Get distance between two locations
        Location l1 = locations.get(0);
        Location l2 = locations.get(1);
        double distance = Location.getDistance(l1, l2);
        System.out.println("Distance: " + distance + " miles.");
        
        // Sort locations by distance from reference location
        List<Location> sorted = Location.sortByDistanceFrom(l1, locations);
        for(Location loc : sorted){
            double d = Location.getDistance(l1, loc);
            System.out.println(l1 + " to " + loc + ": " + d + " miles");
        }
        
        // Get locations within a certain radius of reference location
        double limit = 0.18;
        List<Location> nearby = Location.getWithin(l1, limit, locations);
        for(Location loc : nearby){
            double d = Location.getDistance(l1, loc);
            System.out.println(l1 + " to " + loc + ": " + d + " miles");
        }*/
        
        /*for(int x = 0; x < GRID_SIZE; x++){
            for(int y = 0; y < GRID_SIZE; y++){
                GridLocation home = new GridLocation("G" + x + "/" + y, x, y);
                for(int p = 0; p < FAMILY_SIZE; p++){
                    Routine routine = new NormalRoutine();
                    Person person = new Person(routine, home);
                    city.addPerson(person);
                }
                city.addLocation(home);
            }
        }*/
        
        List<Person> people = city.getPeople();
        int randomInt = Main.getRandom().nextInt(people.size());
        Disease disease = new DeltaDisease();
        people.get(randomInt).doInfect(disease);
        
        openFile();
        
        //Main.printCitySummary(city);
        //Main.printLocationDetails(city);
        Main.printCityLine(city);
        
        //for(int i = 0; i < TURNS; i++){
        boolean outbreak = true;
        while(outbreak){
            city.doTurn();
            //Main.printLocationDetails(city);
            Main.printCityLine(city);
            int totalInfected = Person.getPeopleByState(city.getPeople()).get(Person.State.INFECTED).size();
            if(totalInfected == 0){
                outbreak = false;
            }
            if(city.getTime() > MAX_TURNS){
                outbreak = false;
            }
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