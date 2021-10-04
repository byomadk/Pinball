package Pinball;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.security.sasl.RealmCallback;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Graphics2D;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;

    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;

    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;

    private MapGenerator map;

    public Gameplay(){
        map = new MapGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g){
        //background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        //blocks
        map.draw((Graphics2D) g);

        //border
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(681, 0, 3, 592);

        //paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        //ball
        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20, 20);

        if (ballposY > 570){
            play = false;
            // timer.stop();
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over", 250, 300);
        }
        
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        timer.start();
        Rectangle ball = new Rectangle(ballposX, ballposY, 20, 20);
        Rectangle player = new Rectangle(playerX, 550, 100, 8);

        int brickX;
        int brickY;
        int brickWidth;
        int brickHeight;
        Rectangle rect;
        Rectangle ballRect;
        Rectangle brickRect;


        if (play){
            if (ball.intersects(player)){
                ballYdir = -ballYdir;
            }

            A: for (int i = 0; i<map.map.length; i++){
                for (int j = 0; j<map.map[0].length; j++){
                    if (map.map[i][j] > 0){
                        brickX = j * map.brickWidth + 80;
                        brickY = i * map.brickHeight + 50;
                        brickWidth = map.brickWidth;
                        brickHeight = map.brickHeight;

                        rect  = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        brickRect = rect;

                        if (ballRect.intersects(brickRect)){
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width){
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }

                            break A;
                        }
                    }
                }
            }


            ballposX += ballXdir;
            ballposY += ballYdir;
            if (ballposX < 0){
                ballXdir = -ballXdir;
            }
            if (ballposX > 670){
                ballXdir = -ballXdir;
            }
            if (ballposY < 0){
                ballYdir = -ballYdir;
            }            
            if (ballposY > 600){
                timer.stop();
                System.out.println("you lose");
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            if (playerX > 600){
                playerX = 600;
            } else {
                moveRight();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT){
            if (playerX <10){
                playerX = 10;
            } else {
                moveLeft();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        
    }

    private void moveRight() {
        play = true;
        playerX += 20;
    }

    private void moveLeft() {
        play = true;
        playerX -= 20;
    }
    
}
