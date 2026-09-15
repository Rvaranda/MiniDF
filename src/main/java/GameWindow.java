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
    //public static final int SCALE = 3;

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

        int size = 16;
        for (int i = 0; i < WIDTH / size; i++) {
            for (int j = 0; j < HEIGHT / size; j++) {
                g2d.setColor(grassColors[random.nextInt(grassColors.length)]);
                g2d.fillRect(i*size, j*size, size, size);
            }
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
