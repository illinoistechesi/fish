package fish;
import java.util.*;

public class Person {
    
    private String id;
    private int age;
    private AgeGroup ageGroup;
    private Routine routine;
    private Location location;
    private State state;
    
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
    
    public class Record {
        
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
    
    public Person(String id, AgeGroup ageGroup){
        this.id = id;
        this.ageGroup = ageGroup;
        this.age = AgeGroup.getRandomAge(ageGroup);
        this.routine = null;
        this.location = null;
        this.state = State.SUSCEPTIBLE;
        this.history = new ArrayList<Record>();
    }
    
    public Person(String id, AgeGroup ageGroup, Routine routine, Location location){
        this.id = id;
        this.ageGroup = ageGroup;
        this.age = AgeGroup.getRandomAge(ageGroup);
        this.routine = routine;
        this.location = location;
        this.state = State.SUSCEPTIBLE;
        this.history = new ArrayList<Record>();
    }
    
    private Map<String, Location> locationMap = new HashMap<String, Location>();
    
    public void addNamedLocation(String key, Location loc){
        this.locationMap.put(key, loc);
    }
    
    public Location getNamedLocation(String key){
        Location res = null;
        if(this.hasNamedLocation(key)){
            res = this.locationMap.get(key);
        }
        return res;
    }
    
    public boolean hasNamedLocation(String key){
        return this.locationMap.containsKey(key);
    }

    private int currentTime;
    
    public void doTurn(City city){
        currentTime = city.getTime();
        if(this.getPathogen() != null){
            doInfectionStep();
        }
        Location loc = this.getLocation();
        if(this.getRoutine() != null){
            loc = routine.getNextLocation(this, city);
        }
        loc = this.applyControlMeasures(city, loc);
        this.location = loc;
        this.history.add(new Record(currentTime, this.getLocation(), this.getState()));
    }
    
    private static double INCONVENIENCE_COST = 2.00 / 8.0;
    private ControlMeasure controlMeasure = null;
    private Location applyControlMeasures(City city, Location nextLoc){
        Location res = nextLoc;
        if(controlMeasure != null){
            int[] range = controlMeasure.getTimeRange();
            int time = city.getTime();
            if(time > range[0] && time < range[1]){
                res = controlMeasure.applyMeasure(city, this, nextLoc);
            }
        }
        if(!res.equals(nextLoc)){
            //String ln = this + " wanted to go to " + nextLoc + " but was quarantined to " + res + ".";
            //Helper.printlnLimitTo("q", ln, 10);
            controlMeasure.addAffectedPerson(this, city.getTime());
            controlMeasure.addCost(INCONVENIENCE_COST);
        }
        return res;
    }
    
    public ControlMeasure getControlMeasure(){
        return this.controlMeasure;
    }
    
    public void setControlMeasure(ControlMeasure cm){
        this.controlMeasure = cm;
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
        double res = Helper.getRandom().nextDouble();
        return res;
    }
    
    private int exposure;
    private Pathogen pathogen = null;
    
    public void doExposure(Pathogen pathogen){
        double infection = getSusceptibility() * pathogen.getInfectivity(ageGroup);
        double avoidance = Helper.getRandom().nextDouble();
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
    
    private int LATENT_THRESHOLD = 500;
    private int INCUBATION_TRESHOLD = 5000;
    
    private double COEFF = 2;//0.325;
    
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
    
    public int getAge(){
        return this.age;
    }
    
    public void setRoutine(Routine routine){
        this.routine = routine;
    }
    
    public Routine getRoutine(){
        return this.routine;
    }

    private void setLocation(Location loc){
        this.location = loc;
    }
    
    public Location getLocation(){
        return this.location;
    }
    
    public State getState(){
        return this.state;
    }
    
    private void setState(State state){
        this.state = state;
    }
    
    public List<Record> getHistory(){
        return this.history;
    }
    
    public String getID(){
        return this.id;
    }
    
    @Override
    public String toString(){
        return "Person " + this.getID();
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
    
    public static HashMap<AgeGroup, List<Person>> groupPeopleByAgeGroup(List<Person> people){
        HashMap<AgeGroup, List<Person>> map = new HashMap<AgeGroup, List<Person>>();
        for(AgeGroup ageGroup : AgeGroup.values()){
            map.put(ageGroup, new ArrayList<Person>());
        }
        for(Person p : people){
            map.get(p.getAgeGroup()).add(p);
        }
        return map;
    }
    
    
    
}