package ml.statshub.statshub.Class;

import java.util.Date;

/**
 * Created by 196128636 on 2016-11-14.
 */

public class Games {
    private int id;
    private int idAway;
    private int idHome;
    private String nomAway;
    private String nomHome;
    private String location;
    private String date;

    public void setId(int id){this.id = id;}
    public void setAway(int id){this.idAway = id;}
    public void setHome(int id){this.idHome = id;}
    public void setAwayName(String id){this.nomAway = id;}
    public void setHomeName(String id){this.nomHome = id;}
    public void setLocation(String id){this.location = id;}
    public void setDate(String id){this.date = id;}

    public int getId(){return id;}
    public int getAway(){return idAway;}
    public int getHome(){return idHome;}
    public String getAwayName(){return nomAway;}
    public String getHomeName(){return nomHome;}
    public String getLocation(){return location;}
    public String getDate(){return date;}



}
