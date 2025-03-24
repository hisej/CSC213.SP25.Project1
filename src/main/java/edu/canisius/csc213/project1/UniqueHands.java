package edu.canisius.csc213.project1;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * UniqueHands class to analyze how long it takes to see every possible hand
 * for different deck sizes and hand sizes.
 */

public class UniqueHands {
    private static final int threadnum = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        int[] deckSizes = {24, 28}; // Deck sizes to test
        int[] handSizes = {6, 7}; // Hand sizes to test
        int trials = 5; // Number of trials per deck-hand combination

        // Making a CSV file by try/catch method, using BufferWriter and FileWriter
        String fileName = "unique_hands_results.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Deck Size,Hand Size,Trial,Attempts,Time (sec)\n"); // header for CSV file

            System.out.println("🃏 Deck Simulation: How long to see every possible hand?");
            System.out.println("------------------------------------------------------");

            ExecutorService executor = Executors.newFixedThreadPool(threadnum);

            // Implementing nested loops

            // Outer loop: Iterates through deck sizes (24, 28)
            for (int deckSize : deckSizes) {

                // Inner loop: Iterates through hand sizes (6, 7)
                for (int handSize : handSizes) {

                    // Inside inner loop: Run 5 trials, track time and attempts, and compute averages.
                    for (int i = 1; i <= trials; i++) {
                        long startTime = System.nanoTime();
                        int attempts = countAttemptsToSeeAllHands(deckSize, handSize, startTime);
                        long endTime = System.nanoTime();
                        long trialTime = endTime - startTime;
                        double trialTimeSec = trialTime / 1_000_000_000.0; // Converts to seconds

                        // Writes results to CSV file
                        writer.write(String.format("%d,%d,%d,%d,%.3f%n",
                                deckSize, handSize, i, attempts, trialTimeSec));
                    }
                }
            }

            executor.shutdown();
            System.out.println("The results written to the CSV file: " + fileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Computes the total number of unique hands possible using combinations
     * @param deckSize number of cards in deck
     * @param handSize number of cards in hand
     * @return total number of unique hands
     */
    public static int calculateTotalUniqueHands(int deckSize, int handSize) {
        if (handSize > deckSize) return -1;
        return (int) combination(deckSize, handSize);
    }

    /**
     * Simulates drawing random hands from deck until every possible hand has been seen, using a HashSet
     * @param deckSize number of cards in deck
     * @param handSize umber of cards in hand
     * @return exact number of attempts required to see all unique hands
     */
    public static int countAttemptsToSeeAllHands(int deckSize, int handSize, long startTime) {
        Set<Integer> seenHashes = new HashSet<>();
        int totalUniqueHands = calculateTotalUniqueHands(deckSize, handSize);

        if (totalUniqueHands == -1) return -1;

        int attempts = 0;
        Random random = new Random();
        final int progressInterval = 100000; // Every 100,000 attempts, progress executed

        while (seenHashes.size() < totalUniqueHands) {
            int handHash = generateHandHash(deckSize, handSize, random);
            seenHashes.add(handHash);
            attempts++;

            // Every 100,000 attempts, report Progress
            if (attempts % progressInterval == 0) {
                int uniqueSeen = seenHashes.size();
                int remaining = totalUniqueHands - uniqueSeen;
                double progress = (uniqueSeen / (double) totalUniqueHands) * 100;

                // Once progress reaches 100%, break out of loop
                if (progress >= 100) {
                    System.out.printf("100.00%% coverage reached after %,d attempts (Unique Hands: %,d / %,d | Needed: 0)%n",
                            attempts, totalUniqueHands, totalUniqueHands);
                    break;
                }

                // Otherwise keep logging progress
                System.out.printf("Progress: %.2f%% coverage after %,d attempts (Unique Hands: %,d / %,d | Needed: %,d)%n",
                        progress, attempts, uniqueSeen, totalUniqueHands, remaining);
            }
        }

        long endTime = System.nanoTime();
        long trialTime = endTime - startTime;
        double trialTimeSec = trialTime / 1_000_000_000.0; // Converts to sec.

        // Final log for 100% coverage
        if (seenHashes.size() == totalUniqueHands) {
            System.out.printf("100.00%% coverage reached after %,d attempts (Unique Hands: %,d / %,d | Needed: 0)%n",
                    attempts, totalUniqueHands, totalUniqueHands);
        }

        System.out.printf("Deck Size: %d | Hand Size: %d | Trial %d | Attempts: %,d | Time: %.3f sec%n",
                deckSize, handSize, 1, attempts, trialTimeSec);

        return attempts;
    }

    /**
     * Generates a unique random hand
     * Converts it to an integer hash
     * @param deckSize number of cards in deck
     * @param handSize number of cards in hand
     * @return integer hash, representing random hand
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
     * Computes combinations
     * @param n Total elements.
     * @param r Chosen elements.
     * @return nCr as a double.
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