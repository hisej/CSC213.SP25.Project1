// Project 1 Card.java file

// Step 1: Define Card Class

package edu.canisius.csc213.project1;

public class Card {

    public enum Suit { HEARTS, DIAMONDS, CLUBS, SPADES }
    public enum Rank { 
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, 
        JACK, QUEEN, KING, ACE 
    }

    // Define private fields for suit and rank.
    private final Suit suit;
    private final Rank rank;

    // Implement the constructor.
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    // Implement getters for suit and rank.
    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    // Override toString() to return a readable format.
    @Override
    public String toString() {
        return rank.name() + " of " + suit.name();
    }

    // Override equals() and hashCode() for comparisons.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        Card card = (Card) obj;

        return rank == card.rank && suit == card.suit;
    }

    @Override
    public int hashCode() {
        int result = suit.hashCode();
        result = 31 * result + rank.hashCode();
        return result;
    }
}
