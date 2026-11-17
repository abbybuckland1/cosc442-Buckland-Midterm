package game;
import java.awt.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class CreatureTest {
    private World world;
    private Creature creature;

    @BeforeEach
    void setUp() {
        Tile[][][] tiles = new Tile[3][3][1];

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                tiles[x][y][0] = Tile.FLOOR;
            }
        }

        world = new World(tiles);

        creature = new Creature(world, '@', Color.GRAY, "josh", 100, 10, 5);

        creature.x = 1;
        creature.y = 1;
        creature.z = 0;
    }

    @Test
    void testModifyHpSurvives() {
        //arrange
        int amount = -20;

        //act
        creature.modifyHp(amount, "damage");

        //assert 
        assertEquals(80, creature.hp());
    }

    
    @Test
    void testModifyHpDies() {
        //arrange
        int amount = -100;

        //act
        creature.modifyHp(amount, "damage");

        //assert
        assertEquals(0, creature.hp());
        assertEquals("damage", creature.causeOfDeath());
    }

    @Test
    void testModifyHpLivingCreatureStaysInWorld() {
        //Arrange, adding a creature to the world
        world.add(creature);

        //Act, subtracting 20 hp leaves creature with 80
        creature.modifyHp(-20, "damage");

        //Assert,checks that creature has 80 hp
        assertEquals(80, creature.hp());
        //checks that creature is still in the world (killing mutant)
        assertEquals(creature, world.creature(1, 1, 0));
    }
    @Test
    void testDeadCreatureIsRemovedFromWorld() {
        // Arrange
        new CreatureAi(creature);
        world.add(creature);

        // Confirm the creature is in the world before it dies.
        assertSame(creature, world.creature(1, 1, 0));

        // Act
        creature.modifyHp(-100, "damage");

        // Assert
        assertEquals(0, creature.hp());
        assertEquals("damage", creature.causeOfDeath());
        assertNull(world.creature(1, 1, 0));
    }

    @Test
    void testDeathCreatesCorpseWithExpectedProperties() {
        // Arrange
        new CreatureAi(creature);
        world.add(creature);

        // Act
        creature.modifyHp(-100, "damage");

        // Assert
        Item corpse = world.item(1, 1, 0);

        assertNotNull(corpse);
        assertEquals("josh corpse", corpse.name());
        assertEquals('%', corpse.glyph());
        assertEquals(Color.GRAY, corpse.color());
        assertEquals(500, corpse.foodValue());
    }

    @Test
    void testDeathDropsInventoryItems() {
        // Arrange
        new CreatureAi(creature);
        world.add(creature);

        Item rock = new Item(',', Color.YELLOW, "rock", null);
        Item apple = new Item('%', Color.RED, "apple", null);

        creature.inventory().add(rock);
        creature.inventory().add(apple);

        // Act
        creature.modifyHp(-100, "damage");

        // Assert
        Item corpse = world.item(1, 1, 0);
        assertNotNull(corpse);
        assertEquals("josh corpse", corpse.name());

        int rockCount = 0;
        int appleCount = 0;

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                Item itemOnGround = world.item(x, y, 0);

                if (itemOnGround == rock) {
                    rockCount++;
                }
                if (itemOnGround == apple) {
                    appleCount++;
                }
            }
        }

        assertEquals(1, rockCount);
        assertEquals(1, appleCount);
        assertFalse(creature.inventory().contains(rock));
        assertFalse(creature.inventory().contains(apple));
    }
}
