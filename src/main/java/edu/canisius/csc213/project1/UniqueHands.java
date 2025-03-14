package edu.canisius.csc213.project1;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * UniqueHands class to analyze how long it takes to see every possible hand 
 * for different deck sizes and hand sizes.
 */
public class UniqueHands {
    public static void main(String[] args) {
        int[] deckSizes = {24, 28}; // Deck sizes to test
        int[] handSizes = {6, 7}; // Hand sizes to test
        int trials = 5; // Number of trials per deck-hand combination

        System.out.println("🃏 Deck Simulation: How long to see every possible hand?");
        System.out.println("------------------------------------------------------");

        // Implement nested loops
        // Outer loop: Iterates through deck sizes (24, 28)
        for (int deckSize : deckSizes) {

        // Inner loop: Iterates through hand sizes (6, 7)
            for (int handSize : handSizes) {
                long totalAttempts = 0;
                long totalTime = 0;

        // Inside inner loop: Run 5 trials, track time and attempts, and compute averages.  Which is probably another loop!
                for (int trial = 0; trial < trials; trial++) {
                    long startTime = System.nanoTime();
                    int attempts = countAttemptsToSeeAllHands(deckSize, handSize);
                    long endTime = System.nanoTime();

                    totalAttempts += attempts;
                    totalTime += (endTime - startTime);

                    System.out.printf("Trial %d: Deck Size = %d, Hand Size = %d, Attempts = %d, Time = %.2f seconds%n",
                            trial + 1, deckSize, handSize, attempts, (endTime - startTime) / 1e9);
                }

                // Calculate and print average results
                System.out.printf("Average Attempts for Deck Size = %d, Hand Size = %d: %d%n",
                        deckSize, handSize, totalAttempts / trials);
                System.out.printf("Average Time for Deck Size = %d, Hand Size = %d: %.2f seconds%n",
                        deckSize, handSize, totalTime / 1e9 / trials);
                System.out.println("------------------------------------------------------");
            }
        }
    }

    /**
     * Calculate the total number of unique hands (combinations).
     * C(n, k) = n! / (k! * (n - k)!)
     * 
     * @param deckSize The number of cards in the deck.
     * @param handSize The number of cards in a hand.
     * @return The total number of unique hands.
     */
    public static long calculateTotalUniqueHands(int deckSize, int handSize) {
        return (int) factorial(deckSize) / (factorial(handSize) * factorial(deckSize - handSize));
    }

    /**
     * Calculate the factorial of a number.
     * 
     * @param n The number to calculate the factorial of.
     * @return The factorial of n.
     */
    private static long factorial(int n) {
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    /**
     * Count the number of attempts it takes to see all possible unique hands.
     * 
     * @param deckSize The number of cards in the deck.
     * @param handSize The number of cards in a hand.
     * @return The number of attempts.
     */
    public static int countAttemptsToSeeAllHands(int deckSize, int handSize) {
        Set<Set<Card>> seenHands = new HashSet<>();
        Deck deck = new Deck(deckSize);
        Random rand = new Random();
        int attempts = 0;

        // Run until we've seen all unique hands
        while (seenHands.size() < calculateTotalUniqueHands(deckSize, handSize)) {
            deck.shuffle();
            Set<Card> hand = new HashSet<>();
            // Draw a hand of the specified size
            for (int i = 0; i < handSize; i++) {
                hand.add(deck.draw());
            }

            // Add the hand to the seen hands set (prevents duplicates)
            seenHands.add(hand);
            attempts++;
        }

        return attempts;
    }
}

// jh 3/12
// running at the terminal, but attempts and seconds are coming out 0 in the terminal, could this be bc it is not the test file