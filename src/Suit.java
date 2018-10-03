public enum Suit {

    Clubs("Clubs", '\u2660'),
    Diamonds("Diamonds",'\u2666'),
    Hearts("Hearts", '\u2665'),
    Spades("Spades", '\u2663');

    private final String key;
    private final int value;

    Suit(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public static boolean contains(String test) {

        for (Suit c : Suit.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }

        return false;
    }

    public String getKey() {
        return key;
    }

    public int getValue() {
        return value;
    }
}


