import java.util.*;

public class HomeLocation extends Location {
    
    private String name = "Home";

    public HomeLocation(){
        
    }

    public HomeLocation(String name){
        this.name = name;
    }
    
    @Override
    public void doInteractions(List<Person> people){
        // Home is where the heart is.
    }

}