package fish;

public enum AgeGroup {
    
    CHILD ("Child"),
    TEEN ("Teen"),
    ADULT ("Adult"),
    ELDER ("Elder");
    
    private String name;
    
    AgeGroup(String name){
        this.name = name;
    }
    
    @Override
    public String toString(){
        return this.name;
    }
    
};