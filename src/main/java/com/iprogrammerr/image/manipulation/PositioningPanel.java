package com.iprogrammerr.image.manipulation;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.vecmath.Point2d;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class PositioningPanel extends JPanel {

    private static final AffineTransform EMPTY_TRANSFORM = new AffineTransform();
    private static final double SCALE_DIVISOR = 1.1;
    private static final double HALF_DIVISOR = 2;
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
    private boolean centerTarget;
    private double rotationAngle;
    private BufferedImage image;
    private AffineTransform currentTransform;

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
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(startX, startY);
        g2d.scale(scale, scale);
        if (Math.abs(rotationAngle) > 0) {
            g2d.rotate(rotationAngle, image.getWidth() / HALF_DIVISOR, image.getHeight() / HALF_DIVISOR);
        }
        g2d.drawImage(image, 0, 0, null);
        currentTransform = g2d.getTransform();
        drawTarget(g2d);
    }

    private void drawTarget(Graphics2D g) {
        g.setTransform(EMPTY_TRANSFORM);
        if (centerTarget) {
            targetX = targetCenterX() + (getWidth() / 2);
            targetY = targetCenterY() + (getHeight() / 2);
        } else {
            int targetWidth = target.value().getWidth();
            int halfWidth = targetWidth / 2;
            if (targetX + halfWidth > getWidth()) {
                targetX = getWidth() - halfWidth;
            } else if (targetX + halfWidth < 0) {
                targetX = -halfWidth;
            }
            int targetHeight = target.value().getHeight();
            int halfHeight = targetHeight / 2;
            if (targetY + halfHeight > getHeight()) {
                targetY = getHeight() - halfHeight;
            } else if (targetY + halfHeight < 0) {
                targetY = -halfHeight;
            }
        }
        g.drawImage(target.value(), targetX, targetY, null);
    }

    private int targetX() {
        return targetX + (getWidth() / 2);
    }

    private int targetY() {
        return targetY + (getHeight() / 2);
    }

    private int targetCenterX() {
        return target.value().getWidth() / 2;
    }

    private int targetCenterY() {
        return target.value().getHeight() / 2;
    }

    private int nextWidth() {
        return (int) Math.round(image.getWidth() * scale);
    }

    private int nextHeight() {
        return (int) Math.round(image.getHeight() * scale);
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
            repaint();
        }
    }

    public void moveTargetUp() {
        targetY -= targetMoveY();
        centerTarget = false;
        repaint();
    }

    private int targetMoveY() {
        return (int) Math.round(getHeight() / TARGET_XY_STEPS);
    }

    public void moveTargetDown() {
        targetY += targetMoveY();
        centerTarget = false;
        repaint();
    }

    public void moveTargetLeft() {
        targetX -= targetMoveX();
        centerTarget = false;
        repaint();
    }

    private int targetMoveX() {
        return (int) Math.round(getWidth() / TARGET_XY_STEPS);
    }

    public void moveTargetRight() {
        targetX += targetMoveX();
        centerTarget = false;
        repaint();
    }

    public void centerTarget() {
        centerTarget = true;
        repaint();
    }

    public Point2d positionOnImage() {
        double frameImageScaleX = 1;
        double frameImageScaleY = 1;
        while (getWidth() < (image.getWidth() * frameImageScaleX)) {
            frameImageScaleX /= SCALE_DIVISOR;
        }
        while (getHeight() < (image.getHeight() * frameImageScaleY)) {
            frameImageScaleY /= SCALE_DIVISOR;
        }
        double xTranslation = currentTransform.getTranslateX();
        double yTranslation = currentTransform.getTranslateY();
        double x = targetX - xTranslation;
        double y = targetY - yTranslation;
        return new Point2d(x, y);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        dx = 0;
        dy = 0;
        scale = 1;
        rotationAngle = 0;
        centerTarget = true;
        repaint();
    }
}
