import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A supportive class for the RiskTaker for details related to the hand
 * 1. get count of the value of the risk taker current hand
 * 2. deal to the risk taker
 * 3. see all the cards in the risk taker's hand
 * 3. see all the cards in the risk taker's hand
 */
public class Hand {
    private static final int DEFAULT_MAXIMUM_OF_CARDS_IN_A_HAND = 10;
    private static final int DEFAULT_MAXIMUM_VALUE_OF_FACE_CARDS_IN_A_HAND = 10;
    private static final int ACE_VALUE_11 = 11;
    public static final int BLACKJACK_LIMIT = 21;

    private Card[] cards = new Card[DEFAULT_MAXIMUM_OF_CARDS_IN_A_HAND];
    private int currentCountOfCardsInHand;

    public Hand() {
        this.currentCountOfCardsInHand = 0;
    }

    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) {
        this.cards = cards;
    }

    public int getCurrentCountOfCardsInHand() {
        return currentCountOfCardsInHand;
    }

    public void setCurrentCountOfCardsInHand(int currentCountOfCardsInHand) {
        this.currentCountOfCardsInHand = currentCountOfCardsInHand;
    }

    //Give a new card to the risk taker in the current hand and returns false if within limit
    public boolean addCardToHand(Card card, String riskTakerName) {
        //validation to check the count not exceeding the maximum
        if(this.currentCountOfCardsInHand == DEFAULT_MAXIMUM_OF_CARDS_IN_A_HAND){

            System.err.printf("Cannot give more cards to %s as the maximum is %s cards", riskTakerName, DEFAULT_MAXIMUM_OF_CARDS_IN_A_HAND);
            System.exit(1);
        }

        this.cards[this.currentCountOfCardsInHand] = card;
        this.currentCountOfCardsInHand++;

        return (this.getHandTotalValue() <= BLACKJACK_LIMIT);
    }

    //Get the current total value held in the hand by the risk taker
    public int getHandTotalValue() {
        AtomicInteger total = new AtomicInteger();
        AtomicInteger acesFound = new AtomicInteger();

        Arrays.stream(this.cards).forEach( card -> {
            if(card != null) {
                int currentCardIndex = card.getIndex();
                if(currentCardIndex == 1) {
                    // Card : A
                    acesFound.getAndIncrement();
                    total.addAndGet(ACE_VALUE_11);
                } else if(currentCardIndex >= DEFAULT_MAXIMUM_VALUE_OF_FACE_CARDS_IN_A_HAND) {
                    total.addAndGet(DEFAULT_MAXIMUM_VALUE_OF_FACE_CARDS_IN_A_HAND);
                } else {
                    total.addAndGet(currentCardIndex);
                }
            }

        });

        //handle the aces and use it as 1 or 11 as per the BLACKJACK_LIMIT
        while(total.get() > BLACKJACK_LIMIT && acesFound.get() > 0){
            total.addAndGet(-DEFAULT_MAXIMUM_VALUE_OF_FACE_CARDS_IN_A_HAND);
            acesFound.getAndDecrement();
        }
        return total.get();
    }

    //See all the cards in this hand
    public String toString(boolean dealersHand) {
        StringBuilder hand = new StringBuilder();
        for(int cardIndex=0; cardIndex < getCurrentCountOfCardsInHand(); cardIndex++) {
            if(dealersHand && cardIndex == 0) {
                hand.append("[not showing]");
            } else {
                hand.append(this.cards[cardIndex]).append(" ");
            }

        }
        return hand.toString();
    }
}
