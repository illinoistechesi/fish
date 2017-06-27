package fish;

public abstract class Pathogen {
    
    public static void main(String args[]){
        City city = new City("Pathogen City");
        Pathogen pathogen = new Kineosphaera();
        Person person = new Person(AgeGroup.ADULT);
        person.doInfect(pathogen);
        int t = 0;
        int latentOn = -1;
        int incubatedOn = -1;
        int MAX_TURNS = 1000;
        while(person.getState() != Person.State.RESISTANT && t < MAX_TURNS){
            person.doTurn(city);
            if(person.isLatent() && latentOn == -1){
                latentOn = t;
            }
            if(person.isIncubated() && incubatedOn == -1){
                incubatedOn = t;
            }
            String out = "";
                out += t + ",";
                out += person.getState() + ",";
                out += person.getBacteria() + ",";
                out += person.getResponse();
            System.out.println(out);
            t++;
        }
        double DAY = 24.0;
        double latentDays = ((double)latentOn) / DAY;
        double incubatedDays = ((double)incubatedOn) / DAY;
        double totalDays = ((double)t) / DAY;
        System.out.println("Became latent after " + latentDays + " days.");
        System.out.println("Symptoms showing after " + incubatedDays + " days.");
        System.out.println("Infection lasted " + totalDays + " days.");
    }
    
    public Pathogen() {
        
    }
    
    abstract int expand(int bacteria);

    public String getName() {
        return "[DEFAULT] Disease Name";
    }
    
    public double getInfectivity(AgeGroup ageGroup) {
        return 0.0;
    }
    
    public double getToxigenicity(AgeGroup ageGroup) {
        return 0.0;
    }
    
    public double getResistance(AgeGroup ageGroup) {
        return 0.0;
    }
    
    public void printSummary() {
        System.out.println("Summary for " + this.getName());
        String header = "Age\tI\tT\tR";
        System.out.println(header);
        for(AgeGroup ageGroup : AgeGroup.values()){
            double i = this.getInfectivity(ageGroup);
            double t = this.getToxigenicity(ageGroup);
            double r = this.getResistance(ageGroup);
            String row = ageGroup + "\t" + i + "\t" + t + "\t" + r;
            System.out.println(row);
        }
    }
    
}