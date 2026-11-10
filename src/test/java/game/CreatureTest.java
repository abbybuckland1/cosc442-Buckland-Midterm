package game;
import java.awt.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
