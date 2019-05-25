package com.iprogrammerr.image.manipulation;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {

    private static final double SCALE_DIVISOR = 1.1;
    private BufferedImage image;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        System.out.println(String.format("Size = %d, %d", getWidth(), getHeight()));
        if (image != null) {
            double scale = 1;
            while ((image.getWidth() * scale) > getWidth() || (image.getHeight() * scale) > getHeight()) {
                scale /= SCALE_DIVISOR;
            }
            if (scale < 1) {
                int width = (int) Math.round(getWidth() * scale);
                int height = (int) Math.round(getHeight() * scale);
                BufferedImage transformed = new BufferedImage(width, height, image.getType());
                AffineTransform at = new AffineTransform();
                at.scale(scale, scale);
                AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                op.filter(image, transformed);
                int startX = (getWidth() - transformed.getWidth()) / 2;
                int startY = (getHeight() - transformed.getHeight()) / 2;
                g.drawImage(transformed, startX, startY, null);
            } else {
                g.drawImage(image, 0, 0, null);
            }
        }
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
    }
}
