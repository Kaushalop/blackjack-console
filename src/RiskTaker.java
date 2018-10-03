/**
 * This class helps with all functionality around the risk taker of the game BlackJack like
 */
public class RiskTaker {
    private String name;
    private Hand hand;


    public RiskTaker(String name) {
        this.name = name;
        assignFreshHand();
    }

    //assign a fresh hand to the player
    public void assignFreshHand() {
        hand = new Hand();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Hand getHand() {
        return hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }
}
