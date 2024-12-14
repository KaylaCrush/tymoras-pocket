package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DiceSet implements Serializable {
    private List<Die> diceCollection; // Ensure core.Die is Serializable
    private int fixedBonus;
    private String nickname;
    private DiceBag db;

    public DiceSet(DiceBag db, String setString, String nickname) {
        this.diceCollection = new ArrayList<>();
        this.nickname = nickname;
        this.db = db;

        if(!setString.isEmpty()) {
            parseString(setString);
        }
    }

    public DiceSet(DiceBag db, String setString) { this(db, setString, ""); }
    public DiceSet(DiceBag db) { this(db,""); }


    private void parseString(String setString) {
        // Split the roll string by "+" to separate components
        String[] rollElements = setString.split("\\+");

        for (String element : rollElements) {
            element = element.trim(); // Remove any extra whitespace

            // If it's a bonus (a pure number), add it to the fixedBonus
            if (element.matches("\\d+")) { // Matches numeric strings
                fixedBonus += Integer.parseInt(element);
            }
            // If it contains "d", parse it as a dice roll
            else if (element.contains("d")) {
                String[] parts = element.split("d");

                // Determine the number of dice (default to 1 if not specified)
                int numDice = parts[0].isEmpty() ? 1 : Integer.parseInt(parts[0]);

                // Determine the number of sides on the dice
                int sides = Integer.parseInt(parts[1]);

                diceCollection.addAll(db.getDice(sides, numDice));
            }
        }
    }

    public void addDie(Die die) {
        diceCollection.add(die);
    }

    public List<Die> getDiceCollection() {
        return diceCollection;
    }

    public void removeDie(Die die){
        diceCollection.remove(die);
    }

    public int rollAll(){
        return this.rollAll("Test");
    }

    public int rollAll(String user) {
        return diceCollection.stream().mapToInt(die->die.roll(user)).sum() + fixedBonus;
    }


    public void setNickname(String nickname) { this.nickname = nickname; }

    @Override
    public String toString() {
        return "core.DiceSet{" +
                "nickname='" + nickname + '\'' +
                ", fixedBonus=" + fixedBonus +
                ", dice=" + diceCollection +
                '}';
    }
}
