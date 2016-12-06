package ml.statshub.statshub.Class;

/*
    Class to store the Teams statistics from the database server
*/

public class Teams {
    private int _idTeams;
    private String _teamsName;
    private int _nbGP;
    private int _nbWins;
    private int _nbLoses;
    private int _nbOTLoses;
    private int _nbSOLoses;
    private int _nbPoints;
    private int _nbGF;
    private int _nbGA;
    private int _nbTies;
    private int _nbYC;
    private int _nbRC;


    public void setId(int id){_idTeams = id;}
    public void setTeams(String teams){_teamsName = teams;}
    public void setNbGP(int id){_nbGP = id;}
    public void setWins(int id){_nbWins = id;}
    public void setLoses(int id){_nbLoses = id;}
    public void setOTLoses(int id){_nbOTLoses = id;}
    public void setSOLoses(int id){_nbSOLoses = id;}
    public void setPoints(int id){_nbPoints = id;}
    public void setGF(int id){_nbGF = id;}
    public void setGA(int id){_nbGA = id;}
    public void setTies(int id){_nbTies = id;}
    public void setYC(int id){_nbYC = id;}
    public void setRC(int id){_nbRC = id;}

    public int getId(){return _idTeams;}
    public String getName(){return _teamsName;}
    public int getGP(){return _nbGP;}
    public int getWins(){return _nbWins;}
    public int getLoses(){return _nbLoses;}
    public int getOTLoses(){return _nbOTLoses;}
    public int getSOLoses(){return _nbSOLoses;}
    public int getPoints(){return _nbPoints;}
    public int getGF(){return _nbGF;}
    public int getGA(){return _nbGA;}
    public int getTies(){return _nbTies;}
    public int getYC(){return _nbYC;}
    public int getRC(){return _nbRC;}


}
