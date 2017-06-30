package fish;

public class Kineosphaera extends Pathogen {
    
    @Override
    public String getName() {
        return "Kineosphaera";
    }
    
    @Override
    public double getInfectivity(AgeGroup ageGroup) {
        return 0.1;
    }
    
    @Override
    public double getToxigenicity(AgeGroup ageGroup) {
        return 2.0;
    }
    
    @Override
    public double getResistance(AgeGroup ageGroup) {
        return 2.0;
    }
    
    public int expand(int bacteria){
        //return 2;
        return (int)Math.sqrt(bacteria);
        //return (int)Math.log((double)bacteria);
        //return bacteria;
        //return (int)Math.pow(bacteria, 2);
    }
    
}