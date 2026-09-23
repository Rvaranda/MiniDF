package pathfinding;

import main.Tile;
import main.World;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PathfinderTest {

    @Test
    void encontrarCaminho() {
        World world = new World(10, 10);
        Tile origin = world.getTile(1, 1);
        Tile destination = world.getTile(6, 1);

        assertNotNull(origin);
        assertNotNull(destination);
        assertTrue(origin.isTraversable());
        assertTrue(destination.isTraversable());

        List<Tile> path = Pathfinder.findPath(world, origin, destination);

        assertFalse(path.isEmpty(), "Deveria encontrar um caminho");
        assertEquals(origin, path.getFirst());
        assertEquals(destination, path.getLast());
    }

    @Test
    void naoAtravessaParede() {
        World world = new World(10, 10);

        // parede no meio
        world.placeWall(4, 4);
        Tile parede = world.getTile(4, 4);
        assertFalse(parede.isTraversable());

        Tile origin = world.getTile(2, 4);
        Tile destination = world.getTile(6, 4);

        List<Tile> path = Pathfinder.findPath(world, origin, destination);

        // se achou caminho, não pode passar pela parede
        for (Tile t : path) {
            assertNotEquals(parede, t, "Path não deveria incluir a parede");
            assertTrue(t.isTraversable());
        }
    }
}
