package com.iprogrammerr.image.manipulation;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Optional;

public class SmartImage {

    private static final double EPSILON = 10e-6;
    private final BufferedImage source;
    private final Optional<AffineTransform> appliedTransformation;

    public SmartImage(BufferedImage source, AffineTransform appliedTransformation) {
        this.source = source;
        this.appliedTransformation = Optional.ofNullable(appliedTransformation);
    }

    public SmartImage(BufferedImage image) {
        this(image, null);
    }

    public BufferedImage scaled(int width, int height) {
        BufferedImage image;
        if (width != source.getWidth() || height != source.getHeight()) {
            BufferedImage after = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            AffineTransform at = new AffineTransform();
            double scale = ((double) width) / source.getWidth();
            at.scale(scale, scale);
            AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            image = scaleOp.filter(source, after);
        } else {
            image = source;
        }
        return image;
    }

    public BufferedImage rotated(double angle, int frameWidth, int frameHeight) {
        BufferedImage after = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.rotate(angle, frameWidth / 2, frameHeight / 2);
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        return scaleOp.filter(source, after);
    }

    private boolean shouldTransform(int width, int height, double dx, double dy) {
        return width != source.getWidth() && height != source.getHeight() || Math.abs(dx) > EPSILON ||
            Math.abs(dy) > EPSILON;
    }

    public BufferedImage source() {
        return source;
    }

    public Optional<AffineTransform> appliedTransformation() {
        return appliedTransformation;
    }
}
