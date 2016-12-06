package ml.statshub.statshub.Class;

/*
    Class to store the Leagues and number of teams in this league from the database server
*/

public class Leagues {
    private int _idLeagues;
    private String _leagueName;
    private int _nbTeams;

    public void setId(int id){_idLeagues = id;}
    public void setName(String name){_leagueName = name;}
    public void setTeams(int number){_nbTeams = number;}

    public int getTeams(){return _nbTeams;}
    public int getId(){return _idLeagues;}
    public String getName(){return _leagueName;}

}
