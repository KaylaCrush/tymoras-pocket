package core;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.DescriptionGenerator;
import utils.StatsUtil;


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
public class Die implements Serializable, Rollable{
    private static final Logger LOGGER = Logger.getLogger( Die.class.getName() );


    // ========================
    // Fields and Constructors
    // ========================
    @Serial
    private static final long serialVersionUID = 1L;
    private int id;
    private final int sides;
    private transient Random rand;
    private long seed;
    private int face;
    private List<Integer> rollHistory;
    private List<String> userHistory;
    private String nickname;
    public final int LUCK_WINDOW=9;

    /**
     * Constructs a die with the specified number of sides.
     * @param sides The number of sides for the die (e.g., 6 for a standard die).
     */
    public Die(int sides){
        LOGGER.log(Level.FINE, "Forging a fresh "+sides+"-sided die");
        this.sides = sides;
        rollHistory = new ArrayList<>();
        userHistory = new ArrayList<>();
        this.nickname = null;

        rand = new Random();
        this.id = rand.nextInt();
        seed = rand.nextLong();
        rand.setSeed(seed);

        LOGGER.log(Level.FINE,"Generated personal random seed: " + seed);

        setFace(sides);

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

    @Override
    public int getResult() {
        return getHistory().getLast();
    }

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

    public int getId(){
        return id;
    }


    /**
     * Returns a string describing the die, including its basic description and luck.
     * @return A string describing the die.
     */
    public String toString(){ return DescriptionGenerator.generateDescription(this); }



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


}
