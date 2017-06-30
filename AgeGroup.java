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
        double range = (double)(ar[1] - ar[0]);
        double ageAdd = Helper.nextSeed() * range;
        return (int)Math.round((double)ar[0] + ageAdd);
    }
    
    public int[] getAgeRange(){
        return this.ageRange;
    }
    
    @Override
    public String toString(){
        return this.name;
    }
    
};