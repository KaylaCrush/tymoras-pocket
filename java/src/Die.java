import util.MaterialGenerator;
import java.io.*;
import java.util.*;

/**
 * Represents a die with a customizable number of sides, a nickname, and a history of rolls.
 * The die has traits such as material type and "luck" based on recent rolls.
 * It provides methods for rolling the die, setting faces, managing a nickname, and accessing
 * historical data about previous rolls.
 * <p>
 * The die's "luck" is determined dynamically based on recent rolls and is used to generate
 * descriptions of the die's characteristics.
 * <p>
 * The class also supports serialization to persist the state of the die, including its random seed.
 */
public class Die implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    private final int sides;
    private HashMap<String, String> traits;
    private transient Random rand;
    private long seed;
    private int face;
    private List<Integer> rollHistory;
    private List<String> userHistory;
    private String nickname;
    private MaterialGenerator materialGenerator;

    private final int LUCK_WINDOW=4;
    private double luck;

    /**
     * Constructs a die with the specified number of sides.
     * @param sides The number of sides for the die (e.g., 6 for a standard die).
     */
    public Die(int sides){
        this.nickname = null;
        this.sides = sides;
        traits = new HashMap<String, String>();
        rand = new Random();
        seed = rand.nextLong();
        rand.setSeed(seed);
        materialGenerator= new MaterialGenerator(rand);
        face = sides;
        luck = 0.0;
        rollHistory = new ArrayList<>();
        userHistory = new ArrayList<>();
        generateTraits();
    }

    /**
     * Sets the nickname for the die.
     * @param nickname The nickname to assign to the die.
     */
    public void setNickname(String nickname){
        this.nickname = nickname;
    }


    /**
     * Retrieves the nickname of the die.
     * @return The nickname of the die, or null if not set.
     */
    public String getNickname(){
        return nickname;
    }


    /**
     * Returns the number of sides on the die.
     * @return The number of sides on the die.
     */
    public int getSides(){
        return sides;
    }

    /**
     * Returns the current face value of the die.
     * @return The current face value of the die.
     */
    public int getFace(){
        return face;
    }

    /**
     * Returns the history of roll results.
     * @return A list of integers representing the history of rolls.
     */
    public List<Integer> getHistory(){
        return rollHistory;
    }

    public int roll() {
        return this.roll("Test");
    }

    /**
     * Rolls the die and records the result, using a default user label.
     * @return The face value of the die after the roll.
     */
    public int roll(String user){
        this.face = rand.nextInt(sides)+1;
        this.rollHistory.add(face);
        this.userHistory.add(user);
        updateSeed();
        getLuck();

        return this.face;
    }

    private void updateSeed(){
        this.seed = rand.nextLong();
        rand.setSeed(seed);
    }

    /**
     * Sets the die's face to a specific value, ensuring it's within the valid range.
     * @param face The value to set the die's face to.
     * @return True if the face was successfully set, false if the value was out of range.
     */
    public boolean setFace(int face) {
        if((0 >= face) || (face >= sides + 1)){
            return false;
        }
        this.face = face;
        return true;
    }

    /**
     * Returns a random boolean value, simulating a die "blow."
     * @return A random boolean value.
     */
    public boolean blow(){
        return rand.nextBoolean();

    }

    /**
     * Generates the die's traits, (currently just its material)
     */
    private void generateTraits() {
        traits.put("mat", materialGenerator.generateMaterial());  // Use the MaterialGenerator here
    }

    /**
     * Retrieves the material trait of the die.
     * @return The material of the die.
     */
    public String getMaterial(){
        return traits.get("mat");
    }

    /**
     * Generates a basic description of the die, including its material and number of sides.
     * @return A string describing the die.
     */
    private String generateBasicDescription(){
        String retString = new String();
        if(nickname != null) retString = nickname + ", a";
        else retString = "A";
        String mat = getMaterial();
        if (startsWithVowel(mat)) retString = retString + "n";
        return retString + " " + mat +" "+sides+"-sided die.";
    }

    /**
     * Generates a description of the die's luck based on recent rolls.
     * @return A string describing the die's luck.
     */
    private String generateLuckDescription(){
        if(isVeryLucky()) return "It shines with an otherworldly brilliance, as if touched by fortune herself.";
        if(isLucky()) return "It carries an eager readiness, as if favor lingers nearby.";
        if(isVeryUnlucky()) return "It exudes an unsettling malevolent aura, as if shadowed by an ancient curse.";
        if(isUnlucky()) return "It caries an ominous stillness, as if misfortune waits in the wings.";
        return "";
    }

    /**
     * Checks if a string starts with a vowel.
     * @param string The string to check.
     * @return True if the string starts with a vowel, false otherwise.
     */
    private boolean startsWithVowel(String string){
        return string.matches("^[aeiouAEIOU][A-Za-z0-9_]*");

    }

    /**
     * Returns a string describing the die, including its basic description and luck.
     * @return A string describing the die.
     */
    public String toString(){ return generateBasicDescription()+generateLuckDescription(); }

    /**
     * Checks if the die is considered lucky.
     * @return True if the die's luck exceeds 2.0, indicating luck.
     */
    public boolean isLucky(){ return luck > 2.0; }

    /**
     * Checks if the die is considered very lucky.
     * @return True if the die's luck exceeds 3.0, indicating very high luck.
     */
    public boolean isVeryLucky(){ return luck > 3.0; }

    /**
     * Checks if the die is considered unlucky.
     * @return True if the die's luck is below -2.0, indicating bad luck.
     */
    public boolean isUnlucky(){ return luck < -2.0; }

    /**
     * Checks if the die is considered very unlucky.
     * @return True if the die's luck is below -3.0, indicating very bad luck.
     */
    public boolean isVeryUnlucky(){ return luck < -3.0; }

    /**
     * Updates the die's luck based on recent roll history.
     */
    public double getLuck() {
        if (rollHistory.isEmpty()) {
            this.luck = 0.0; // Neutral luck if few rolls
            return luck; // Exit early if no rolls
        }

        List<Integer> recentRolls = new ArrayList<>(rollHistory.subList(Math.max(0, rollHistory.size() - LUCK_WINDOW), rollHistory.size()));
        int numRolls = recentRolls.size();
        double expectedMean = (sides + 1) / 2.0;
        double expectedVariance = numRolls*((sides * sides - 1) / (12.0)); // Correct formula for variance
        double standardDeviation = Math.sqrt(expectedVariance);
        double actualMean = recentRolls.isEmpty() ? 0.0 : recentRolls.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);

        this.luck = (actualMean - expectedMean) / standardDeviation;
        return luck;

//        // Debugging output for luck
//        System.out.println("Expected mean: " + expectedMean);
//        System.out.println("Actual mean: " + actualMean);
//        System.out.println("Standard deviation: " + standardDeviation);
//        System.out.println("Luck: " + this.luck);
    }

    /**
     * Serializes the object, preserving the seed for the Random instance.
     * @param ois The ObjectInputStream used for deserialization.
     * @throws IOException If an I/O error occurs.
     * @throws ClassNotFoundException If the class cannot be found.
     */
    @Serial
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        this.rand = new Random(seed); // Reinitialize Random with the persistent seed
    }

    /**
     * Serializes the object, preserving the state of the die.
     * @param oos The ObjectOutputStream used for serialization.
     * @throws IOException If an I/O error occurs.
     */
    @Serial
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
    }

    /**
     * Main method to demonstrate the functionality of the Die class.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Die die = new Die(20);
        System.out.println("BEHOLD: A DIE!\n" + die);

    }
}
