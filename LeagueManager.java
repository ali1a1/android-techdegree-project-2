import com.teamtreehouse.model.Player;
import com.teamtreehouse.model.Players;
import com.teamtreehouse.model.Team;
import com.teamtreehouse.model.Teams;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Arrays;

public class LeagueManager {
  private Teams teams;
  private SortedSet<Player> freePlayers;
  private static LeaguePrompter prompter;

  public static void main(String[] args) {
    LeagueManager manager = new LeagueManager();
    manager.freePlayers = new TreeSet<Player>( Arrays.asList(Players.load()) );
    LeagueManager.prompter = new LeaguePrompter();   
    manager.teams = new Teams();
    
    manager.processOption(prompter.promptForMenuOption());
    
  }
   
  private void processOption(String option) {
    Team team = null;
    switch(option) {
      case "create":
        if(teams.isPosibleToCreateTeam()) {
          team = prompter.promptForNewTeam();            
          if(teams.addTeam(team))
            LeaguePrompter.printFormat("\nTeam %s successfully created.\n", team.toString());
          else
            LeaguePrompter.printFormat("\nTeam %s already exist.\n", team.toString());
        } else {
          LeaguePrompter.printLine("\nSorry, according to the player count no other team can be created.");
        }
      break;
      
      case "add": 
        team = prompter.promptForAvailableTeams(teams.toArray());
        if(team != null)
          prompter.promptForAddingPlayers(team, freePlayers);         
      break;
      
      case "remove":
        team = prompter.promptForAvailableTeams(teams.toArray());
        if(team != null)
          prompter.promptForRemovingPlayers(team, freePlayers);
      break;
      
      case "report":
        team = prompter.promptForAvailableTeams(teams.toArray());
        if(team != null)
          prompter.printPlayersByHeight(team);
      break;
      
      case "balance":
        prompter.printLeagueBalanceReport(teams.toArray());
      break;
      
      case "roster":
        prompter.printRoster(teams.toArray());
      break;
      
      case "quit":
        System.exit(0);
      break;
      
      default:
        LeaguePrompter.print("Invalid option, please try again: ");
        processOption(LeaguePrompter.readLine().toLowerCase());
    }
    // Continue to show menu option until the user quit/exit.
    processOption(prompter.promptForMenuOption());    
  }    
}














