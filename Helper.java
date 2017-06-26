import java.util.*;
import java.io.*;
import java.util.regex.*;

public class Helper {

    private static Random random = new Random();
    public static Random getRandom(){
        return random;
    }
    
    private static class FileRecord {
        
        private String filename;
        private FileWriter fw = null;
        private BufferedWriter bw = null;
        private FileReader fr = null;
        private BufferedReader br = null;
        
        public FileRecord(String filename){
            this.filename = filename;
        }
        
        public boolean isOpenForWriting(){
            return this.bw != null;
        }
        
        public boolean isOpenForReading(){
            return this.br != null;
        }
        
        public void openForWriting(){
            try{
                this.fw = new FileWriter(filename);
                this.bw = new BufferedWriter(this.fw);
            }
            catch(IOException e){
                System.out.println("Error in openForWriting()");
                e.printStackTrace();
            }
        }
        
        public void openForReading(){
            try{
                this.fr = new FileReader(filename);
                this.br = new BufferedReader(this.fr);
            }
            catch(IOException e){
                System.out.println("Error in openForReading()");
                e.printStackTrace();
            }
        }
        
        public void writeFileLine(String content){
            try{
                bw.write(content + "\n");
            }
            catch(IOException e){
                System.out.println("Error in writeFileLine()");
                e.printStackTrace();
            }
        }
        
        public String readEntireFile(){
            String out = "";
            try{
                String line = null;
                boolean reading = true;
                while(reading){
                    line = br.readLine();
                    if(line == null){
                        reading = false;
                    }
                    else{
                        out += line;
                    }
                }
            }
            catch(IOException e){
                System.out.println("Error in readEntireFile()");
                e.printStackTrace();
            }
            return out;
        }
        
        public void closeFile(){
            try{
                if(bw != null){
                    bw.close();
                }
                if(fw != null){
                    fw.close();
                }
                if(br != null){
                    br.close();
                }
                if(fr != null){
                    fr.close();
                }
            }
            catch(IOException e){
                System.out.println("Error in closeFile()");
                e.printStackTrace();
            }
        }
        
    }
    
    private static HashMap<String, FileRecord> fileMap = new HashMap<String, FileRecord>();
    
    public static void writeFileLine(String filename, String content){
        if(!fileMap.containsKey(filename)){
            fileMap.put(filename, new FileRecord(filename));
        }
        FileRecord rec = fileMap.get(filename);
        if(!rec.isOpenForWriting()){
           rec.openForWriting(); 
        }
        rec.writeFileLine(content);
    }
    
    public static String readEntireFile(String filename){
        if(!fileMap.containsKey(filename)){
            fileMap.put(filename, new FileRecord(filename));
        }
        FileRecord rec = fileMap.get(filename);
        if(!rec.isOpenForReading()){
            rec.openForReading();
        }
        String content = rec.readEntireFile();
        return content;
    }
    
    public static void closeAllFiles(){
        for(Map.Entry<String, FileRecord> entry : fileMap.entrySet()){
            entry.getValue().closeFile();
        }
    }

    public static List<String[]> readCoordsFromFile(String filename){
        List<String[]> coords = new ArrayList<String[]>();
        String input = Helper.readEntireFile(filename);
        String[] coordStrs = input.split(Pattern.quote("$"));
        for(String str : coordStrs){
            String[] pair = str.split(Pattern.quote(","));
            coords.add(pair);
        }
        return coords;
    }

    public static void printCityLine(String filename, City city){
        String out = "" + city.getTime();
        List<Person> people = city.getPeople();
        HashMap<Person.State, List<Person>> stateMap = Person.groupPeopleByState(people);
        for(Person.State state : Person.State.values()){
            out += "," + stateMap.get(state).size();
        }
        Helper.writeFileLine(filename, out);
    }
    
    public static void printLocationLine(String filename, City city){
        String out = city.getTime() + ":";
        List<Person> infected = Person.groupPeopleByState(city.getPeople()).get(Person.State.INFECTED);
        HashMap<Location, List<Person>> map = Person.groupPeopleByLocation(infected);
        for(Map.Entry<Location, List<Person>> entry : map.entrySet()){
            Location loc = entry.getKey();
            List<Person> inf = entry.getValue();
            out += loc.getID() + "," + inf.size() + "$";
        }
        out += "%";
        Helper.writeFileLine(filename, out);
    }

    public static void printPeopleData(String filename, City city){
        int p = 0;
        for(Person person : city.getPeople()){
            List<Person.Record> history = person.getHistory();
            String out = "Person " + p + "," + person.getAgeGroup() + ",";
            for(Person.Record rec : history){
                out += rec + ",";
            }
            Helper.writeFileLine(filename, out);
            p++;
        }
    }

}