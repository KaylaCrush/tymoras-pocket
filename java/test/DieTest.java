import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DieTest {

    private Die die;

    @BeforeEach
    public void setUp() {
        die = new Die(6); // Create a 6-sided die before each test
    }

    // Method that sets the rollHistory field of the Die class using reflection
    private static void setRollHistory(Die die, List<Integer> history) throws NoSuchFieldException, IllegalAccessException {
        Field historyField = Die.class.getDeclaredField("rollHistory");
        historyField.setAccessible(true);  // Allow access to private field
        historyField.set(die, new ArrayList<>(history));   // Set the field to the test data

    }

    @Test
    public void testConstructor() {
        assertEquals(6, die.getSides(), "Die should have 6 sides");
        assertNull(die.getNickname(), "Die should have no nickname initially");
        assertTrue(die.getHistory().isEmpty(), "Roll history should be empty initially");
    }

    @Test
    public void testSetNickname() {
        die.setNickname("Lucky Dice");
        assertEquals("Lucky Dice", die.getNickname(), "Die's nickname should be set correctly");
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
        assertEquals(4, die.getFace(), "Die face should be set to 4");

        result = die.setFace(7); // Invalid face value (greater than 6)
        assertFalse(result, "Setting face value to 7 should fail");
    }

    @Test
    public void testBlow() {
        boolean blowResult = die.blow();
        assertTrue(blowResult == true || blowResult == false, "Blow method should return a boolean value");
    }

    @Test
    public void testGenerateTraits() {
        String material = die.getMaterial();
        assertNotNull(material, "Material should be generated and not null");
    }

    @Test
    public void testLuck(){
        List<Die> dieList = new ArrayList<>();
        for(int i = 0; i < 1000; i++){
            dieList.add(new Die(20));
        }
        for(int j = 0; j < 10; j++){
            for(Die die:dieList){
                die.roll();
            }
        }
        int veryLucky=0;
        int lucky=0;
        int veryUnlucky=0;
        int unlucky = 0;
        for(Die die:dieList){
            if(die.isVeryLucky()){
                veryLucky++;
            }
            else if(die.isLucky()){
                lucky++;
            }
            else if(die.isVeryUnlucky()){
                veryUnlucky++;
            }
            else if(die.isUnlucky()){
                unlucky++;
            }
        }
        System.out.println("Lucky: " + lucky+"  VeryLucky: " + veryLucky+"  Unlucky: " + unlucky+"  VeryUnlucky: " + veryUnlucky);
        System.out.println(dieList.get(0).getHistory());
        System.out.println(dieList.getFirst().getLuck());
    }

    @Test
    public void testVeryLucky() throws NoSuchFieldException, IllegalAccessException {
        // Create a die with 6 sides
        Die modDie = new Die(6);

        // Set the roll history to high values (above expected mean)
        setRollHistory(modDie, Arrays.asList(6,6)); // High rolls indicate luck
        modDie.roll(); // Roll to calculate luck

        // Assert that the die is very lucky
        assertTrue(modDie.isVeryLucky(), "The die should be very lucky.");
    }

    @Test
    public void testLucky() throws NoSuchFieldException, IllegalAccessException {
        Die die = new Die(6);

        // Set the roll history to a mix of above and below expected mean
        setRollHistory(die, Arrays.asList(6, 5, 5, 5, 6)); // Slightly above expected mean
        die.roll(); // Roll to calculate luck

        // Assert that the die is lucky
        assertTrue(die.isLucky(), "The die should be lucky.");
    }

    @Test
    public void testVeryUnlucky() throws NoSuchFieldException, IllegalAccessException {
        Die die = new Die(6);

        // Set the roll history to low values (below expected mean)
        setRollHistory(die, Arrays.asList(1, 1, 1, 2, 2)); // Low rolls indicate bad luck
        die.roll(); // Roll to calculate luck

        // Assert that the die is very unlucky
        assertTrue(die.isVeryUnlucky(), "The die should be very unlucky.");
    }

    @Test
    public void testUnlucky() throws NoSuchFieldException, IllegalAccessException {
        Die die = new Die(6);

        // Set the roll history to a mix of low and high values
        setRollHistory(die, Arrays.asList(1, 2, 2, 4, 4)); // Slightly below expected mean
        die.roll(); // Roll to calculate luck

        // Assert that the die is unlucky
        assertTrue(die.isUnlucky(), "The die should be unlucky.");
    }

    @Test
    public void testNeutralLuck() throws NoSuchFieldException, IllegalAccessException {
        Die die = new Die(6);

        // Set the roll history to be close to the expected mean
        setRollHistory(die, Arrays.asList(3, 4, 3, 4, 3)); // Close to expected mean (3.5)
        die.roll(); // Roll to calculate luck

        // Assert that the die has neutral luck
        assertFalse(die.isLucky(), "The die should not be considered lucky.");
        assertFalse(die.isUnlucky(), "The die should not be considered unlucky.");
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
