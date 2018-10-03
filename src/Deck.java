import java.util.Arrays;
import java.util.Random;

/**
 * This class will help us with the functionality to be performed on the Deck
 * 1. Create a deck
 * 2. Shuffle
 * 3. Print the deck
 * 4. Get a card out of the Deck
 */
public class Deck {
    //Need cards to make a deck
    private Card[] cards;
    private int cardIndexToBeDealt;
    private int currentDeckCount;

    private final static int NUMBER_OF_CARDS_IN_A_SET = 52;
    private final static int NUMBER_OF_DEFAULT_SETS = 1;

    //we have to initialise the deck with the values whether we want one or more sets of cards in the deck
    public Deck() {
        setUpTheDeck(NUMBER_OF_DEFAULT_SETS);
    }

    public Deck(int numberOfSets) {
        setUpTheDeck(numberOfSets);
    }


    //Initialise the deck with the required number of {Card}s
    private void setUpTheDeck(int numberOfSets) {
        //Set the currentDeckCount as per the sets
        this.currentDeckCount = numberOfSets * NUMBER_OF_CARDS_IN_A_SET;
        this.cards = new Card[this.currentDeckCount];

        //We now create the {cards} array with the shuffled set
        int counter = 0;

        //For every {Suit} and {Card:index} we have
        for( int setIndex = 0; setIndex < numberOfSets; setIndex++) {
            for (Suit currentSuit : Suit.values())
            {
                for ( int index=1; index<=Card.namesOfCards.length; index++)
                {
                    Card card = new Card(currentSuit.toString(), index);
                    this.cards[counter++] = card;
                }
            }
        }
        //Now we need to shuffle this deck of cards
        shuffleTheDeck();

    }

    //Basic random integer generation and swapping functioning in the array {cards}
    private void shuffleTheDeck() {

            Random randomGenerator = new Random();
            for(int cardIndex=0; cardIndex<this.currentDeckCount;cardIndex++){

                int randomIndex = randomGenerator.nextInt(this.currentDeckCount);

                Card temp= this.cards[cardIndex];
                this.cards[cardIndex] = this.cards[randomIndex];
                this.cards[randomIndex] = temp;
            }
    }

    //Function to tell the last dealt index
    public int getCardIndexToBeDealt() {
        return cardIndexToBeDealt;
    }

    //Function to return the next card to be dealt
    public Card getTopCardFromDeck() {
        //If the deck is over, then return null as per the Single Responsibility principle
        if(cardIndexToBeDealt > currentDeckCount) {
            return null;
        }
        //return the card that is next in line
        return cards[cardIndexToBeDealt++];
    }

    //Get the deck as a string based on
    //if {currentDeck} is true, then only from {cardIndexToBeDealt} otherwise the whole deck
    public String toString(boolean currentDeck) {
        StringBuilder deck = new StringBuilder();
        int startIndex = 0;
        if(currentDeck) {
            startIndex = cardIndexToBeDealt;

        }
        for(int cardIndex=startIndex; cardIndex < currentDeckCount; cardIndex++) {
            deck.append(this.cards[cardIndex]).append(" ");
        }
        return deck.toString();
    }
}