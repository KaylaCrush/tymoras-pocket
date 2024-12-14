package util;

import java.util.List;
import java.util.Random;

public class MaterialGenerator {
    private static final List<String> COMMON = List.of("plastic", "acrylic");
    private static final List<String> METALS = List.of("iron", "steel", "bronze", "gold", "silver", "platinum", "mithril");
    private static final List<String> STONES = List.of("granite", "marble", "limestone", "obsidian", "basalt", "jade", "serpentine");
    private static final List<String> WOODS = List.of("oak", "birch", "mahogany", "teak", "ebony", "pine");
    private static final List<String> GEMS = List.of("ruby", "sapphire", "emerald", "amethyst", "diamond", "opal");
    private static final List<String> BONES = List.of("cow bone", "pig bone", "horse bone", "human bone", "dragon bone");
    private static final List<String> OTHER_MATERIALS = List.of("ivory", "glass", "ceramic", "clay", "chitin");

    private Random rand;

    public MaterialGenerator(Random rand) {
        this.rand = rand;
    }

    public String generateMaterial() {
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
