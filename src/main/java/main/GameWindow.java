package main;

import renderer.TileRenderer;
import renderer.Camera;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameWindow extends JPanel implements Runnable {
    Thread thread;
    private boolean running = false;

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int FPS = 60;

    private final World world = new World();
    private final TileRenderer tileRenderer = new TileRenderer();
    private final Camera camera = new Camera();

    private double cameraSpeed = 300;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public GameWindow() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDoubleBuffered(true);
        setFocusable(true);
        setBackground(Color.black);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> upPressed = true;
                    case KeyEvent.VK_S -> downPressed = true;
                    case KeyEvent.VK_A -> leftPressed = true;
                    case KeyEvent.VK_D -> rightPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W -> upPressed = false;
                    case KeyEvent.VK_S -> downPressed = false;
                    case KeyEvent.VK_A -> leftPressed = false;
                    case KeyEvent.VK_D -> rightPressed = false;
                }
            }
        });
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
        if (upPressed) camera.move(0, -cameraSpeed * delta);
        if (downPressed) camera.move(0, cameraSpeed * delta);
        if (rightPressed) camera.move(cameraSpeed * delta, 0);
        if (leftPressed) camera.move(-cameraSpeed * delta, 0);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Tile t : world.getTiles()) {
            tileRenderer.render(t, camera, g2d);
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
