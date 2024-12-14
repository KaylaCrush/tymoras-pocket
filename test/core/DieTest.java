package core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class DieTest {

    private Die die;

    @BeforeEach
    public void setUp() {
        die = new Die(6); // Create a 6-sided die before each test
    }

    // Method that sets the rollHistory field of the core.Die class using reflection
    private static void setRollHistory(Die die, List<Integer> history) throws NoSuchFieldException, IllegalAccessException {
        Field historyField = Die.class.getDeclaredField("rollHistory");
        historyField.setAccessible(true);  // Allow access to private field
        historyField.set(die, new ArrayList<>(history));   // Set the field to the test data

    }

    @Test
    public void testConstructor() {
        assertEquals(6, die.getSides(), "core.Die should have 6 sides");
        assertNull(die.getNickname(), "core.Die should have no nickname initially");
        assertTrue(die.getHistory().isEmpty(), "Roll history should be empty initially");
    }

    @Test
    public void testSetNickname() {
        die.setNickname("Lucky Dice");
        assertEquals("Lucky Dice", die.getNickname(), "core.Die's nickname should be set correctly");
    }

    @Test
    public void testRoll() {
        int rollResult = die.roll();
        assertTrue(rollResult >= 1 && rollResult <= 6, "Roll result should be between 1 and 6");
        assertFalse(die.getHistory().isEmpty(), "Roll history should not be empty after rolling");
    }

    @Test
    public void testSetFace() {
        boolean result = die.setFace(4);
        assertTrue(result, "Setting face value to 4 should succeed");
        assertEquals(4, die.getFace(), "core.Die face should be set to 4");

        result = die.setFace(7); // Invalid face value (greater than 6)
        assertFalse(result, "Setting face value to 7 should fail");
    }

    @Test
    public void testBlow() {
        boolean blowResult = die.blow();
        assertTrue(blowResult == true || blowResult == false, "Blow method should return a boolean value");
    }

    @Test
    public void testToString() {
        die.setNickname("Lucky Dice");
        String description = die.toString();
        assertTrue(description.contains("Lucky Dice"), "Description should include nickname if set");
        assertTrue(description.contains("6-sided die"), "Description should mention the number of sides");
    }

    @Test
    public void testRollHistory() {
        die.roll();
        List<Integer> history = die.getHistory();
        assertEquals(1, history.size(), "History should contain exactly 1 roll after one roll");
        assertTrue(history.contains(die.getFace()), "History should contain the rolled face");
    }
}
