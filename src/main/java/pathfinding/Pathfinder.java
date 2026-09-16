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

    private int heuristic(Node node, Tile tile) {
        int x1 = node.tile.getX();
        int y1 = node.tile.getY();
        int x2 = tile.getX();
        int y2 = tile.getY();

        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);

        return 10 * (dx + dy) - 6 * Math.min(dx, dy);
    }

    private int distance(Node n1, Node n2) {
        int x1 = n1.tile.getX();
        int y1 = n1.tile.getY();
        int x2 = n2.tile.getX();
        int y2 = n2.tile.getY();

        int diffX = Math.abs(x1 - x2);
        int diffY = Math.abs(y1 - y2);

        return diffX + diffY > 1 ? 14 : 10;
    }

    private boolean hasReachedGoal(Tile current, Tile goal) {
        int x1 = current.getX();
        int y1 = current.getY();
        int x2 = goal.getX();
        int y2 = goal.getY();

        return x1 == x2 && y1 == y2;
    }

    private Node[] getNeighbors(World world, Node node) {
        int x = node.tile.getX();
        int y = node.tile.getY();
        int[][] directions = {
                {0, -1}, { 0, 1},
                {1,  0}, {-1, 0},
                {1, -1}, {-1, -1},
                {-1, 1}, {1, 1}
        };

        List<Node> nodes = new ArrayList<>();

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

            Node neighborNode = new Node(neighbor);
            neighborNode.g = diagonal ? node.g + 14 : node.g + 10;
            nodes.add(neighborNode);
        }

        return nodes.toArray(Node[]::new);
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
        Comparator<Node> comparator = Comparator.comparingInt(n -> n.f);
        Queue<Node> open = new PriorityQueue<>(comparator);
        List<Node> closed = new ArrayList<>();

        Node start = new Node(origin);
        start.h = heuristic(start, destination);
        start.f = start.g + start.h;
        open.add(start);

        while (!open.isEmpty()) {
            Node current = open.poll();
            closed.add(current);
            if (hasReachedGoal(current.tile, destination)) {
                // Finaliza o algoritmo e reconstroi o caminho
                return reconstructPath(current);
            }

            Node[] neighbors = getNeighbors(world, current);
            for (Node neighbor : neighbors) {
                if (closed.contains(neighbor)) {
                    continue;
                }
                if (!open.contains(neighbor)) open.add(neighbor);
                neighbor.h = heuristic(neighbor, destination);
                neighbor.f = neighbor.g + neighbor.h;
                neighbor.parent = current;
            }
        }

        return null;
    }
}
