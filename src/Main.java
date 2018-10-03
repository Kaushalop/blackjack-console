import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This is the main class that runs the game BlackJack for single player
 * @author Kaushal Saraf
 */
public class Main {

    //The main function will initiate a new Game with the name of the player
    public static void main(String[] args) {
        //Take Name of the player to make it more personal for the player
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        String riskTakerName = "";
        System.out.println("Welcome to BlackJack!");
        System.out.println("Please enter your name to start the game : ");
        try {
            riskTakerName = stdin.readLine();
        } catch (IOException e) {
            //This is the catch in case of errors while input
            e.printStackTrace();
            System.out.println("Please re-run the program with the First Input as the name!");
        }
        //creating an instance of the game to be played with {playerName}
        new Game(riskTakerName);

        try {
            stdin.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ran into issues with Standard Input! Please re-run the program!");
        }
    }
}
