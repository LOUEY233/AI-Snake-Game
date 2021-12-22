package Java2232.final_project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;
// original
public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 28;
    private final int DELAY = 140;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;
    SnakeLearner2 player;

    public boolean leftDirection = false;
    public boolean rightDirection = true;
    public boolean upDirection = false;
    public boolean downDirection = false;
    public boolean inGame = true;
    public int time;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;

    public int score;

    public Board() throws Exception {

        initBoard();
    }

    public void initBoard() throws InterruptedException {

        addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
            }
        });
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/resources/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/resources/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/resources/head.png");
        head = iih.getImage();
    }

    public void initGame() throws InterruptedException {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        locateApple();
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        if (inGame) {

            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        }
    }

//    private void gameOver(Graphics g) {
//
//        String msg = "Game Over";
//        Font small = new Font("Helvetica", Font.BOLD, 14);
//        FontMetrics metr = getFontMetrics(small);
//
//        g.setColor(Color.white);
//        g.setFont(small);
//        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
//    }

    public void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            this.score++;
            dots++;
            locateApple();
        }
    }

    public void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    public void checkCollision() throws InterruptedException {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] < 0) {
            inGame = false;
        }

        while (!inGame) {
            scoreReset();
            timer.stop();
            time += 1;
            inGame = true;
            initBoard();
        }
    }

    private void scoreReset(){
        this.score = 0;
    }

    public void locateApple() {

        int r = (int) (Math.random() * RAND_POS+1);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS+1);
        apple_y = ((r * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {
            checkApple();
            try {
                checkCollision();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            move();
        }

        repaint();
    }

    public float distanceToApple(){
        return Math.abs(x[0]-apple_x)+Math.abs(y[0]-apple_y);
    }

    public int getScore() {
        return score;
    }

    public String getState() {
        String position = new String();
        String ax = new String();
        String ay = new String();
        String x_ = new String();
        String y_ = new String();
        if (apple_x<100){
            ax = "0"+String.valueOf(apple_x/10);
        }
        else {
            ax = String.valueOf(apple_x/10);
        }
        if(apple_y<100){
            ay = "0"+String.valueOf(apple_y/10);
        }
        else {
            ay = String.valueOf(apple_y/10);
        }
        if(x[0] < 100){
            x_ = "0"+String.valueOf(x[0]/10);
        }
        else {
            x_ = String.valueOf(x[0]/10);
        }
        if(y[0] < 100){
            y_ = "0"+String.valueOf(y[0]/10);
        }
        else {
            y_ = String.valueOf(y[0]/10);
        }
        position = ax+ay+x_+y_;
        return position;
    }


    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}