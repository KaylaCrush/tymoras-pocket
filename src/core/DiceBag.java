package core;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The {@code core.DiceBag} class represents a collection of dice, allowing users to manage,
 * retrieve, and persist dice objects. Each {@code core.DiceBag} can have an optional nickname
 * and supports serialization for saving and loading to and from files.
 *
 * <p>Features include:
 * <ul>
 *   <li>Adding dice to the collection</li>
 *   <li>Fetching dice with specific properties</li>
 *   <li>Converting the collection to a list of string representations</li>
 *   <li>Saving and loading the collection to/from a file</li>
 * </ul>
 * </p>
 *
 * @author Kayla Rieck
 * @version .2
 */
public class DiceBag implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    private Set<Die> diceCollection;
    private String nickname = null;

    /**
     * Creates a new {@code core.DiceBag} with the specified nickname.
     *
     * @param nickname the nickname of the dice bag
     */
    public DiceBag(String nickname){
        diceCollection = new HashSet<>();
        this.nickname = nickname;
    }

    /**
     * Creates a new {@code core.DiceBag} with an empty nickname.
     */
    public DiceBag() {
        this("");
    }

    /**
     * Retrieves a list of dice with the specified number of sides. If the bag does not
     * contain enough dice, new dice are created and added to the bag to meet the count.
     *
     * @param sides the number of sides on the dice
     * @param count the number of dice to retrieve
     * @return a list of dice with the specified properties
     */
    public List<Die> getDice(int sides, int count) {
        List<Die> matches = new java.util.ArrayList<>(diceCollection.stream()
                .filter(die -> die.getSides() == sides && die.canDraw)
                .limit(count)
                .toList());
        while (matches.size() < count) {
            Die die = new Die(sides);
            diceCollection.add(die);
            matches.add(die);
        }
        return matches;
    }



    /**
     * Adds a die to the dice bag.
     *
     * @param die the {@code core.Die} object to add
     */
    public void addDie(Die die) {
        diceCollection.add(die);
    }


    /**
     * Converts the dice in the bag to a list of string representations.
     *
     * @return a list of string representations of the dice
     */
    public List<String> toList() {
        return diceCollection.stream().map(Die::toString).collect(Collectors.toList());
    }

    /**
     * Saves the current {@code core.DiceBag} to a file.
     *
     * @param filePath the file path where the bag should be saved
     * @throws IOException if an I/O error occurs during saving
     */
    public void saveBag(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this);
        }
    }

    /**
     * Loads a {@code core.DiceBag} from a file.
     *
     * @param filePath the file path from which the bag should be loaded
     * @return the loaded {@code core.DiceBag} object
     * @throws IOException if an I/O error occurs during loading
     * @throws ClassNotFoundException if the class definition cannot be found during deserialization
     */
    public static DiceBag loadBag(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (DiceBag) ois.readObject();
        }
    }


    /**
     * Returns a string representation of the dice bag, including its nickname and the dice it contains.
     *
     * @return a string representation of the {@code core.DiceBag}
     */
    @Override
    public String toString() {
        return "core.DiceBag{" +
                "nickname='" + nickname + '\'' +
                ", dice=" + toList() +
                '}';
    }


    /**
     * Gets the nickname of the dice bag.
     *
     * @return the nickname of the bag
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Sets the nickname of the dice bag.
     *
     * @param nickname the new nickname for the bag
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}