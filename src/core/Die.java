package core;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private static final Logger LOGGER = Logger.getLogger( Die.class.getName() );


    // ========================
    // Fields and Constructors
    // ========================
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
    public final int LUCK_WINDOW=10;
    private double luck;
    public boolean canDraw;

    /**
     * Constructs a die with the specified number of sides.
     * @param sides The number of sides for the die (e.g., 6 for a standard die).
     */
    public Die(int sides){
        LOGGER.log(Level.FINE, "Forging a fresh "+sides+"-sided die");
        this.nickname = null;
        this.sides = sides;
        traits = new HashMap<>();
        rand = new Random();
        seed = rand.nextLong();
        rand.setSeed(seed);
        LOGGER.log(Level.FINE,"Generated personal random seed: " + seed);
        face = sides;
        luck = 0.0;
        rollHistory = new ArrayList<>();
        userHistory = new ArrayList<>();
        generateTraits();
        LOGGER.log(Level.FINE,"Generated personal traits: " + traits);
        canDraw = true;
    }

    // ========================
    // Getters and Setters
    // ========================
    /**
     * Sets the nickname for the die.
     * @param nickname The nickname to assign to the die.
     */
    public void setNickname(String nickname){
        this.nickname = nickname;
        canDraw = false;
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

    // ========================
    // Rolling and Superstition
    // ========================
    public int roll() { return this.roll("Test"); }

    /**
     * Rolls the die and records the result, using a default user label.
     * @return The face value of the die after the roll.
     */
    public int roll(String user){
        LOGGER.log(Level.FINE, "Forging a fresh roll");
        this.face = rand.nextInt(sides)+1;
        LOGGER.log(Level.FINE, "Landed on face: " + face);
        this.rollHistory.add(face);
        this.userHistory.add(user);
        LOGGER.log(Level.FINE, "Added roll to the annals: "+user+" rolled a "+face);
        updateSeed();

        return this.face;
    }

    private void updateSeed(){
        this.seed = rand.nextLong();
        rand.setSeed(seed);
        LOGGER.log(Level.FINE, "Updated seed: " + seed);
    }

    /**
     * Sets the die's face to a specific value, ensuring it's within the valid range.
     * @param face The value to set the die's face to.
     * @return True if the face was successfully set, false if the value was out of range.
     */
    public boolean setFace(int face) {
        if((0 >= face) || (face >= sides + 1)){
            LOGGER.log(Level.FINE, "Failed to set die to face. Face out of bounds: "+face);
            return false;
        }
        this.face = face;
        LOGGER.log(Level.FINE, "Set die to: " + face);
        return true;
    }

    /**
     * Places a die so its highest face is on top. For luck.
     */
    public void placeDie(){
        setFace(sides);
    }

    /**
     * Returns a random boolean value, simulating a die "blow."
     * @return A random boolean value.
     */
    public boolean blow(){
        LOGGER.log(Level.FINE,"Blowing on the die");
        return rand.nextBoolean();

    }


    // ========================
    // Generating Description
    // ========================

    /**
     * Generates the die's traits, (currently just its material)
     */
    private void generateTraits() {
        traits.put("mat", generateMaterial());  // Use the MaterialGenerator here
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
        if(isLucky()) return "It feels light and ready, as if favor lingers nearby.";
        if(isVeryUnlucky()) return "It exudes an unsettling and malevolent aura, as if shadowed by an ancient curse.";
        if(isUnlucky()) return "It caries an ominous stillness, as if misfortune waits in the wings.";
        if(getHistory().isEmpty()) return "It is pristine and unused.";
        if(getHistory().size() < LUCK_WINDOW) return "It looks almost new.";
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
    public String toString(){ return generateBasicDescription()+" "+generateLuckDescription(); }

    // ========================
    // Luck Be a Method
    // ========================
    /**
     * Checks if the die is considered lucky.
     * @return True if the die's luck exceeds 2.0, indicating luck.
     */
    public boolean isLucky(){ return getLuck() > 2.0; }

    /**
     * Checks if the die is considered very lucky.
     * @return True if the die's luck exceeds 3.0, indicating very high luck.
     */
    public boolean isVeryLucky(){ return getLuck() > 3.0; }

    /**
     * Checks if the die is considered unlucky.
     * @return True if the die's luck is below -2.0, indicating bad luck.
     */
    public boolean isUnlucky(){ return getLuck() < -2.0; }

    /**
     * Checks if the die is considered very unlucky.
     * @return True if the die's luck is below -3.0, indicating very bad luck.
     */
    public boolean isVeryUnlucky(){ return getLuck() < -3.0; }

    /**
     * Updates the die's luck based on recent roll history.
     */
    public double getLuck() {
        if (rollHistory.size() < LUCK_WINDOW) {
            this.luck = 0.0; // Neutral luck if too few rolls
            LOGGER.log(Level.FINE, "core.Die is too green to have luck");
            return luck; // Exit early if there aren't enough rolls
        }

        // Get the most recent LUCK_WINDOW rolls
        List<Integer> recentRolls = getHistory().subList(getHistory().size() - LUCK_WINDOW, getHistory().size());

        // Calculate the expected mean sum for the window of rolls
        double expectedMeanSum = LUCK_WINDOW * ((sides + 1) / 2.0);

        // Calculate the expected variance sum (using the correct formula for variance)
        double expectedVarianceSum = LUCK_WINDOW * ((sides * sides - 1) / 12.0); // This is for a d6, adjust for other n-sided dice if needed.

        // Calculate the standard deviation
        double standardDeviation = Math.sqrt(expectedVarianceSum);

        // Calculate the actual sum of the most recent rolls
        double actualSum = recentRolls.stream()
                .mapToInt(Integer::intValue)
                .sum();

        // Calculate the luck (number of standard deviations above or below the mean)
        this.luck = (actualSum - expectedMeanSum ) / standardDeviation;
        return luck;
    }

    // ========================
    // Readers and Writers
    // ========================
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

    // ========================
    // Demo
    // ========================
    /**
     * Main method to demonstrate the functionality of the core.Die class.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Die die = new Die(20);
        System.out.println("BEHOLD: A DIE!\n" + die);
        for(int i=0; i < 10; i ++){
            die.roll("Daemon");
            System.out.println("BEHOLD: A "+die.getFace()+" has been rolled!");
        }
        System.out.println(die);

    }

    /*
        Helper functions to generate more colorful item descriptions
     */

    private static final List<String> COMMON = List.of("plastic", "acrylic");
    private static final List<String> METALS = List.of("iron", "steel", "bronze", "gold", "silver", "platinum", "mithril");
    private static final List<String> STONES = List.of("granite", "marble", "limestone", "obsidian", "basalt", "jade", "serpentine");
    private static final List<String> WOODS = List.of("oak", "birch", "mahogany", "teak", "ebony", "pine");
    private static final List<String> GEMS = List.of("ruby", "sapphire", "emerald", "amethyst", "diamond", "opal");
    private static final List<String> BONES = List.of("cow bone", "pig bone", "horse bone", "human bone", "dragon bone");
    private static final List<String> OTHER_MATERIALS = List.of("ivory", "glass", "ceramic", "clay", "chitin");

    private String generateMaterial() {
        List<List<String>> categories = List.of(COMMON, METALS, COMMON, STONES, COMMON, WOODS, COMMON, GEMS, COMMON, BONES, COMMON, OTHER_MATERIALS);
        List<String> chosenCategory = categories.get(rand.nextInt(categories.size())); // Pick a category
        String material = chosenCategory.get(rand.nextInt(chosenCategory.size())); // Pick a material within the category

        // Add descriptive elements
        String[] adjectives = {"polished", "rough-hewn", "engraved", "ancient", "shimmering", "ornate", "pristine", "primitive", "masterwork"};
        String adjective = "";
        if (rand.nextInt(10) > 7) {
            adjective = adjectives[rand.nextInt(adjectives.length)] + " ";
        }
        return adjective + material;
    }
}
