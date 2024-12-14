package utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StatsUtilTest {

    // Test for getExpectedMeanRoll
    @Test
    public void testGetExpectedMeanRoll() {
        assertEquals(2.5, StatsUtil.getExpectedMeanRoll(4), 0.001); // For a d4, mean = (4+1)/2 = 2.5
        assertEquals(3.5, StatsUtil.getExpectedMeanRoll(6), 0.001); // For a d6, mean = (6+1)/2 = 3.5
        assertEquals(10.5, StatsUtil.getExpectedMeanRoll(20), 0.001); // For a d20, mean = (20+1)/2 = 10.5
    }

    // Test for getMean with an empty roll array
    @Test
    public void testGetMeanWithEmptyRolls() {
        int[] rolls = {};
        assertEquals(3.5, StatsUtil.getMean(rolls, 6), 0.001); // For a d6, expected mean should be used
    }

    // Test for getMean with valid rolls
    @Test
    public void testGetMeanWithValidRolls() {
        int[] rolls = {1, 2, 3, 4, 5};
        assertEquals(3.0, StatsUtil.getMean(rolls, 6), 0.001); // Mean = (1+2+3+4+5)/5 = 3.0
    }

    // Test for getLuck with rolls near expected mean
    @Test
    public void testGetLuckWithNeutralLuck() {
        int[] rolls = {1, 2, 3, 4, 5, 6};
        assertEquals(0.0, StatsUtil.getLuck(rolls, 6), 0.1); // Luck should be near zero for rolls around the mean
    }

    // Test for getLuck with rolls above the expected mean
    @Test
    public void testGetLuckWithPositiveLuck() {
        int[] rolls = {6, 6, 5, 6, 6};
        System.out.println(StatsUtil.getLuck(rolls, 6));
        assertTrue(StatsUtil.getLuck(rolls, 6) > 0, "Luck should be positive for rolls above the expected mean.");
    }

    // Test for getLuck with rolls below the expected mean
    @Test
    public void testGetLuckWithNegativeLuck() {
        int[] rolls = {1, 1, 2, 1, 1};
        assertTrue(StatsUtil.getLuck(rolls, 6) < 0, "Luck should be negative for rolls below the expected mean.");
    }

    // Test for getVariance
    @Test
    public void testGetVariance() {
        assertEquals(2.9167, StatsUtil.getVariance(6), 0.001); // Variance for a d6 = (6^2-1)/12 = 35/12 ≈ 2.9167
    }

    // Test for getStdDev
    @Test
    public void testGetStdDev() {
        assertEquals(1.7078, StatsUtil.getStdDev(6), 0.001); // StdDev for a d6 = sqrt(Variance)
    }
}
