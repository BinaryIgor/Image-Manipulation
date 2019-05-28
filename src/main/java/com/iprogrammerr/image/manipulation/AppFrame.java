package com.iprogrammerr.image.manipulation;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.vecmath.Point2d;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class AppFrame extends JFrame {

    private static final String TITLE = "Image manipulation";
    private static final String COORDINATES_TITLE = "Coordinates";
    private static final String COORDINATES_FORMAT = "Current coordinates: %d px, %d px";
    private static final String STATE_IMAGE = "State: image";
    private static final String STATE_POSITION = "State: position";
    private static final String ERROR_TITLE = "Error";
    private static final String READING_IMAGE_ERROR_FORMAT = "Can't read %s file as initialImage";
    private final BufferedImage initialImage;
    private PositioningPanel positioningPanel;
    private JPanel controlPanel;
    private boolean positionState;

    public AppFrame(BufferedImage initialImage) {
        this.initialImage = initialImage;
    }

    public void init() {
        setTitle(TITLE);
        Dimension rootSize = new Dimension(1200, 600);
        setMinimumSize(rootSize);
        setPreferredSize(rootSize);
        createPositioningPanel();
        createControlPanel();
        setLayout();
    }

    private void createPositioningPanel() {
        positioningPanel = new PositioningPanel();
        positioningPanel.setImage(initialImage);
    }

    private void createControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        controlPanel.setBackground(Color.darkGray);
        GridLayout layout = new GridLayout(4, 3);
        layout.setHgap(10);
        layout.setVgap(10);
        controlPanel.setLayout(layout);
        controlPanel.add(rotateLeftButton());
        controlPanel.add(upButton());
        controlPanel.add(rotateRightButton());
        controlPanel.add(leftButton());
        controlPanel.add(centerButton());
        controlPanel.add(rightButton());
        controlPanel.add(minusButton());
        controlPanel.add(downButton());
        controlPanel.add(plusButton());
        controlPanel.add(chooseImageButton());
        controlPanel.add(stateButton());
        controlPanel.add(readCoordinatesButton());
    }

    private JButton rotateLeftButton() {
        return button("<", e -> positioningPanel.rotateLeft());
    }

    private JButton button(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        return button;
    }

    private JButton upButton() {
        return button("Up", e -> {
            if (positionState) {
                positioningPanel.moveTargetUp();
            } else {
                positioningPanel.moveUp();
            }
        });
    }

    private JButton rotateRightButton() {
        return button(">", e -> positioningPanel.rotateRight());
    }

    private JButton leftButton() {
        return button("Left", e -> {
            if (positionState) {
                positioningPanel.moveTargetLeft();
            } else {
                positioningPanel.moveLeft();
            }
        });
    }

    private JButton centerButton() {
        return button("Center", e -> {
            if (positionState) {
                positioningPanel.centerTarget();
            } else {
                positioningPanel.center();
            }
        });
    }

    private JButton rightButton() {
        return button("Right", e -> {
            if (positionState) {
                positioningPanel.moveTargetRight();
            } else {
                positioningPanel.moveRight();
            }
        });
    }

    private JButton minusButton() {
        return button("-", e -> positioningPanel.zoomOut());
    }

    private JButton downButton() {
        return button("Down", e -> {
            if (positionState) {
                positioningPanel.moveTargetDown();
            } else {
                positioningPanel.moveDown();
            }
        });
    }

    private JButton plusButton() {
        return button("+", e -> positioningPanel.zoomIn());
    }

    private JButton chooseImageButton() {
        return button("Choose image", e -> showFileChooser());
    }

    private JButton stateButton() {
        return button(STATE_IMAGE, e -> {
            positionState = !positionState;
            ((JButton) e.getSource()).setText(positionState ? STATE_POSITION : STATE_IMAGE);
        });
    }

    private JButton readCoordinatesButton() {
        return button("Read coordinates", e -> {
            Point2d coordinates = positioningPanel.positionOnImage();
            JOptionPane.showMessageDialog(this, String.format(COORDINATES_FORMAT,
                (int) coordinates.x, (int) coordinates.y),
                COORDINATES_TITLE, JOptionPane.INFORMATION_MESSAGE);
        });
    }

    private void setLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 2;
        c.weighty = 2;
        c.gridheight = 2;
        c.gridx = 0;
        c.gridy = 0;
        add(positioningPanel, c);
        c.weightx = 0.5;
        c.weighty = 1;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 0;
        add(controlPanel, c);
    }


    private void showFileChooser() {
        JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        chooser.setFileFilter(new FileNameExtensionFilter("Pictures", "jpg", "png", "jpeg"));
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selected = chooser.getSelectedFile();
            try {
                positioningPanel.setImage(ImageIO.read(selected));
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    String.format(READING_IMAGE_ERROR_FORMAT, selected.getAbsolutePath()),
                    ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
