package ml.statshub.statshub;

/**
 * Created by 196128636 on 2016-10-05.
 */

public class Leagues {
    public int _idLeagues;
    public String _leagueName;
    public int _nbTeams;

    public Leagues(){

    }

    public void setId(int id){_idLeagues = id;}
    public void setName(String name){_leagueName = name;}
    public void setTeams(int number){_nbTeams = number;}
    public int getTeams(){return _nbTeams;}
    public int getId(){return _idLeagues;}
    public String getName(){return _leagueName;}

}
