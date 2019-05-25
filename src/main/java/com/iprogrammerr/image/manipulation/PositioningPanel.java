package com.iprogrammerr.image.manipulation;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PositioningPanel extends JPanel {

    private static final double SCALE_DIVISOR = 1.1;
    private static final double XY_STEPS = 20;
    private static final double MAX_ANGLE = Math.toRadians(360);
    private static final double MIN_ANGLE = -MAX_ANGLE;
    private final double minScale;
    private final double maxScale;
    private final double scaleMultiplier;
    private double scale = 1;
    private double dx;
    private double dy;
    private BufferedImage image;

    public PositioningPanel(double minScale, double maxScale, double scaleMultiplier) {
        this.minScale = minScale;
        this.maxScale = maxScale;
        this.scaleMultiplier = scaleMultiplier;
    }

    public PositioningPanel() {
        this(0.1, 3, 1.25);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) {
            return;
        }
        int width = (int) Math.round(image.getWidth() * scale);
        int height = (int) Math.round(image.getHeight() * scale);
        BufferedImage transformed = new SmartImage(image).transformed(width, height, dx, dy).source();
        int startX = (getWidth() - transformed.getWidth()) / 2;
        int startY = (getHeight() - transformed.getHeight()) / 2;
        g.drawImage(transformed, startX, startY, null);

    }

    public void moveUp() {
        dy -= yStep();
        repaint();
    }

    private int yStep() {
        return (int) Math.round(getHeight() / XY_STEPS);
    }

    public void moveLeft() {
        dx -= xStep();
        repaint();
    }

    private int xStep() {
        return (int) Math.round(getWidth() / XY_STEPS);
    }

    public void center() {
        dx = 0;
        dy = 0;
        repaint();
    }

    public void moveRight() {
        dx += xStep();
        repaint();
    }

    public void moveDown() {
        dy += yStep();
        repaint();
    }

    public void zoomOut() {
        if (scale > minScale) {
            scale /= scaleMultiplier;
            repaint();
        }
    }

    public void zoomIn() {
        if (scale < maxScale) {
            scale *= scaleMultiplier;
            repaint();
        }
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
    }
}
