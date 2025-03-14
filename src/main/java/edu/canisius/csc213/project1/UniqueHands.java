package edu.canisius.csc213.project1;

import java.util.*;
import java.util.concurrent.*;

/**
 * UniqueHands class to analyze how long it takes to see every possible hand
 * for different deck sizes and hand sizes.
 */
public class UniqueHands {
    private static final int threadnum = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        int[] deckSizes = {24}; // Deck sizes to test
        int[] handSizes = {6}; // Hand sizes to test
        int trials = 2; // Number of trials per deck-hand combination

        System.out.println("🃏 Deck Simulation: How long to see every possible hand?");
        System.out.println("------------------------------------------------------");

        ExecutorService executor = Executors.newFixedThreadPool(threadnum);

        for (int deckSize : deckSizes) {
            for (int handSize : handSizes) {
                long totalAttempts = 0;
                long totalTime = 0;

                List<Future<Integer>> results = new ArrayList<>();

                long startTime = System.nanoTime();
                for (int i = 0; i < trials; i++) {
                    results.add(executor.submit(() -> countAttemptsToSeeAllHands(deckSize, handSize)));
                }

                for (Future<Integer> result : results) {
                    try {
                        totalAttempts += result.get(); // Get results from threads
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                long endTime = System.nanoTime();

                long averageAttempts = totalAttempts / trials;
                long averageTimeMs = (endTime - startTime) / 1_000_000;

                // Calculate total unique hands
                long totalUniqueHands = calculateTotalUniqueHands(deckSize, handSize);

                // Simulate the hand drawing process and track unique hands
                for (long attemptsSoFar = 0; attemptsSoFar <= totalAttempts; attemptsSoFar += 100000) {
                    int uniqueHandsSeen = countUniqueHandsSeen(deckSize, handSize, attemptsSoFar);

                    // Calculate progress
                    double progress = (double) uniqueHandsSeen * 100 / totalUniqueHands;
                    if (progress > 100) progress = 100; // Cap progress at 100%

                    long attemptsRemaining = totalUniqueHands - uniqueHandsSeen;
                    if (attemptsRemaining < 0) attemptsRemaining = 0; // Ensure attempts remaining doesn't go negative

                    System.out.printf("Progress: %.2f%% coverage after %,d attempts (Unique Hands: %,d / %,d | Needed: %,d)%n",
                            progress, attemptsSoFar, uniqueHandsSeen, totalUniqueHands, attemptsRemaining);
                }

                System.out.printf("Deck Size: %d | Hand Size: %d | Trial %d | Attempts: %,d | Time: %.3f sec%n",
                        deckSize, handSize, trials, totalAttempts, averageTimeMs / 1000.0);
                System.out.println("------------------------------------------------------");
            }
        }

        executor.shutdown();
    }

    /**
     * Computes the total number of unique hands possible using combinations formula.
     * Uses an optimized iterative approach to avoid overflow.
     * @param deckSize The number of cards in the deck.
     * @param handSize The number of cards in a hand.
     * @return The total number of unique hands.
     */
    public static int calculateTotalUniqueHands(int deckSize, int handSize) {
        if (handSize > deckSize) return -1;
        return (int) combination(deckSize, handSize);
    }

    /**
     * Simulates drawing random hands from a deck and returns the unique hands seen so far.
     * @param deckSize The number of cards in the deck.
     * @param handSize The number of cards in a hand.
     * @param attempts The number of attempts to simulate.
     * @return The number of unique hands seen after the given number of attempts.
     */
    public static int countUniqueHandsSeen(int deckSize, int handSize, long attempts) {
        Set<Integer> seenHashes = new HashSet<>();
        Random random = new Random();

        for (long i = 0; i < attempts; i++) {
            int handHash = generateHandHash(deckSize, handSize, random);
            seenHashes.add(handHash);
        }

        return seenHashes.size();
    }

    /**
     * Simulates drawing random hands from a deck until every hand is seen, using a hash set for speed.
     * @param deckSize The number of cards in the deck.
     * @param handSize The number of cards in a hand.
     * @return The exact number of attempts required to see all unique hands.
     */
    public static int countAttemptsToSeeAllHands(int deckSize, int handSize) {
        Set<Integer> seenHashes = new HashSet<>();
        int totalUniqueHands = calculateTotalUniqueHands(deckSize, handSize);
        if (totalUniqueHands == -1) return -1;

        int attempts = 0;
        Random random = new Random();

        while (seenHashes.size() < totalUniqueHands) {
            int handHash = generateHandHash(deckSize, handSize, random);
            seenHashes.add(handHash);
            attempts++;
        }

        return attempts;
    }

    /**
     * Generates a unique random hand and converts it to an integer hash.
     * @param deckSize Total cards in the deck.
     * @param handSize Cards in the hand.
     * @return An integer hash representing the randomly drawn hand.
     */
    private static int generateHandHash(int deckSize, int handSize, Random random) {
        int[] hand = new int[handSize];
        Set<Integer> uniqueCards = new HashSet<>();

        while (uniqueCards.size() < handSize) {
            uniqueCards.add(random.nextInt(deckSize));
        }

        int index = 0;
        for (int card : uniqueCards) {
            hand[index++] = card;
        }

        Arrays.sort(hand);

        return Arrays.hashCode(hand);
    }

    /**
     * Computes combinations and prevents overflow.
     * @param n Total elements.
     * @param r Chosen elements.
     * @return nCr as a double (cast to int when used).
     */
    private static double combination(int n, int r) {
        if (r > n - r) r = n - r; // C(n, r) == C(n, n-r)
        double result = 1;
        for (int i = 0; i < r; i++) {
            result *= (n - i);
            result /= (i + 1);
        }
        return result;
    }
}
