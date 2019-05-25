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

    public SmartImage transformed(int width, int height, double dx, double dy) {
        BufferedImage image;
        AffineTransform at = null;
        if (shouldTransform(width, height, dx, dy)) {
            BufferedImage after = new BufferedImage(width, height, source.getType());
            at = new AffineTransform();
            double scale = ((double) width) / source.getWidth();
            at.translate(-dx, -dy);
            at.scale(scale, scale);
            AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            image = scaleOp.filter(source, after);
        } else {
            image = source;
        }
        return new SmartImage(image, at);
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
