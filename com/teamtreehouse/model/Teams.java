package com.teamtreehouse.model;

import java.util.SortedSet;
import java.util.TreeSet;

import com.teamtreehouse.model.Players;

public class Teams {
  private SortedSet<Team> teams;
  
  public Teams() {
    teams = new TreeSet<Team>();
  }  
  
  public boolean addTeam(Team team) {
    return teams.add(team);
  } 
  
  public boolean isPosibleToCreateTeam() {
    return teams.size() < (Players.load().length / 11);
  }
  
  public int getTeamCount() {
    return teams.size();
  }
  
  public boolean isEmpty() {
    return teams.isEmpty();
  }
  
  public Team[] toArray() {
    return teams.toArray(new Team[0]);
  }   
}