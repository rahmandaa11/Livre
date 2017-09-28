package com.example.rahmanda.livre2;

/**
 * Created by rahmanda on 9/27/17.
 */

public class ModalMatch {
    private String matchId;
    private String team1;
    private String team2;

    public ModalMatch(){

    }

    public ModalMatch(String matchId, String team1, String team2) {
        this.matchId = matchId;
        this.team1 = team1;
        this.team2 = team2;
    }

    public String getMatchId() {
        return matchId;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }


}
