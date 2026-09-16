package pathfinding;

import main.Tile;
import main.World;

import java.util.*;

public class Pathfinder {
    private static class Node {
        Tile tile;
        Node parent;
        int f, g, h;

        Node(Tile tile) {
            this.tile = tile;
            parent = null;
            f = g = h = 0;
        }
    }

    private int heuristic(Tile from, Tile to) {
        int x1 = from.getX();
        int y1 = from.getY();
        int x2 = to.getX();
        int y2 = to.getY();

        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);

        return 10 * (dx + dy) - 6 * Math.min(dx, dy);
    }

    private int movementCost(Tile from, Tile to) {
        boolean diagonal = from.getX() != to.getX() && from.getY() != to.getY();
        return diagonal ? 14 : 10;
    }

    private boolean hasReachedGoal(Tile current, Tile goal) {
        int x1 = current.getX();
        int y1 = current.getY();
        int x2 = goal.getX();
        int y2 = goal.getY();

        return x1 == x2 && y1 == y2;
    }

    private Tile[] getNeighbors(World world, Tile tile) {
        int x = tile.getX();
        int y = tile.getY();
        int[][] directions = {
                {0, -1}, { 0, 1},
                {1,  0}, {-1, 0},
                {1, -1}, {-1, -1},
                {-1, 1}, {1, 1}
        };

        List<Tile> tiles = new ArrayList<>();

        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];

            int nx = x + dx;
            int ny = y + dy;

            Tile neighbor = world.getTile(nx, ny);

            if (neighbor == null || !neighbor.isTraversable())
                continue;

            boolean diagonal = dx != 0 && dy != 0;

            if (diagonal) {
                Tile horizontal = world.getTile(x + dx, y);
                Tile vertical   = world.getTile(x, y + dy);

                if (horizontal == null || vertical == null)
                    continue;

                if (!horizontal.isTraversable() || !vertical.isTraversable())
                    continue;
            }

            tiles.add(neighbor);
        }

        return tiles.toArray(Tile[]::new);
    }

    private List<Tile> reconstructPath(Node node) {
        List<Tile> path = new ArrayList<>();
        while (node != null) {
            path.add(node.tile);
            node = node.parent;
        }

        return path.reversed();
    }

    public List<Tile> findPath(World world, Tile origin, Tile destination) {
        if (origin == null || destination == null) return List.of();

        Comparator<Node> comparator = Comparator.comparingInt(n -> n.f);
        Queue<Node> open = new PriorityQueue<>(comparator);
        List<Node> closed = new ArrayList<>();
        Map<Tile, Node> nodes = new HashMap<>();

        Node start = nodes.computeIfAbsent(origin, Node::new);
        start.h = heuristic(origin, destination);
        start.f = start.g + start.h;
        open.add(start);

        while (!open.isEmpty()) {
            Node current = open.poll();
            closed.add(current);
            if (hasReachedGoal(current.tile, destination)) {
                // Finaliza o algoritmo e reconstroi o caminho
                return reconstructPath(current);
            }

            Tile[] neighbors = getNeighbors(world, current.tile);
            for (Tile neighbor : neighbors) {
                Node neighborNode = nodes.computeIfAbsent(neighbor, Node::new);
                if (closed.contains(neighborNode)) {
                    continue;
                }

                int tentativeG = current.g + movementCost(current.tile, neighbor);
                if (!open.contains(neighborNode)) {
                    neighborNode.g = tentativeG;
                    neighborNode.h = heuristic(neighbor, destination);
                    neighborNode.f = neighborNode.g + neighborNode.h;
                    neighborNode.parent = current;
                    open.add(neighborNode);
                }
                else if (tentativeG < neighborNode.g) {
                    neighborNode.g = tentativeG;
                    neighborNode.f = neighborNode.g + neighborNode.h;
                    neighborNode.parent = current;
                }
            }
        }

        return List.of();
    }
}
