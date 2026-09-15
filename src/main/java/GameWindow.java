import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GameWindow extends JPanel implements Runnable {
    Thread thread;
    Random random = new Random();
    private boolean running = false;

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int FPS = 60;

    private final World world = new World();

    private final Color[] grassColors = new Color[] {
            new Color(96, 194, 89),
            new Color(111, 219, 61),
            new Color(168, 222, 102),
            new Color(48, 150, 42),
            new Color(101, 191, 112),
            new Color(99, 214, 32),
    };

    public GameWindow() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDoubleBuffered(true);
        setFocusable(true);
        setBackground(Color.black);
    }

    public void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        running = false;
    }

    void update(double delta) {

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Tile t : world.getTiles()) {
            Color color;
            int screenX = t.getX() * World.TILE_SIZE;
            int screenY = t.getY() * World.TILE_SIZE;

            switch (t.getType()) {
                case GRASS -> color = grassColors[t.getVisualVariant()];
                default -> color = Color.white;
            }

            g2d.setColor(color);
            g2d.fillRect(screenX, screenY, World.TILE_SIZE, World.TILE_SIZE);
        }
    }

    @Override
    public void run() {
        double frameMinDuration = 1.0 / FPS;
        final double TICK = 1.0 / 20.0;
        double acumulator = 0.0;

        long lastUpdate = System.nanoTime();
        double delta;

        while (running) {
            long frameStart = System.nanoTime();

            delta = (frameStart - lastUpdate) / 1_000_000_000.0;
            lastUpdate = frameStart;

            acumulator += delta;

            while (acumulator >= TICK) {
                update(TICK);
                acumulator -= TICK;
            }

            repaint();

            double frameDuration = (System.nanoTime() - frameStart) / 1_000_000_000.0;
            double remainingTime = frameMinDuration - frameDuration;

            if (remainingTime > 0) {
                try {
                    Thread.sleep((long) (remainingTime * 1000.0));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
