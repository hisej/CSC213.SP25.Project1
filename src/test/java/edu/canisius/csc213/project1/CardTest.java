package edu.canisius.csc213.project1;

import static org.junit.Assert.*;

import org.junit.Test;

public class CardTest {

    @Test
    public void testCardCreation() {
        Card card = new Card(Card.Suit.HEARTS, Card.Rank.ACE);
        assertEquals(Card.Suit.HEARTS, card.getSuit());
        assertEquals(Card.Rank.ACE, card.getRank());
    }

    @Test
    public void testToString() {
        Card card = new Card(Card.Suit.SPADES, Card.Rank.KING);
        assertEquals("KING of SPADES", card.toString());
    }

    @Test
    public void testEquality() {
        Card card1 = new Card(Card.Suit.CLUBS, Card.Rank.TEN);
        Card card2 = new Card(Card.Suit.CLUBS, Card.Rank.TEN);
        Card card3 = new Card(Card.Suit.DIAMONDS, Card.Rank.TEN);

        assertEquals(card1, card2);
        assertNotEquals(card1, card3);
    }

    @Test
    public void testHashCode() {
        Card card1 = new Card(Card.Suit.HEARTS, Card.Rank.THREE);
        Card card2 = new Card(Card.Suit.HEARTS, Card.Rank.THREE);
        assertEquals(card1.hashCode(), card2.hashCode());
    }
}