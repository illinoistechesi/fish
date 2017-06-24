import java.util.*;

public class GridLocation extends Location {
    
    private String name = "Home";
    private int x;
    private int y;

    public GridLocation(String name, int x, int y){
        this.name = name;
        this.x = x;
        this.y = y;
    }
    
    public void doInteractions(List<Person> people){
        HashMap<Person.State, List<Person>> map = Person.getPeopleByState(people);
        for(Person personInf : map.get(Person.State.INFECTED)){
            List<Person> susceptibles = map.get(Person.State.SUSCEPTIBLE);
            Iterator<Person> iter = susceptibles.iterator();
            while(iter.hasNext()){
                Person personSus = iter.next();
                double inf = personInf.getInfectivity();
                double sus = personSus.getSusceptibility();
                if(inf > sus){
                    //personSus.setState(Person.State.INFECTED);
                    personSus.doInfect(personInf.getDisease());
                    iter.remove();
                }
            }
        }
    }
    
    public boolean isAdjacentTo(GridLocation loc){
        int diffX = Math.abs(loc.getX() - this.getX());
        int diffY = Math.abs(loc.getY() - this.getY());
        return diffX <= 1 && diffY <= 1;
    }
    
    public static List<Location> getAdjacentLocations(List<Location> list, GridLocation loc){
        List<Location> res = new ArrayList<Location>();
        for(Location lc : list){
            if(lc instanceof GridLocation){
                if(loc.isAdjacentTo((GridLocation)lc)){
                    res.add(lc);
                }
            }
        }
        return res;
    }
    
    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public String getName(){
        return this.name;
    }

}