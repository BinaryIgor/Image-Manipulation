package com.iprogrammerr.image.manipulation;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class App {

    public static void main(String[] args) throws Exception {
        AppFrame frame = new AppFrame(initialImage());
        frame.init();
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    private static BufferedImage initialImage() throws Exception {
        try (InputStream is = new BufferedInputStream(App.class.getResourceAsStream("/dragon.jpg"))) {
            return ImageIO.read(is);
        }
    }
}
