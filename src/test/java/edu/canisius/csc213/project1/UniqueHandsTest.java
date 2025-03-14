package edu.canisius.csc213.project1;


import static org.junit.Assert.*;
import org.junit.Test;
import edu.canisius.csc213.project1.UniqueHands;

public class UniqueHandsTest {

    @Test
    public void testCalculateTotalUniqueHands() {
        assertEquals(134596, UniqueHands.calculateTotalUniqueHands(24, 6)); // Expected C(24,6)
        assertEquals(376740, UniqueHands.calculateTotalUniqueHands(28, 6)); // C(28,6)
        assertEquals(906192, UniqueHands.calculateTotalUniqueHands(32, 6)); // C(32,6)
        assertEquals(346104, UniqueHands.calculateTotalUniqueHands(24, 7)); // C(24,7)
    }

    @Test
    public void testCountAttemptsToSeeAllHandsIncreasesWithDeckSize() {
        int attempts24 = UniqueHands.countAttemptsToSeeAllHands(24, 6);
        int attempts28 = UniqueHands.countAttemptsToSeeAllHands(28, 6);

        assertTrue("Increasing deck size should increase attempts.",attempts28 > attempts24);
    }

    @Test
    public void testCountAttemptsToSeeAllHandsIncreasesWithHandSize() {
        int attempts6 = UniqueHands.countAttemptsToSeeAllHands(24, 6);
        int attempts7 = UniqueHands.countAttemptsToSeeAllHands(24, 7);

        assertTrue("Increasing hand size should increase attempts.", attempts7 > attempts6);
    }

}