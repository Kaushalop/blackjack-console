import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;

/**
 * This class will help us in creating a game of blackjack for the risk taker
 * 1. Start the game and initialisation of decks to the risk taker and dealer involved
 * 2. Track RiskTaker's turn
 * 3. Track Dealer's turn
 * 4. Hit
 * 5. Stand
 * 6. DoubleDown
 * 7. Check if either won the game
 */
public class Game {

    //We need a deck to play
    private Deck deck;

    //The name of the risk taker
    private String riskTakerName;

    //We need two RiskTakers involved - Player and Dealer
    private RiskTaker player;
    private RiskTaker dealer;

    //Track the risk taker turn
    private boolean riskTakerTurnOver;

    //Track the dealer's turn
    private boolean dealerTurnOver;

    //Track RiskTaker's current bet amount
    private double betAmount;

    //Track RiskTaker's current balance
    private double balanceAmount;

    private BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

    //Choice whether DoubleDown is permitted or not
    private boolean allowDoubleDown;

    private final static int MAXIMUM_VALUE_DEALER_CAN_HIT = 17;
    private final static int DEFAULT_BALANCE_FOR_RISK_TAKER = 1000;
    private final static String DEALER = "Dealer";
    private final static int MINIMUM_BET_AMOUNT = 1;
    private final static String START_CHOICE = "S";
    private final static String YES = "Y";


    public Game(String riskTakerName) {

        //Initialise both the parties
        initialiseRiskTaker(riskTakerName);
        initialiseDealer();

        //Create a game
        initialiseGame();

        //let's start the game
        manageGame(false);
    }

    //Initialise the dealer as a RiskTaker
    private void initialiseDealer() {
        dealer = new RiskTaker(DEALER);
    }

    //Initialise the Player as a Risk Taker
    private void initialiseRiskTaker(String name) {
        player = new RiskTaker(name);
        this.riskTakerName = name;
    }

    //Initialise the game
    private void initialiseGame() {
        this.deck = new Deck(1);
        this.balanceAmount = DEFAULT_BALANCE_FOR_RISK_TAKER;
        System.out.println();
        System.out.println(this.riskTakerName + " has been provided with $"+DEFAULT_BALANCE_FOR_RISK_TAKER+" amount to start off taking risk!");
    }

    private void manageGame(boolean over) {
        //Game finish conditions are
        //1. When the balance runs out for the Risk Taker or
        //2. When loses a round

        while(this.balanceAmount >= MINIMUM_BET_AMOUNT && !over) {
            System.out.println("\n"+this.riskTakerName+", [S]tart or [E]nd the game? [S or E]");
            String riskTakerChoice = null;
            try {
                riskTakerChoice = stdin.readLine();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Could not read your choice!");
            }

            if(riskTakerChoice.compareToIgnoreCase(START_CHOICE) == 0){
                this.play();
            }
            else{
                //if anything else other than start, just end the game
                over = true;
            }
        }
        endGameAndCheckIfPlayAgain();
    }

    private void play() {

        boolean blackjack;
        this.betAmount = 0 ;
        this.allowDoubleDown = true;

        System.out.println("Balance for "+this.riskTakerName+" :$"+this.balanceAmount);
        String askBetMessage = "How many dollars for the bet? : ";

        while(this.betAmount<=0){
            try {

                System.out.println(askBetMessage);
                this.betAmount = Double.parseDouble(stdin.readLine());
            } catch(InputMismatchException e){
                System.err.println("Could not capture the bet amount!");
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Could not capture the bet amount!");
            } finally{
                askBetMessage = "How many dollars for the bet? Try Natural numbers please:";
            }
        }


        if((this.betAmount >= 1) && (this.betAmount%1 == 0) && (this.balanceAmount-this.betAmount>=0)){

            this.balanceAmount-= this.betAmount;

            this.riskTakerTurnOver = false;
            this.dealerTurnOver = false;

            // Assigning fresh hands to the players involved and then assigning cards to them
            player.assignFreshHand();
            dealer.assignFreshHand();

            player.getHand().addCardToHand(deck.getTopCardFromDeck(), this.riskTakerName);
            dealer.getHand().addCardToHand(deck.getTopCardFromDeck(), DEALER);

            player.getHand().addCardToHand(deck.getTopCardFromDeck(), this.riskTakerName);
            dealer.getHand().addCardToHand(deck.getTopCardFromDeck(), DEALER);


            // Cards that have been currently assigned
            System.out.println(dealer.getHand().toString(true));

            printRiskTakerDetails(player, true);

            //Confirming if blackjack or not already
            blackjack = this.checkIfBlackJack();

            while(!this.riskTakerTurnOver || !this.dealerTurnOver){
                if(!this.riskTakerTurnOver){
                    this.choose();
                } else if(!this.dealerTurnOver){

                    this.serveDealersHand();
                }
                System.out.println();
            }
            if(!blackjack){
                this.whoWon();
            }
        }
        else{
            System.out.println("Invalid bet amount! Try again with something less than $"+this.balanceAmount);
        }
    }

    private void endGameAndCheckIfPlayAgain() {
        System.out.println();
        System.out.println(this.riskTakerName+", You have chosen to end the current game!");

        System.out.println();
        System.out.println(this.riskTakerName+", Want to Give BlackJack a shot again?[Y or N]");
        String Y = null;
        try {
            Y = stdin.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not get your choice to play again!");
        }
        if(Y.compareToIgnoreCase(YES) == 0){

            new Game(this.riskTakerName);
        }
        //Since the risk taker chooses not to take risk anymore, we close the input reader
        try {
            stdin.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not close the Input reader!");
        }
    }

    private void whoWon() {

        //Compare the totals to figure out the winner
        int playerHandTotal = player.getHand().getHandTotalValue();
        int dealerHandTotal = dealer.getHand().getHandTotalValue();

        if(playerHandTotal > dealerHandTotal
                && playerHandTotal<= Hand.BLACKJACK_LIMIT || dealerHandTotal > Hand.BLACKJACK_LIMIT) {

            this.balanceAmount+= this.betAmount*2;
            System.out.println(this.riskTakerName + " won! New balance : "+this.balanceAmount);

        } else if(playerHandTotal == dealerHandTotal) {

            this.balanceAmount+= this.betAmount;
            System.out.println(this.riskTakerName + " drew! New balance : "+this.balanceAmount);

        } else {
            System.out.println(this.riskTakerName + " lost! New balance : "+this.balanceAmount);
        }
    }

    private void serveDealersHand() {
        if(dealer.getHand().getHandTotalValue() > MAXIMUM_VALUE_DEALER_CAN_HIT) {
            //Dealer can't hit anymore
            dealerTurnOver = true;
            System.out.println(dealer.getHand().toString(false));
            System.out.println("Dealer Total : "+dealer.getHand().getHandTotalValue());
        } else {
            //Dealer can still hit
            System.out.println("\tDealer Hits");
            dealerTurnOver = !dealer.getHand().addCardToHand(deck.getTopCardFromDeck(), DEALER);

            if(dealerTurnOver) {
                System.out.println(dealer.getHand().toString(false));
                System.out.println("Dealer Total : "+dealer.getHand().getHandTotalValue());
                System.out.println("Dealer Busted");
            }
        }
    }

    // Risk Taker's Double Down
    private void doubleDown(){

        System.out.println("\tYou Choose to Double Down.");

        riskTakerTurnOver = player.getHand().addCardToHand(deck.getTopCardFromDeck(), this.riskTakerName);
        this.balanceAmount = this.balanceAmount - this.betAmount;
        this.betAmount = 2 * this.betAmount;
        riskTakerTurnOver = true;
        printRiskTakerDetails(player, true);
        if(player.getHand().getHandTotalValue() > Hand.BLACKJACK_LIMIT){
            playerBusted();
        }

        System.out.println("Now Dealer plays");
    }

    // Risk taker plays Stand
    private void stand(){
        System.out.println("\tYou Choose to Stand, Dealer's turn");
        riskTakerTurnOver = true;
    }

    //Risk Taker wants to take more risk with Hit move!
    private void hit() {
        System.out.println("\tYou Choose to Hit!");
        riskTakerTurnOver = !player.getHand().addCardToHand(deck.getTopCardFromDeck(), this.riskTakerName);
        printRiskTakerDetails(player, true);
        //basically checking for BLACKJACK_LIMIT
        if(riskTakerTurnOver) {
            playerBusted();
        }

    }

    //function to do stuff when player is busted
    private void playerBusted() {
        riskTakerTurnOver = true;
        dealerTurnOver = true;

        System.out.println(player.getHand().toString(false));
        System.out.println(this.riskTakerName+"'s Total : "+player.getHand().getHandTotalValue());
        System.out.println(this.riskTakerName+" Busted");
    }

    //Print Player Details
    private void printRiskTakerDetails(RiskTaker person, boolean isPlayer) {
        System.out.println(person.getHand().toString(!isPlayer));

        System.out.printf(person.getName() + "'s Score:%d\t", person.getHand().getHandTotalValue());
        System.out.println("Bet:$"+this.betAmount);
        String content = isPlayer ? "Balance:$"+this.balanceAmount: "";
        System.out.println(content);
        System.out.println("-------------------------------------------");
    }

    //Take player's choice
    private void choose() {
        String choice = "S";//defaults to stand

        if(this.balanceAmount >= this.betAmount && this.allowDoubleDown){
            System.out.println("[H]it | [S]tand or [D]ouble Down? [H or S or D]");
        }
        else{
            this.allowDoubleDown = false;
            System.out.println("[H]it | [S]tand? [H or S]");
        }
        try {
            choice = stdin.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not pick your choice!");
        }

        switch (choice) {
            case "h":
            case "H":
                //hit case
                this.hit();
                this.allowDoubleDown = false;
                break;

            case "D":
            case "d":
                //Double Down
                if(allowDoubleDown) {
                    this.doubleDown();
                }
                break;
            case "s":
            case "S":
            default:
                //stand case
                this.stand();
                break;
        }
    }

    //Check whether the hand total for any player is leading to BlackJack
    private boolean checkIfBlackJack(){

        boolean blackJack = false;

        if(player.getHand().getHandTotalValue() == Hand.BLACKJACK_LIMIT){

            this.riskTakerTurnOver = true;
            this.dealerTurnOver = true;

            if(player.getHand().getHandTotalValue() > dealer.getHand().getHandTotalValue() || dealer.getHand().getHandTotalValue() > 21){

                System.out.println("You won! BlackJack!");
                System.out.println("Dealer : ");
                printRiskTakerDetails(dealer, false);


                this.balanceAmount= this.balanceAmount + (3*this.betAmount)/2 + this.betAmount;
                printRiskTakerDetails(player, true);

                blackJack = true;
            }
            else{

                System.out.println("Dealer : ");
                printRiskTakerDetails(dealer, false);

                blackJack = false;
            }
        }
        else if(dealer.getHand().getHandTotalValue() == Hand.BLACKJACK_LIMIT){
            System.out.println("You lost! BlackJack for Dealer!");
            System.out.println("Dealer : ");
            printRiskTakerDetails(dealer, false);

            this.dealerTurnOver = true;
            blackJack = false;
        }
        return blackJack;
    }
}
