package com.sd451;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 25;
    static final int DELAY = 75;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 1;
    int applesEaten;
    int appleX;
    int appleY;
   static char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new myKeyAdapter());
        startGame();
    }
    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics graphics) {

        if (running) {
            /*
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                graphics.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                graphics.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            } */
            graphics.setColor(Color.magenta);
            graphics.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    graphics.setColor(Color.CYAN);
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
//                graphics.fillOval(x[0],y[0],UNIT_SIZE,UNIT_SIZE);
                } else {
                    graphics.setColor(Color.pink);
//                    For changing the color
                    graphics.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
                    graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Bungee Shade", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("SCORES: "+applesEaten,(SCREEN_WIDTH-metrics.stringWidth("SCORES: "+applesEaten))/2, graphics.getFont().getSize());
        } else {
            gameOver(graphics);
        }
    }
    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move(){
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }
    public void checkApple(){
        if ((x[0] == appleX) && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }
    public void checkCollisions(){
//        check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
        }
//        check if head touches left border
        if (x[0] < 0) {
            running = false;
        }
//        check if head touches right border
        if (x[0] > SCREEN_WIDTH) {
            running = false;
        }
//        check if head touches top border
        if (y[0] < 0) {
            running = false;
        }
        if (y[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics graphics) {
//        Game Over Text
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Bungee Shade", Font.BOLD, 70));
        FontMetrics metrics = getFontMetrics(graphics.getFont());
        graphics.drawString("GAME OVER",(SCREEN_WIDTH-metrics.stringWidth("GAME OVER"))/2,SCREEN_HEIGHT/2);
//        SCORES
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Bungee Shade", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(graphics.getFont());
        graphics.drawString("SCORES: "+applesEaten,(SCREEN_WIDTH-metrics1.stringWidth("SCORES: "+applesEaten))/2, graphics.getFont().getSize());
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }
    public static class myKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                if (direction != 'L') {
                    direction = 'R';
                }
                break;
                case KeyEvent.VK_UP:
                if (direction != 'D') {
                    direction = 'U';
                }
                break;
                case KeyEvent.VK_DOWN:
                if (direction != 'U') {
                    direction = 'D';
                }
                break;
            }
        }
    }
}
