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

    public void doTurn(City city){
        Location loc = routine.getNextLocation(this, city);
        this.location = loc;
    }
    
    private int infectedOn;
    public void doInfect(City city){
        infectedOn = city.getTime();
        this.setState(State.INFECTED);
    }
    
    public double getInfectivity(){
        return Main.getRandom().nextDouble() / 2;
    }
    
    public double getSusceptibility(){
        return Main.getRandom().nextDouble();
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
    
    private void setState(State state){
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