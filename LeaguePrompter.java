import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Team;

public class LeaguePrompter extends Prompter {  
  private static final String[] PLAYERS_BY_HEIGHT_MAP_KEYS = {"35-40 inches", "41-46 inches", "47-50 inches"};

  public String promptForMenuOption() {
     printLine("\nMENU");
     printLine("Create  - Create a new team.");
     printLine("Add     - Add a player to a team.");
     printLine("Remove  - Remove a player from a team.");
     printLine("Report  - View a report of a team by height.");
     printLine("Balance - View the league balance report.");
     printLine("Roster  - View roster.");
     printLine("Quit    - Exit the program.\n");         
     print("Select an Option: ");
     return readLine().toLowerCase();
   }
  
   public Team promptForNewTeam() {
     Team team = new Team();
     print("\nWhat is the team name?  ");
     team.setTeamName(Prompter.readLine());
     print("What is the coach name? "); 
     team.setCoachName(readLine());
     return team;
   }
  
   public Team promptForAvailableTeams(Team[] teams) {
     if(!isEmpty(teams)) { // At least one team was created.
       if(teams.length == 1) {
         return teams[0];
       } else {
         printLine("\nTEAM LIST");
         printLine("No.          Teams");
         printLine("--- ------------------------");
         for(int i = 0; i < teams.length; i++) {
           printFormat("%d.) Team %s (%d players)\n", 
                       i+1, 
                       teams[i].toString(),
                       teams[i].size());
         }      
         print("\nSelect a team number: ");
         int teamIndex = readPositiveInt() - 1; // zero based index.     
         while (teamIndex == -1 || teamIndex >= teams.length) {
           print("Invalid option, please try again: ");
           teamIndex = readPositiveInt() - 1;
         } 
         return teams[teamIndex];
        }    
     }
    return null;
   }
  
   public void promptForAddingPlayers(Team team, SortedSet<Player> freePlayersSet) {
     if(!isFull(team)) {  // Team is not full.          
       Player[] freePlayersArray = freePlayersSet.toArray(new Player[0]);
       printPlayersList(freePlayersArray);
       
       printFormat("\nADDING PLAYERS FOR TEAM %s.\n", team.toString().toUpperCase());
       printLine("Please select a player serial number or 0 to exit. ");
       int playerIndex = 0;
       int playerCount = team.size() + 1;      
       do{
         print("Enter option: ");
         playerIndex = readPositiveInt() -1; // 0 based index
         try {
           Player selectedPlayer = freePlayersArray[playerIndex];
           if(team.addPlayer(selectedPlayer)){
             printFormat("P%d- %s was added. ", 
                         playerCount++,
                         selectedPlayer.getFirstName());
           //For not showing recruited players.
             freePlayersSet.remove(selectedPlayer);               
           } else {// Unsuccessful adding
             printFormat("You chose this player. ");
           }
         }catch(IndexOutOfBoundsException e) {
           if(playerIndex == -1)
             break; // Entered 0
           else
            print("Input out of bound. ");
         }
       }while(!team.isFull());    
       
       if(team.isFull()) {
         printFormat("\n\nWonderful, team %s is full.\n", team.toString());
       }
     }  
  }
  
  public void promptForRemovingPlayers(Team team, SortedSet<Player> freePlayersSet) {
    if(!isEmpty(team)) {
      SortedSet<Player> teamPlayersSet = team.getTeamPlayers();
      Player[] teamPlayersArray = teamPlayersSet.toArray(new Player[0]);
      printPlayersList(teamPlayersArray); 
      
      printFormat("\nREMOVING PLAYERS FROM TEAM %s.\n", team.toString().toUpperCase());
      printLine("Please select a player serial number or 0 to exit. ");
      int playerIndex = 0;      
      do{
         print("Enter option: ");
         playerIndex = readPositiveInt() -1; 
         try {
           Player selectedPlayer = teamPlayersArray[playerIndex];
           if(team.removePlayer(selectedPlayer)){
             printFormat("%s was removed sucessfuly. ", 
                         selectedPlayer.getFirstName());
             // Make removed player available for other teams.
             freePlayersSet.add(selectedPlayer);               
           } else {
             printFormat("You removed this player. ");
           }
         }catch(IndexOutOfBoundsException e) {
           if(playerIndex == -1)
             break;
           else
            print("Input out of bound. ");
         }
       }while(!team.isEmpty());  
      
       if(team.isEmpty()) {
          printFormat("\n\nTeam %s is now empty.\n", team.toString());
       }
    }
  }
    
  public void printPlayersByHeight(Team team) {
    if(!isEmpty(team)) {   
      Map<String, TreeSet<Player>> playersByHeight = mapPlayersByHeight(team);       
      printFormat("\nPLAYERS BY HEIGHT REPORT FOR TEAM %s.\n", team.toString().toUpperCase()); 
      
      for(String key : PLAYERS_BY_HEIGHT_MAP_KEYS) {
        Player[] heightGroup = playersByHeight.get(key).toArray(new Player[0]);
        // Print group heading.
        printFormat("%s (%d players):\n", key, heightGroup.length);
        // Print players under current group.
        if(heightGroup.length == 0) {
          printLine("\tNo players.");
          continue;
        }
        for(Player player : heightGroup) {
          printFormat("\t%s [%d\"]\n", 
                  player.getFullName(), 
                  player.getHeightInInches());
        }
      }
      waitForEnterKey();
    }
  } 

  public void printLeagueBalanceReport(Team[] teams) {
    if(!isEmpty(teams)) {  
      printLine("\nLEAGUE BALANCE REPORT");
      for(Team team : teams) {
        Map<String, TreeSet<Player>> playersByHeight = mapPlayersByHeight(team); 
        int experiencedCount = getExperiencePlayersCount(team);     
        int teamSize = team.size();
        // Print teams experience report.
        printFormat("Team %s (%d players): \n", team.toString(), team.size());
        printFormat("\tPast experience: %d <experienced>\t%d <inexperienced>\t%.0f%% <average experience>\n",
                   experiencedCount, 
                   teamSize - experiencedCount, 
                   //avoid deviding by zero while calculating average experience percentage.
                   teamSize == 0? 0f : (float) experiencedCount / teamSize * 100);   
        // Print team heaigh report summary.
        print("\t         Height: ");
        for(String key : PLAYERS_BY_HEIGHT_MAP_KEYS) {
          printFormat("%d <%s>\t", playersByHeight.get(key).size(), key);                              
        } 
        printLine(""); //Go to next line.    
      }
      waitForEnterKey();
    }
    
  }   

  public void printRoster(Team[] teams) {
    if(!isEmpty(teams)) {
      printLine("\nROSTER");
      Player[] players;                
      for(Team team : teams) {
        printFormat("Team %s (%d players):\n\t", team.toString(), team.size());
        players = team.getTeamPlayers().toArray(new Player[0]);
        
        if(players.length == 0) { 
          printLine("This team is empty."); 
        } else {
          // Print two players on each line.
          for(int i = 0; i < players.length; i++) {
            if(i == 0) {
              printFormat("[%s, ", players[i].toString());
            } else if(i == players.length - 1) {
              printFormat("%s]\n", players[i].toString());
            } else {
              printFormat("%s, ", players[i].toString());
              if((i+1) % 2 == 0) print("\n\t ");
            }                    
          }
        }

      }
      waitForEnterKey();
    }
  }
  
  
  private boolean isEmpty(Team team) {
    if(team.isEmpty()) {
      printFormat("\nTeam %s is empty.\n", team.toString());
      return true;
    }
    return false;
  }
  
  private boolean isEmpty(Team[] teams) {
    if(teams.length == 0) {
      printLine("No team available, please create a new team using \"Create\" option below.");
      return true;
    }
    return false;
  }
  
  private boolean isFull(Team team) {
    if(team.isFull()) {
      printFormat("\nTeam %s has maximun number of players, first remove the player you want to replace.\n", team.getTeamName()); 
      return true;
    }
    return false;
  }

  private void printPlayersList(Player[] players) {
    printLine("\nPLAYER LIST");
    printLine("No.               Players");
    printLine("--- -------------------------------------");
    for(int i = 0; i < players.length; i++) {
      printFormat("%d.) %s\n", i + 1, players[i].toString());
   }
  } 
  
  private int getExperiencePlayersCount(Team team) {
    Iterator<Player> iterator = team.getTeamPlayers().iterator();
    int experiencedCount = 0;
    for(int i = 0; i < team.size(); i++) {
      if(iterator.next().isPreviousExperience()) 
        experiencedCount++;
    }    
    return experiencedCount;   
  }
  
  private Map<String, TreeSet<Player>> mapPlayersByHeight(Team team) {
    Player[] players = team.getTeamPlayers().toArray(new Player[0]);
    Map<String, TreeSet<Player>> playersByHeight = new HashMap<String, TreeSet<Player>>();
    
    for(String key : PLAYERS_BY_HEIGHT_MAP_KEYS) {
      playersByHeight.put(key, new TreeSet<Player>(Player.HeightComperator));
    }
    
    for(Player player : players) {
      int height = player.getHeightInInches();
      if(height < 41) {;
        playersByHeight.get(PLAYERS_BY_HEIGHT_MAP_KEYS[0]).add(player);       
      } else if(height < 47) {
        playersByHeight.get(PLAYERS_BY_HEIGHT_MAP_KEYS[1]).add(player);  
      } else {
        playersByHeight.get(PLAYERS_BY_HEIGHT_MAP_KEYS[2]).add(player);  
      }
    }
 
    
    return playersByHeight;
  }
}







