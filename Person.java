import java.util.*;

public class Person {
    
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
    
    public Person(Routine routine, Location location){
        this.routine = routine;
        this.location = location;
        this.state = State.SUSCEPTIBLE;
    }

    private int currentTime;
    
    public void doTurn(City city){
        currentTime = city.getTime();
        if(this.getState() == State.INFECTED){
            doInfectionStep();
        }
        Location loc = routine.getNextLocation(this, city);
        this.location = loc;
    }
    
    private int infectedOn;
    private int timeSinceInfection;
    private Disease disease;
    
    public void doInfect(Disease disease){
        this.infectedOn = currentTime;
        this.disease = disease;
        this.bacteria = INITIAL_BACTERIA;
        this.energy = INITIAL_ENERGY;
        this.setState(State.INFECTED);
    }
    
    private int energy;
    private int INITIAL_ENERGY = 100;
    private int MIN_ENERGY = 0;
    private int TOXIN_COST = 4;
    private int EXIT_COST = 2;
    private int DAY_COST = 2;
    
    private int bacteria;
    private int INITIAL_BACTERIA = 0;
    private int BACTERIA_GROWTH = 10;
    private int BACTERIA_DECAY = 2;
    private int ENERGY_PER_BACTERIA = 5;
    private int INCUBATION_TRESHOLD = 50;
    private int LATENT_THRESHOLD = 30;
    
    public boolean isIncubated() {
        return bacteria > INCUBATION_TRESHOLD;
    }
    
    public boolean isLatent() {
        return bacteria > LATENT_THRESHOLD;
    }
    
    public int getEnergy() {
        return this.energy;
    }
    
    public int getBacteria() {
        return this.bacteria;
    }
    
    public int getTimeSinceInfection() {
        return timeSinceInfection;
    }
    
    private void doInfectionStep(){
        if(energy > MIN_ENERGY){
            energy -= (DAY_COST * bacteria);
            DiseaseAction action = disease.move(this);
            switch(action){
                case MULTIPLY:
                    bacteria += BACTERIA_GROWTH;
                    energy += (ENERGY_PER_BACTERIA * bacteria);
                    //diseaseEvents.add("Day " + day + ": Infection multiplied.");
                    break;
                case RELEASE:
                    if(isIncubated()){
                        energy -= (TOXIN_COST * bacteria);
                        if(energy > MIN_ENERGY){
                            //diseaseEvents.add("Day " + day + ": Toxin released.");
                        }
                        else{
                            //diseaseEvents.add("Day " + day + ": Failed to release toxin.");
                        }   
                    }
                    else{
                        //diseaseEvents.add("Day " + day + ": Failed to release toxin.");
                    }
                    break;
                case EXIT:
                    if(isLatent()){
                        energy -= (EXIT_COST * bacteria);
                        if(energy > MIN_ENERGY){
                            //diseaseEvents.add("Day " + day + ": Infection exited the host.");
                        }
                        else{
                           //diseaseEvents.add("Day " + day + ": Failed to exit host."); 
                        }
                    }
                    else{
                        //diseaseEvents.add("Day " + day + ": Failed to exit host.");
                    }
                    break;
                default:
                    //diseaseEvents.add("Day " + day + ": No activity.");
                    break;
            }
            bacteria -= BACTERIA_DECAY;
            //Integer[] data = {energy, bacteria};
            //diseaseData.add(data);
            timeSinceInfection++;
        }
        else{
            this.setState(State.RESISTANT);
        }
    }
    
    /*
     *External Section
     */
    
    public double getInfectivity(){
        double res = Main.getRandom().nextDouble();
        return res;
    }
    
    public double getSusceptibility(){
        double res = Main.getRandom().nextDouble();
        return res;
    }
    
    public Disease getDisease(){
        return this.disease;
    }
    
    public void setRoutine(Routine routine){
        this.routine = routine;
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
    
    public static HashMap<State, List<Person>> getPeopleByState(List<Person> people){
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