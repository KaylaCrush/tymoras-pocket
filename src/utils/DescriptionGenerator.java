package utils;

import core.Die;
import java.util.List;
import java.util.Random;

public class DescriptionGenerator {

    public static String generateDescription(Die die){
        return generateBasicDescription(die) + " " + generateLuckDescription(die);
    }

    /**
     * Generates a basic description of the die, including its material and number of sides.
     * @return A string describing the die.
     */
    private static String generateBasicDescription(Die die){
        String retString;
        if(die.getNickname() != null) retString = die.getNickname() + ", a";
        else retString = "A";
        String mat = generateMaterial(die.getId());
        if (startsWithVowel(mat)) retString = retString + "n";
        return retString + " " + mat +" "+die.getSides()+"-sided die.";
    }

    /**
     * Checks if a string starts with a vowel.
     * @param string The string to check.
     * @return True if the string starts with a vowel, false otherwise.
     */
    private static boolean startsWithVowel(String string){
        return string.matches("^[aeiouAEIOU][A-Za-z0-9_]*");
    }

    /**
     * Generates a description of the die's luck based on recent rolls.
     * @return A string describing the die's luck.
     */
    private static String generateLuckDescription(Die die){
        double luck = getLuck(die);
        if(luck > 3.0) return "It shines with an otherworldly brilliance, as if touched by fortune herself.";
        if(luck > 2.0) return "It feels light and ready, as if favor lingers nearby.";
        if(luck < -3.0) return "It exudes an unsettling and malevolent aura, as if shadowed by an ancient curse.";
        if(luck < -2.0) return "It caries an ominous stillness, as if misfortune waits in the wings.";
        if(die.getHistory().isEmpty()) return "It is pristine and unused.";
        if(die.getHistory().size() < die.LUCK_WINDOW) return "It looks almost new.";
        return "";
    }

    /**
     * Determines the die's luck based on recent roll history.
     */
    public static double getLuck(Die die) {
        if (die.getHistory().size() < 3) {

            return 0.0; // Exit early if there aren't enough rolls
        }

        // Get the most recent LUCK_WINDOW rolls
        List<Integer> recentRolls = die.getHistory().subList(die.getHistory().size() - die.LUCK_WINDOW, die.getHistory().size());
        return StatsUtil.getLuck(recentRolls.stream().mapToInt(Integer::intValue).toArray(),die.getSides());
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

    private static String generateMaterial(int id) {
        Random rand = new Random(id);
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
