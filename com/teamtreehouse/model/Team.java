package com.teamtreehouse.model;

import java.util.SortedSet;
import java.util.TreeSet;

public class Team implements Comparable<Team> {
  private static final int MAX_PLAYERS = 11;
  private String teamName;
  private String coachName;
  private SortedSet<Player> teamPlayers; 
  
  public Team() {
    teamPlayers = new TreeSet<Player>();
  }
  
  public void setTeamName(String teamName) {
    this.teamName = teamName;
  }
  
  public void setCoachName(String coachName) {
    this.coachName = coachName;
  }
  
  public String getTeamName() {
    return teamName;
  }
  
  public String getCoachName() {
    return coachName;
  }
  
  public SortedSet<Player> getTeamPlayers() {
    return teamPlayers;
  }
  
  public boolean addPlayer(Player player) {
    if(teamPlayers.size() <= MAX_PLAYERS) 
      return teamPlayers.add(player);
    return false;  
  }
  
  public boolean removePlayer(Player player) {
    return teamPlayers.remove(player);
  }
  
  public boolean isFull() {
    if(teamPlayers.size() < MAX_PLAYERS)
      return false;
    return true;  
  }
  
  public boolean isEmpty() {
    return teamPlayers.isEmpty();
  }
  
  public int size() {
    return teamPlayers.size();
  } 
  
  @Override
  public boolean equals(Object o) {
    if(this == o) return true;
    if(!(o instanceof Team)) return false;    
    Team team = (Team) o;    
    if(!teamName.equalsIgnoreCase(team.teamName)) return false;
    return coachName.equalsIgnoreCase(team.coachName);    
  }
  
  @Override
  public int compareTo(Team other) {
    if(equals(other)) 
       return 0;       
    int result = teamName.compareTo(other.teamName);
    if(result == 0)
       return coachName.compareTo(other.coachName);       
    return result;
    
  }
  
  @Override
  public int hashCode() {
    int result = teamName.hashCode();
    result = 33 * result + coachName.hashCode();
    result = 33 * result + teamPlayers.hashCode();
    return result;
  }
  
  @Override
  public String toString() {
    return teamName + " coached by " + coachName;
  }
}