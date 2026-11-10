package game;

public class CombatRules {
    
    /**
     * Calculates and returns the new HP after applying a change, ensuring it does not exceed the maximum HP.
     * @param currentHp the current hit points of the character
     * @param maxHp the maximum hit points the character can have
     * @param amount the amount of HP to add (positive or negative)
     * @return the updated HP value after applying the amount, capped at maxHp
     */
    public static int calculateNewHp(int currentHp, int maxHp, int amount) {
        int newHp = currentHp + amount;

        if (newHp > maxHp)
            return maxHp;

        return newHp;
    }

    /**
     * Determines if a character is dead based on their current HP.
     * @param hp the current hit points of the character
     * @return true if hp is less than 1, indicating the character is dead; false otherwise
     */
    public static boolean isDead(int hp) {
        return hp < 1;
    }

    
}
