package main;

import jobsystem.ChopTreeJob;
import jobsystem.JobManager;

import renderer.DwarfRenderer;
import renderer.TileRenderer;
import renderer.Camera;

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameWindow extends JPanel implements Runnable {
    Thread thread;
    private boolean running = false;

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int FPS = 60;

    private final World world = new World();

    // Renderers
    private final TileRenderer tileRenderer = new TileRenderer();
    private final DwarfRenderer dwarfRenderer = new DwarfRenderer();
    private final Camera camera = new Camera();

    // Camera
    private double cameraSpeed = 300;
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    // Mouse
    private int mouseX, mouseY;

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
                    case KeyEvent.VK_R -> {
                        for (int[] pos : world.pos) {
                            JobManager.addJob(new ChopTreeJob(world.getTile(pos[0], pos[1])));
                        }
                    }
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

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                mouseX = e.getX();
                mouseY = e.getY();

                int worldX = mouseX + (int) camera.getX();
                int worldY = mouseY + (int) camera.getY();

                int tileX = worldX / World.TILE_SIZE;
                int tileY = worldY / World.TILE_SIZE;

                Tile clickedTile = world.getTile(tileX, tileY);

                if (clickedTile != null) {
                    System.out.println("WorldX: " + worldX + "\tWorldY: " + worldY);
                    System.out.println("TileX: " + tileX + "\tTileY: " + tileY);
                    System.out.println("X: " + clickedTile.getX() + "\tY: " + clickedTile.getY());
                    System.out.println("Tree: " + clickedTile.hasTree());

                    if (clickedTile.hasTree()) {
                        // criar job de cortar arvore
                        JobManager.addJob(new ChopTreeJob(clickedTile));
                    }

//                    List<Tile> path = pathfinder.findPath(world, world.getTile(dwarf.getX(), dwarf.getY()), clickedTile);
//                    dwarf.setPath(path.toArray(Tile[]::new));
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

    void updateCamera(double delta) {
        if (upPressed) camera.move(0, -cameraSpeed * delta);
        if (downPressed) camera.move(0, cameraSpeed * delta);
        if (rightPressed) camera.move(cameraSpeed * delta, 0);
        if (leftPressed) camera.move(-cameraSpeed * delta, 0);
    }

    void update(double delta) {
        world.getDwarves().forEach(Dwarf::update);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (Tile t : world.getTiles()) {
            tileRenderer.render(t, camera, g2d);
        }

        world.getDwarves().forEach(d -> dwarfRenderer.render(d, camera, g2d));
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

            updateCamera(delta);

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
