package Java2232.final_project;

import javax.swing.*;
import java.awt.event.ActionEvent;
// for the AI player
public class Game_1 {
    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 28;
    public int time = 0;
    public int step;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;

    public boolean leftDirection = false;
    public boolean rightDirection = true;
    public boolean upDirection = false;
    public boolean downDirection = false;
    public boolean inGame = true;
    public int score = 0;

    public Game_1() throws Exception {
        initGame();
    }

    public void initGame() {
        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        locateApple();
    }

    public void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            this.score++;
//            System.out.println(this.score);
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
        step += 1;
        if(step == 3000){
            inGame = false;
        }
    }

    public void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }

        if (y[0] <= 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH) {
            inGame = false;
        }

        if (x[0] <= 0) {
            inGame = false;
        }

        if (!inGame) {
            step = 0;
            scoreReset();
            time += 1;
            inGame = true;
            initGame();
        }
    }

    private void scoreReset(){
        this.score = 0;
    }

    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS+1);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS+1);
        apple_y = ((r * DOT_SIZE));
    }
    public void actionPerformed() throws Exception {

        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }
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

}
