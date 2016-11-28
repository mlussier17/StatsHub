package ml.statshub.statshub.Marqueur.Hockey;

/**
 * Created by 196128636 on 2016-11-28.
 */

public class Results {
    private int away;
    private int home;

    public void setAway(int id){away += id;}
    public void setHome(int id){home += id;}

    public int getAway(){return away;}
    public int getHome(){return home;}
}
