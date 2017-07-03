package fish;

public enum AgeGroup {
    
    CHILD ("Child", 5, 13),
    TEEN ("Teen", 13, 25),
    ADULT ("Adult", 25, 40),
    ELDER ("Elder", 40, 65);
    
    private String name;
    private int[] ageRange;
    
    AgeGroup(String name, int minAge, int maxAge){
        this.name = name;
        this.ageRange = new int[] {minAge, maxAge};
    }
    
    public static int getRandomAge(AgeGroup ag){
        int[] ar = ag.getAgeRange();
        int range = ar[1] - ar[0];
        int ageAdd = Helper.getRandom().nextInt(range);
        return ar[0] + ageAdd;
    }
    
    public int[] getAgeRange(){
        return this.ageRange;
    }
    
    @Override
    public String toString(){
        return this.name;
    }
    
};