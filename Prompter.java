import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Prompter {
  private static InputStreamReader mStreamReader = new InputStreamReader(System.in);
  private static BufferedReader mReader = new BufferedReader(mStreamReader);
  
  public static void print(String message) {
    System.out.print(message);
  }
    
  public static void printLine(String message) {
    System.out.println(message);
  }
  
  public static void printFormat(String format, Object... values) {
    System.out.format(format, values);
  }  
 
  public static String readLine() {   
    String input = "";
    boolean hasError;    
    do {
      hasError = false;
      try {
        input = mReader.readLine();
        if(input.isEmpty()) {
          print("No input received, please try again: ");
          hasError = true;
        }
      } catch(IOException e) {
        print("Error in receiving user input, please try again:");
        hasError = true;
      }
    }while(hasError);
    
    return input.trim();
  }  
  
  public static int readPositiveInt() {
    String inputBuffer; 
    int input = 0;
    Boolean wrongInput;    
    do {
     wrongInput = false;
     inputBuffer = readLine();  
        try {
          input = Integer.parseInt(inputBuffer);         
          // Decline negative integers.
          if(input < 0) {
            print("Please enter a positive number: ");
            wrongInput = true;
          }          
        } catch (NumberFormatException nfe) {
          // Decline non numeric or decimal inputs.
          print("Please enter and integer or a whole number: ");
          wrongInput = true;
        }      
    } while(wrongInput);    
    return input;
  }
  
  public static void waitForEnterKey() {
    try {
      print("\nPress Enter to continue.");
      mReader.readLine();
    } catch(IOException e) {}
  }
}