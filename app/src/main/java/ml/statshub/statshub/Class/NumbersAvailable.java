package ml.statshub.statshub.Class;

import java.util.List;

/*
    Class to store the Numbers available for a specific team from the database server
*/

public class NumbersAvailable {
    private int idGames;
    private int idTeam;
    private List<Integer> numbers;

    public void setIdGames(int id){this.idGames = id;}
    public void setIdTeams(int id){this.idTeam = id;}
    public void setNumbers(List<Integer> id){this.numbers = id;}

    public int getIdGames(){return idGames;}
    public int getIdTeam(){return idTeam;}
    public List<Integer> getNumbers(){return numbers;}
}
