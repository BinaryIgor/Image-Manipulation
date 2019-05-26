package com.iprogrammerr.image.manipulation;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.vecmath.Point2d;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class AppFrame extends JFrame {

    private static final String COORDINATES_TITLE = "Coordinates";
    private static final String COORDINATES_FORMAT = "Current coordinates: %g px, %g px";
    private static final String STATE_IMAGE = "State: image";
    private static final String STATE_POSITION = "State: position";
    private final BufferedImage image;
    private PositioningPanel positioningPanel;
    private JButton upButton;
    private JButton rotateRightButton;
    private JButton rotateLeftButton;
    private JButton leftButton;
    private JButton centerButton;
    private JButton rightButton;
    private JButton minusButton;
    private JButton downButton;
    private JButton plusButton;
    private JButton choosePictureButton;
    private JButton stateButton;
    private JButton coordinatesButton;
    private boolean positionState;

    public AppFrame(BufferedImage image) {
        this.image = image;
    }

    public void init() {
        Dimension rootSize = new Dimension(1200, 600);
        setMinimumSize(rootSize);
        setPreferredSize(rootSize);
        positioningPanel = new PositioningPanel();

        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        controlPanel.setBackground(Color.darkGray);
        GridLayout layout = new GridLayout(4, 3);
        layout.setHgap(10);
        layout.setVgap(10);

        controlPanel.setLayout(layout);
        setButtons();
        controlPanel.add(rotateLeftButton);
        controlPanel.add(upButton);
        controlPanel.add(rotateRightButton);
        controlPanel.add(leftButton);
        controlPanel.add(centerButton);
        controlPanel.add(rightButton);
        controlPanel.add(minusButton);
        controlPanel.add(downButton);
        controlPanel.add(plusButton);
        controlPanel.add(choosePictureButton);
        controlPanel.add(stateButton);
        controlPanel.add(coordinatesButton);

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;

        c.weightx = 2;
        c.weighty = 2;
        c.gridheight = 2;
        c.gridx = 0;
        c.gridy = 0;
        positioningPanel.setImage(image);
        add(positioningPanel, c);
        c.weightx = 0.5;
        c.weighty = 1;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 0;
        add(controlPanel, c);
    }

    private void setButtons() {
        rotateLeftButton = new JButton("<");
        upButton = new JButton("Up");
        rotateRightButton = new JButton(">");
        leftButton = new JButton("Left");
        centerButton = new JButton("Center");
        rightButton = new JButton("Right");
        minusButton = new JButton("-");
        downButton = new JButton("Down");
        plusButton = new JButton("+");
        choosePictureButton = new JButton("Choose picture");
        stateButton = new JButton(STATE_IMAGE);
        coordinatesButton = new JButton("Read coordinates");

        rotateLeftButton.addActionListener(e -> positioningPanel.rotateLeft());
        upButton.addActionListener(e -> {
            if (positionState) {
                positioningPanel.moveTargetUp();
            } else {
                positioningPanel.moveUp();
            }
        });
        rotateRightButton.addActionListener(e -> positioningPanel.rotateRight());
        leftButton.addActionListener(e -> {
            if (positionState) {
                positioningPanel.moveTargetLeft();
            } else {
                positioningPanel.moveLeft();
            }
        });
        centerButton.addActionListener(e -> {
            if (positionState) {
                positioningPanel.centerTarget();
            } else {
                positioningPanel.center();
            }
        });
        rightButton.addActionListener(e -> {
            if (positionState) {
                positioningPanel.moveTargetRight();
            } else {
                positioningPanel.moveRight();
            }
        });
        minusButton.addActionListener(e -> positioningPanel.zoomOut());
        downButton.addActionListener(e -> {
            if (positionState) {
                positioningPanel.moveTargetDown();
            } else {
                positioningPanel.moveDown();
            }
        });
        plusButton.addActionListener(e -> positioningPanel.zoomIn());
        choosePictureButton.addActionListener(e -> showFileChooser());
        stateButton.addActionListener(e -> {
            positionState = !positionState;
            stateButton.setText(positionState ? STATE_POSITION : STATE_IMAGE);
        });
        coordinatesButton.addActionListener(e -> {
            Point2d coords = positioningPanel.positionOnImage();
            JOptionPane.showMessageDialog(this, String.format(COORDINATES_FORMAT, coords.x, coords.y),
                COORDINATES_TITLE, JOptionPane.INFORMATION_MESSAGE);
        });
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
                e.printStackTrace();
                //TODO error handling
            }
        }
    }


    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
    }
}
