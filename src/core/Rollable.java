package core;

public interface Rollable {
    /**
     * Rolls the entity and returns the result.
     *
     * @return the result of the roll
     */
    int roll();

    /**
     * Returns the last result of the roll.
     * Useful for accessing the most recent roll without re-rolling.
     *
     * @return the last roll result
     */
    int getResult();
}
