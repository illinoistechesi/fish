package fish;
import java.util.*;

public class Person {
    
    AgeGroup ageGroup;
    Routine routine;
    Location location;
    State state;
    
    public enum State {
        
        SUSCEPTIBLE ("Susceptible"),
        INFECTED ("Infected"),
        RESISTANT ("Resistant");
        
        String name;
        
        State(String name){
            this.name = name;    
        }
        
        public String getName(){
            return this.name;
        }
        
    };
    
    protected class Record {
        
        private int time;
        private Location location;
        private State state;
        
        public Record(int time, Location location, State state){
            this.time = time;
            this.location = location;
            this.state = state;
        }
        
        public int getTime(){
            return this.time;
        }
        
        public Location getLocation(){
            return this.location;
        }
        
        public State getState(){
            return this.state;
        }
        
        @Override
        public String toString(){
            return this.getTime() + "$" + this.getLocation() + "$" + this.getState();
        }
        
    }
    
    private List<Record> history;
    
    public Person(AgeGroup ageGroup){
        this.ageGroup = ageGroup;
        this.routine = null;
        this.location = null;
        this.state = State.SUSCEPTIBLE;
        this.history = new ArrayList<Record>();
    }
    
    public Person(AgeGroup ageGroup, Routine routine, Location location){
        this.ageGroup = ageGroup;
        this.routine = routine;
        this.location = location;
        this.state = State.SUSCEPTIBLE;
        this.history = new ArrayList<Record>();
    }

    private int currentTime;
    
    public void doTurn(City city){
        currentTime = city.getTime();
        this.history.add(new Record(currentTime, this.getLocation(), this.getState()));
        if(this.getPathogen() != null){
            doInfectionStep();
        }
        Location loc = this.getLocation();
        if(this.getRoutine() != null){
            loc = routine.getNextLocation(this, city);    
        }
        this.location = loc;
    }
    
    public boolean feelsSick(){
        return isIncubated() && getState() != State.RESISTANT;
    }
    
    public boolean isContagious(){
        return isLatent() && getState() != State.RESISTANT;
    }
    
    public boolean isVulnerable(){
        return getPathogen() == null && getState() != State.RESISTANT;
    }
    
    public double getSusceptibility(){
        double res = Helper.nextSeed();
        return res;
    }
    
    private int exposure;
    private Pathogen pathogen = null;
    
    public void doExposure(Pathogen pathogen){
        double infection = getSusceptibility() * pathogen.getInfectivity(ageGroup);
        double avoidance = Helper.nextSeed();
        if(infection > avoidance){
            this.doInfect(pathogen);
        }
    }
    
    public void doInfect(Pathogen pathogen){
        if(this.pathogen == null){
            this.exposure = currentTime;
            this.pathogen = pathogen;
            //this.bacteria = INITIAL_BACTERIA;
            //this.energy = INITIAL_ENERGY;   
        }
        //this.setState(State.INFECTED);
    }
    
    private boolean wasLatent = false;
    private boolean wasIncubated = false;
    
    public boolean isIncubated() {
        boolean res = bacteria > INCUBATION_TRESHOLD;
        if(res && !wasIncubated){
            this.setState(State.INFECTED);
            this.response = INITIAL_RESPONSE;
            this.wasIncubated = true;
        }
        return res;
    }
    
    public boolean isLatent() {
        boolean res = bacteria > LATENT_THRESHOLD;
        if(res && !wasLatent){
            this.wasLatent = true;
        }
        return res;
    }
    
    private int response = 0;
    private int INITIAL_RESPONSE = 10;
    
    private int bacteria = 10;
    private int MIN_BACTERIA = 0;
    private double BACTERIA_COST = 0.5;
    
    private int LATENT_THRESHOLD = 100;
    private int INCUBATION_TRESHOLD = 200;
    
    private double COEFF = 0.325;
    
    private int lastBacteria = 0;
    private int sumBacteria = 0;
    private double factor = 1.5;
    private void doInfectionStep(){
        lastBacteria = getBacteria();
        int expansion = pathogen.expand(getBacteria());
            bacteria -= (int)(BACTERIA_COST * (double)expansion);
            double resistance = Math.pow(pathogen.getResistance(ageGroup), -1);
            double immuneResponse = resistance * (double)getResponse();
            double bacteriaExpansion = pathogen.getToxigenicity(ageGroup) * (double)expansion;
            //bacteria += (expansion - (int)immuneResponse);
            bacteria += (int)(bacteriaExpansion - immuneResponse);
        if(bacteria > MIN_BACTERIA){
            sumBacteria += expansion;
            if(isIncubated()){
                response += (int)(Math.sqrt((double)getResponse()) * COEFF);
            }   
        }
        else{
            this.setState(State.RESISTANT);
            this.pathogen = null;
        }
    }
    
    public int getResponse() {
        return this.response;
    }
    
    public int getBacteria() {
        return this.bacteria;
    }
    
    public int getTimeSinceExposure() {
        return currentTime - exposure;
    }
    
    /*
     *External Section
     */
     
    public Pathogen getPathogen(){
        return this.pathogen;
    }
    
    public AgeGroup getAgeGroup(){
        return this.ageGroup;
    }
    
    public void setRoutine(Routine routine){
        this.routine = routine;
    }
    
    public Routine getRoutine(){
        return this.routine;
    }

    public void setLocation(Location loc){
        this.location = loc;
    }
    
    public Location getLocation(){
        return this.location;
    }
    
    public State getState(){
        return this.state;
    }
    
    public void setState(State state){
        this.state = state;
    }
    
    public List<Record> getHistory(){
        return this.history;
    }
    
    public static List<Person> getContagious(List<Person> people){
        List<Person> contagious = new ArrayList<Person>();
        for(Person person : people){
            if(person.isContagious()){
                contagious.add(person);
            }
        }
        return contagious;
    }
    
    public static List<Person> getVulnerable(List<Person> people){
        List<Person> vulnerable = new ArrayList<Person>();
        for(Person person : people){
            if(person.isVulnerable()){
                vulnerable.add(person);
            }
        }
        return vulnerable;
    }
    
    public static HashMap<Location, List<Person>> groupPeopleByLocation(List<Person> people){
        HashMap<Location, List<Person>> map = new HashMap<Location, List<Person>>();
        for(Person person : people){
            Location loc = person.getLocation();
            if(map.containsKey(loc)){
                map.get(loc).add(person);
            }
            else{
                List<Person> list = new ArrayList<Person>();
                list.add(person);
                map.put(loc, list);
            }
        }
        return map;
    }
    
    public static HashMap<State, List<Person>> groupPeopleByState(List<Person> people){
        HashMap<State, List<Person>> map = new HashMap<State, List<Person>>();
        for(State state : State.values()){
            map.put(state, new ArrayList<Person>());
        }
        for(Person p : people){
            map.get(p.getState()).add(p);
        }
        return map;
    }
    
}