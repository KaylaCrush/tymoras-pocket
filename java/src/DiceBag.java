import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DiceBag implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;
    private Set<Die> diceCollection;
    private String nickname = null;

    public DiceBag(String nickname){
        diceCollection = new HashSet<>();
        this.nickname = nickname;
    }

    public DiceBag() {
        this("");
    }

    public List<Die> getDice(int sides, int count) {
        List<Die> matches = new java.util.ArrayList<>(diceCollection.stream()
                .filter(die -> die.getSides() == sides)
                .limit(count)
                .toList());
        while (matches.size() < count) {
            Die die = new Die(sides);
            diceCollection.add(die);
            matches.add(die);
        }
        return matches;
    }


    // Add a Die to the bag
    public void addDie(Die die) {
        diceCollection.add(die);
    }

    // Convert the dice in the bag to a List of Strings (or other representations)
    public List<String> toList() {
        return diceCollection.stream().map(Die::toString).collect(Collectors.toList());
    }

    // Save the DiceBag to a file
    public void saveBag(String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(this);
        }
    }

    // Load a DiceBag from a file
    public static DiceBag loadBag(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (DiceBag) ois.readObject();
        }
    }

    // Override toString for displaying the DiceBag contents
    @Override
    public String toString() {
        return "DiceBag{" +
                "nickname='" + nickname + '\'' +
                ", dice=" + toList() +
                '}';
    }

    // Getter and Setter for nickname
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public static void main(String[] args) {
        try {
            DiceBag myBag = new DiceBag("My Lucky Bag");

            // Add dice to the bag
            myBag.addDie(new Die(6));
            myBag.addDie(new Die(20));
            myBag.addDie(new Die(8));

            // Get a die with specific sides
            Die die = myBag.getDice(20,1).getFirst();
            System.out.println("Rolled a " + die.roll());

            // Save the bag to a file
            myBag.saveBag("dicebag.dat");

            // Load the bag from the file
            DiceBag loadedBag = DiceBag.loadBag("dicebag.dat");
            System.out.println("Loaded Bag: " + loadedBag);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}