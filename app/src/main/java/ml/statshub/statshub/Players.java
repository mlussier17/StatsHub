package ml.statshub.statshub;

/**
 * Created by 196128636 on 2016-10-05.
 */

public class Players {
    private int _id;
    private String _fName;
    private String _name;
    private int _nbGP;
    private int _nbG;
    private int _nbA;
    private int _nbPts;
    private int _nbPPG;
    private int _nbPKG;
    private int _nbPen;

    public void setId(int id){_id = id;}
    public void setFName(String players){_fName = players;}
    public void setName(String id){_name = id;}
    public void setGP(int id){_nbGP = id;}
    public void setGoals(int id){_nbG = id;}
    public void setAssists(int id){_nbA = id;}
    public void setPoints(int id){_nbPts = id;}
    public void setPPG(int id){_nbPPG = id;}
    public void setPKG(int id){_nbPKG = id;}
    public void setPenalty(int id){_nbPen = id;}

    public int getId(){return _id;}
    public String getFName(){return _fName;}
    public String getName(){return _name;}
    public int getGP(){return _nbGP;}
    public int getGoals(){return _nbG;}
    public int getAssists(){return _nbA;}
    public int getPts(){return _nbPts;}
    public int getPPG(){return _nbPPG;}
    public int getPKG(){return _nbPKG;}
    public int getPen(){return _nbPen;}
}
