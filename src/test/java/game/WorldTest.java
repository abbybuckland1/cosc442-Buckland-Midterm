package game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class WorldTest {
    private World world;

    @BeforeEach
    /**
     * Initializes the world with a 3D array of tiles, setting floors everywhere and distinct tiles at the corners for boundary testing.
     */
    void setUp() {
        Tile[][][] tiles = new Tile[3][4][5];

        for (Tile[][] column : tiles) {
            for (Tile[] row : column) {
                Arrays.fill(row, Tile.FLOOR);
            }
        }

        // Distinct corners verify that valid coordinates return stored tiles.
        tiles[0][0][0] = Tile.WALL;
        tiles[2][3][4] = Tile.STAIRS_DOWN;

        world = new World(tiles);
    }

    @ParameterizedTest
    @CsvSource({
        "0, 0, 0, WALL",         // First valid corner
        "2, 3, 4, STAIRS_DOWN",  // Last valid corner
        "0, 1, 1, FLOOR",        // First valid x
        "2, 1, 1, FLOOR",        // Last valid x
        "1, 0, 1, FLOOR",        // First valid y
        "1, 3, 1, FLOOR",        // Last valid y
        "1, 1, 0, FLOOR",        // First valid z
        "1, 1, 4, FLOOR"         // Last valid z
    })
    /**
     * Verifies that querying the world at valid boundary coordinates returns the correct stored tile.
     * @param x The x-coordinate in the world.
     * @param y The y-coordinate in the world.
     * @param z The z-coordinate in the world.
     * @param expected The expected tile to be returned at the given coordinates.
     */
    void testTileReturnsStoredTileAtValidBoundaries(
            int x, int y, int z, Tile expected) {
        // Arrange: the world and expected tile are supplied above.

        // Act
        Tile actual = world.tile(x, y, z);

        // Assert
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @CsvSource({
        "-1, 1, 1",  // Immediately before the first x
        "3, 1, 1",   // Immediately after the last x
        "1, -1, 1",  // Immediately before the first y
        "1, 4, 1",   // Immediately after the last y
        "1, 1, -1",  // Immediately before the first z
        "1, 1, 5"    // Immediately after the last z
    })
    /**
     * Checks that querying the world at coordinates immediately outside valid boundaries returns the BOUNDS tile.
     * @param x The x-coordinate just outside the world bounds.
     * @param y The y-coordinate just outside the world bounds.
     * @param z The z-coordinate just outside the world bounds.
     */
    void testTileReturnsBoundsImmediatelyOutsideWorld(
            int x, int y, int z) {
        // Arrange: the world and out-of-range coordinate are supplied above.

        // Act
        Tile actual = world.tile(x, y, z);

        // Assert
        assertEquals(Tile.BOUNDS, actual);
    }
}
