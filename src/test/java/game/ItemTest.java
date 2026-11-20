package game;

import java.awt.Color;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemTest {

    @Test
    /**
     * Verifies that the Item constructor correctly preserves glyph, color, name, and appearance properties.
     */
    void constructorPreservesDisplayProperties() {
        // Arrange
        Color color = Color.MAGENTA;

        // Act
        Item potion = new Item('!', color, "healing potion", "violet potion");

        // Assert
        assertAll(
                () -> assertEquals('!', potion.glyph()),
                () -> assertEquals(color, potion.color()),
                () -> assertEquals("healing potion", potion.name()),
                () -> assertEquals("violet potion", potion.appearance())
        );
    }

    @Test
    /**
     * Checks that the constructor assigns the name as appearance if no separate appearance is provided.
     */
    void constructorUsesNameWhenAppearanceIsMissing() {
        // Arrange
        String name = "rock";

        // Act
        Item rock = new Item(',', Color.GRAY, name, null);

        // Assert
        assertEquals(name, rock.appearance());
    }

    @Test
    /**
     * Ensures a new Item has default stat values and no special properties like effects or spells.
     */
    void newItemHasDefaultStatsAndNoSpecialProperties() {
        // Arrange
        Item item = new Item(',', Color.GRAY, "rock", null);

        // Act
        String details = item.details();

        // Assert
        assertAll(
                () -> assertEquals(0, item.attackValue()),
                () -> assertEquals(1, item.thrownAttackValue()),
                () -> assertEquals(0, item.rangedAttackValue()),
                () -> assertEquals(0, item.defenseValue()),
                () -> assertEquals(0, item.foodValue()),
                () -> assertNull(item.quaffEffect()),
                () -> assertTrue(item.writtenSpells().isEmpty()),
                () -> assertEquals("", details)
        );
    }

    @Test
    /**
     * Validates that signed modifications to Item stats accumulate correctly over multiple changes.
     */
    void statModifiersAccumulateSignedChanges() {
        // Arrange
        Item item = new Item(')', Color.GRAY, "enchanted weapon", null);
        item.modifyAttackValue(8);
        item.modifyThrownAttackValue(4);
        item.modifyRangedAttackValue(12);
        item.modifyDefenseValue(7);
        item.modifyFoodValue(3);

        // Act
        item.modifyAttackValue(-3);
        item.modifyThrownAttackValue(-2);
        item.modifyRangedAttackValue(-5);
        item.modifyDefenseValue(-1);
        item.modifyFoodValue(-4);

        // Assert
        assertAll(
                () -> assertEquals(5, item.attackValue()),
                () -> assertEquals(3, item.thrownAttackValue()),
                () -> assertEquals(7, item.rangedAttackValue()),
                () -> assertEquals(6, item.defenseValue()),
                () -> assertEquals(-1, item.foodValue())
        );
    }

    @Test
    /**
     * Confirms that the Item details string lists all non-default stats in a specific display order.
     */
    void detailsListsAllNonDefaultStatsInDisplayOrder() {
        // Arrange
        Item item = new Item(')', Color.GRAY, "enchanted weapon", null);
        item.modifyAttackValue(4);
        item.modifyThrownAttackValue(2);
        item.modifyRangedAttackValue(7);
        item.modifyDefenseValue(6);
        item.modifyFoodValue(9);

        // Act
        String details = item.details();

        // Assert
        assertEquals("  attack:4  thrown:3  ranged:7  defense:6  food:9", details);
    }

    @Test
    /**
     * Verifies that details include penalties and zero thrown attack but omit negative ranged attack values.
     */
    void detailsShowsPenaltiesAndZeroThrownValueButOmitsNegativeRangedValue() {
        // Arrange
        Item item = new Item(')', Color.GRAY, "cursed weapon", null);
        item.modifyAttackValue(-2);
        item.modifyThrownAttackValue(-1);
        item.modifyRangedAttackValue(-3);
        item.modifyDefenseValue(-4);
        item.modifyFoodValue(-5);

        // Act
        String details = item.details();

        // Assert
        assertEquals("  attack:-2  thrown:0  defense:-4  food:-5", details);
    }

    @Test
    /**
     * Tests that the quaffEffect of an Item can be set and replaced with another effect.
     */
    void quaffEffectCanBeReplaced() {
        // Arrange
        Item potion = new Item('!', Color.BLUE, "potion", null);
        Effect original = new Effect(2);
        Effect replacement = new Effect(5);

        // Act
        potion.setQuaffEffect(original);
        Effect first = potion.quaffEffect();
        potion.setQuaffEffect(replacement);
        Effect second = potion.quaffEffect();

        // Assert
        assertAll(
                () -> assertSame(original, first),
                () -> assertSame(replacement, second)
        );
    }

    @Test
    /**
     * Checks that written spells maintain insertion order and are specific to the Item they belong to.
     */
    void writtenSpellsRetainOrderAndBelongToTheItem() {
        // Arrange
        Item spellbook = new Item('+', Color.BLUE, "spellbook", null);
        Item otherBook = new Item('+', Color.BLUE, "other spellbook", null);

        // Act
        spellbook.addWrittenSpell("Spark", 2, new Effect(1));
        spellbook.addWrittenSpell("Shield", 5, new Effect(2));
        List<Spell> spells = spellbook.writtenSpells();

        // Assert
        assertEquals(2, spells.size());
        Spell spark = spells.get(0);
        Spell shield = spells.get(1);
        assertAll(
                () -> assertEquals("Spark", spark.name()),
                () -> assertEquals(2, spark.manaCost()),
                () -> assertEquals(1, spark.effect().duration),
                () -> assertEquals("Shield", shield.name()),
                () -> assertEquals(5, shield.manaCost()),
                () -> assertEquals(2, shield.effect().duration),
                () -> assertTrue(otherBook.writtenSpells().isEmpty())
        );
    }

    @Test
    void quaffEffectCanBeCleared() {
        // Arrange
        Item potion = new Item('!', Color.BLUE, "potion", null);
        potion.setQuaffEffect(new Effect(3));

        // Act
        potion.setQuaffEffect(null);

        // Assert
        assertNull(potion.quaffEffect());
    }

}
