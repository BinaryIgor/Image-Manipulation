package com.iprogrammerr;

import org.junit.Test;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;

public class AppTest {

    @Test
    public void shouldAnswerWithTrue() {
        Vector2d vector = new Vector2d(-1, 0);
        double angle = Math.toRadians(45);
        System.out.println(vector);
        System.out.println(rotated(vector.x, vector.y, angle));
        Matrix4d matrix = new Matrix4d();
        matrix.rotZ(angle);
        Point3d p = new Point3d(vector.x, vector.y, 0);
        matrix.transform(p);
        System.out.println("Using matrix = " + p);
    }

    private Vector2d rotated(double x, double y, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double translatedDx = x * cos - y * sin;
        double translatedDy = x * sin + y * cos;
        return new Vector2d(translatedDx, translatedDy);
    }
}
