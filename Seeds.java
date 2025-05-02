public class Seeds {
    private String name;
    private int dayToHarvest;
    private String season;


    public Seeds(String name, int dayToHarvest, String season) {
        this.name = name;
        this.dayToHarvest = dayToHarvest;
        this.season = season;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getDayToHarvest() {
        return dayToHarvest;
    }
    public void setDayToHarvest(int dayToHarvest) {
        this.dayToHarvest = dayToHarvest;
    }
    public String getSeason() {
        return season;
    }
    public void setSeason(String season) {
        this.season = season;
    }
}
