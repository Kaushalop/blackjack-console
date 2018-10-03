/**
 * Card class will help us create cards for the blackjack game
 * and describe how it looks on the console
 */
public class Card {
    private Suit suit;
    private int index;

    private final static int ACE_INDEX = 1;
    private final static int KING_INDEX = 13;

    public final static String[] namesOfCards= {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};


    //Constructor to validate the Cards being used in the game
    public Card(String suit, int index) {

        //Check whether the suit actually exists
        if(Suit.contains(suit)) {
            this.suit = Suit.valueOf(suit);
        } else {
            System.out.println("Suit of the card being used in invalid : "+suit);
            System.exit(1);
        }

        //Check whether the index of the card stays in 1(Ace) to 13(King)
        if(index >= ACE_INDEX && index <= KING_INDEX) {
            this.index = index;
        } else {
            System.out.println("The card is not within Ace and King: "+index);
            System.exit(1);
        }
    }

    public int getIndex() {
        return index;
    }

    private String getCardNameForIndex(int index) {
        return namesOfCards[index-1];
    }

    //So that we can read what cards are dealt to us and the Dealer of-course!
    @Override
    public String toString() {
        //use Unicode to print the suits
        return getCardNameForIndex(getIndex())+""+Character.toString((char)suit.getValue());
    }

    //checks whether the card being passed is the same as the current one
    public boolean isSameCard(Card B) {
        boolean result = false;
        if (this.getIndex() == B.getIndex()) {
            result = true;
        }
        return result;
    }

}
