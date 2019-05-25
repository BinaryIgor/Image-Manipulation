package com.iprogrammerr.image.manipulation;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class PositioningPanel extends JPanel {

    private static final double SCALE_DIVISOR = 1.1;
    private static final double XY_STEPS = 20;
    private static final double TARGET_XY_STEPS = 2 * XY_STEPS;
    private static final double MAX_ANGLE = Math.toRadians(360);
    private static final double MIN_ANGLE = -MAX_ANGLE;
    private static final double ROTATION_STEP = MAX_ANGLE / 36;
    private final double minScale;
    private final double maxScale;
    private final double scaleMultiplier;
    private final Initialization<BufferedImage> target;
    private double scale = 1;
    private int dx;
    private int dy;
    private int targetY;
    private int targetX;
    private double rotationAngle;
    private BufferedImage image;

    public PositioningPanel(double minScale, double maxScale, double scaleMultiplier) {
        this.minScale = minScale;
        this.maxScale = maxScale;
        this.scaleMultiplier = scaleMultiplier;
        this.target = new Initialization<>(() -> {
            try (InputStream is = PositioningPanel.class.getResourceAsStream("/position.png")) {
                return ImageIO.read(is);
            }
        });
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
        int width = nextWidth();
        int height = nextHeight();
        int startX = startX(width);
        int startY = startY(height);
        BufferedImage scaled = new SmartImage(image).scaled(width, height);
        Graphics2D g2d = (Graphics2D) g;
        if (Math.abs(rotationAngle) > 0) {
            Point2d translation = translation();
            g2d.rotate(rotationAngle, translation.x, translation.y);
        }
        Vector2d startXY = startXY(width, height);
        g2d.translate(startX, startY);
        System.out.println(String.format("Start x = %d, start y = %d", startX, startY));
        System.out.println(String.format("Translation x = %.3f, translation y = %.3f",
            g2d.getTransform().getTranslateX(), g2d.getTransform().getTranslateY()));
        g2d.drawImage(scaled, 0, 0, null);
        drawTarget(g2d);
    }

    private void drawTarget(Graphics2D g) {
        g.setTransform(new AffineTransform());
        g.drawImage(target.value(), targetX + (getWidth() / 2), targetY + (getHeight() / 2), null);
    }

    private int targetCenterX() {
        return target.value().getWidth() / 2;
    }

    private int targetCenterY() {
        return target.value().getHeight() / 2;
    }

    private Point2d translation() {
        return new Point2d(getWidth() / 2.0, getHeight() / 2.0);
    }

    private int nextWidth() {
        return (int) Math.round(image.getWidth() * scale);
    }

    private int nextHeight() {
        return (int) Math.round(image.getHeight() * scale);
    }

    private Vector2d startXY(int width, int height) {
        double x = (getWidth() - width) / 2;
        double y = (getHeight() - height) / 2;
        double cos = Math.cos(rotationAngle);
        double sin = Math.sin(rotationAngle);
        double translatedDx = dx * cos - dy * sin;
        double translatedDy = dx * sin + dy * cos;
        return new Vector2d(x - dx, y - dy);
    }

    private int startX(int width) {
        return (getWidth() - width) / 2 - dx;
    }

    private int startX() {
        return startX(nextWidth());
    }

    private int startY(int height) {
        return (getHeight() - height) / 2 - dy;
    }

    private int startY() {
        return startY(nextHeight());
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
//            dx *= scale;
//            dy *= scale;
            repaint();
        }
    }

    public void rotateLeft() {
        rotationAngle -= ROTATION_STEP;
        repaint();
    }

    public void rotateRight() {
        rotationAngle += ROTATION_STEP;
        repaint();
    }

    public void zoomIn() {
        if (scale < maxScale) {
            scale *= scaleMultiplier;
//            dx /= scale;
//            dy /= scale;
            repaint();
        }
    }

    public void moveTargetUp() {
        targetY -= targetMoveY();
        repaint();
    }

    private int targetMoveY() {
        return (int) Math.round(getHeight() / TARGET_XY_STEPS);
    }

    public void moveTargetDown() {
        targetY += targetMoveY();
        repaint();
    }

    public void moveTargetLeft() {
        targetX -= targetMoveX();
        repaint();
    }

    private int targetMoveX() {
        return (int) Math.round(getWidth() / TARGET_XY_STEPS);
    }

    public void moveTargetRight() {
        targetX += targetMoveX();
        repaint();
    }

    public void centerTarget() {
        targetX = 0;
        targetY = 0;
        repaint();
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        dx = 0;
        dy = 0;
        scale = 1;
        rotationAngle = 0;
        targetX = targetCenterX();
        targetY = targetCenterY();
        repaint();
    }
}