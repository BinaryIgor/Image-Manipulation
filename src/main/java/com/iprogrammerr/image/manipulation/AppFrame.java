package com.iprogrammerr.image.manipulation;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class AppFrame extends JFrame {

    private static final String IMAGE = "IMAGE";
    private static final String POSITION = "POSITION";
    private final BufferedImage image;
    private ImagePanel imagePanel;
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

    public AppFrame(BufferedImage image) {
        this.image = image;
    }

    public void init() {
        Dimension rootSize = new Dimension(800, 600);
        setMinimumSize(rootSize);
        imagePanel = new ImagePanel();

        JPanel controlPanel = new JPanel();
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        controlPanel.setBackground(Color.darkGray);
        GridLayout layout = new GridLayout(3, 3);
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

        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;

        c.weightx = 2;
        c.weighty = 2;
        c.gridheight = 2;
        c.gridx = 0;
        c.gridy = 0;
        imagePanel.setImage(image);
        add(imagePanel, c);
        c.weightx = 0.5;
        c.weighty = 1;
        c.gridheight = 1;
        c.gridwidth = 2;
        c.gridx = 1;
        c.gridy = 0;
        add(controlPanel, c);

        c.weightx = 0.25;
        c.weighty = 0.25;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 1;
        add(choosePictureButton, c);

        c.gridx = 2;
        c.gridy = 1;
        add(stateButton, c);
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
        stateButton = new JButton(POSITION);
    }

    @Override
    public void paintComponents(Graphics g) {
        super.paintComponents(g);
    }
}
