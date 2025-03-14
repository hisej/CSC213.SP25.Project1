package edu.canisius.csc213.project1;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.NoSuchElementException;

public class DeckTest {

    @Test
    public void testValidDeckSizes() {
        // Test deck with 4 cards (4 Aces)
        Deck deck4 = new Deck(4);
        assertEquals(4, deck4.size());

        // Test deck with 8 cards (4 Aces + 4 Kings)
        Deck deck8 = new Deck(8);
        assertEquals(8, deck8.size());

        // Test deck with 12 cards (4 Aces + 4 Kings + 4 Queens)
        Deck deck12 = new Deck(12);
        assertEquals(12, deck12.size());

        // Test full deck (52 cards)
        Deck deck52 = new Deck(52);
        assertEquals(52, deck52.size());
    }

    @Test
    public void testInvalidDeckSizes() {
        assertThrows(IllegalArgumentException.class, () -> new Deck(3));  // Not a multiple of 4
        assertThrows(IllegalArgumentException.class, () -> new Deck(55)); // Greater than 52
        assertThrows(IllegalArgumentException.class, () -> new Deck(0));  // Less than 4
    }

    @Test
    public void testDeckShuffling() {
        Deck deck = new Deck(52);

        // Get the order before shuffling
        Set<String> beforeShuffle = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            beforeShuffle.add(deck.draw().toString());
        }

        // Recreate deck and shuffle
        deck = new Deck(52);
        deck.shuffle();

        Set<String> afterShuffle = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            afterShuffle.add(deck.draw().toString());
        }

        // The order should be different
        assertNotEquals(beforeShuffle, afterShuffle);
    }

    @Test
    public void testDrawingCards() {
        Deck deck = new Deck(12);
        assertEquals(12, deck.size());

        Card drawnCard = deck.draw();
        assertNotNull(drawnCard);
        assertEquals(11, deck.size());

        // Draw all remaining cards
        for (int i = 0; i < 11; i++) {
            deck.draw();
        }

        // Deck should now be empty
        assertEquals(0, deck.size());
        assertThrows(NoSuchElementException.class, deck::draw);
    }
}

