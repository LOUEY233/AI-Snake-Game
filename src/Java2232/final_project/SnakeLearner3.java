package Java2232.final_project;

import javax.swing.*;
import java.awt.*;
//need to implement the CheckPropertyListener so that we can visualize the game with AI player
public class SnakeLearner3 extends JFrame {
    Q_learner learner = new Q_learner();
    Board gameModel;
    private float lastDistance;
    private int lastScore;
    private float reward;
    private float greedyDecay = 0.02f;
    private int generation = 0;
    private int highestScore = 0;
    private int sumScore = 0;
    private boolean stopedLearning = false;

    public SnakeLearner3() throws Exception {
        this.learner = new Q_learner(0.3f, 0.8f, 0.5f, 4, this::getReward, this::executeAction);
        learner.load_data("test.txt");
        initUI();
    }

    private void initUI() throws Exception {

        add(new Board());

        setResizable(false);
        pack();

        setTitle("Snake");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void stopLearning() {
        stopedLearning = true;
        learner.stopLearning();
    }

    public int currentGeneration() {
        return generation;
    }

    public void learn() throws Exception {
        this.learner.MoveAndLearn();
        this.learner.decreaseGreedy(greedyDecay);
    }

    private String executeAction(int action) throws Exception {
        //leftDirection true left, upDirection true up, rightDirection true right, downDirection true down
        switch (action) {
            case 0:
                if (gameModel.rightDirection != true) {
                    gameModel.leftDirection = true;
                    gameModel.upDirection = false;
                    gameModel.downDirection = false;
                }
                break;
            case 1:
                if (gameModel.downDirection != true) {
                    gameModel.upDirection = true;
                    gameModel.leftDirection = false;
                    gameModel.rightDirection = false;
                }
                break;
            case 2:
                if (gameModel.leftDirection!= true) {
                    gameModel.rightDirection = true;
                    gameModel.upDirection = false;
                    gameModel.downDirection = false;
                }
                break;
            case 3:
                if (gameModel.upDirection != true) {
                    gameModel.downDirection = true;
                    gameModel.leftDirection = false;
                    gameModel.rightDirection = false;
                }
                break;
        }
        gameModel.move();
        int currentScore = gameModel.getScore();
        float currentDistance = gameModel.distanceToApple()/10;
        if (currentScore > lastScore) {
            reward = 80;
            lastScore = currentScore;
            if (stopedLearning) System.out.println("current score:" + currentScore);
        } else if (currentScore < lastScore) {
            reward = -100;
            generation++;
            sumScore += lastScore;
            highestScore = Math.max(lastScore, highestScore);
            float newGreedy = (float) (1f / (float) (highestScore));
            if (!stopedLearning) learner.setGreedyFactor(newGreedy);
            if (generation % 100 == 0) {
                System.out.println("current generation:" + generation);
                System.out.println("new greedy factor:" + newGreedy);
                System.out.println("average score:" + sumScore / 100f);
                sumScore = 0;
                System.out.println("high score:" + highestScore);
            }
            if (generation % 1000 == 0) learner.save_output(learner.qTable,"test.txt"); //save the output;
            lastScore = currentScore;
        } else if (currentDistance < lastDistance) {
            reward = 1;
            lastDistance = currentDistance;
        } else if (currentDistance == lastDistance) {
            reward = 0;
            lastDistance = currentDistance;
        } else if (currentDistance > lastDistance) {
            reward = -3;
            lastDistance = currentDistance;
        }


        return gameModel.getState();
    }

    private float getReward() {
        return reward;
    }


    public static void main(String[] args) throws Exception {
        EventQueue.invokeLater(() -> {
            JFrame player = null;
            try {
                player = new SnakeLearner3(); // player
                ((SnakeLearner3) player).gameModel = new Board();  //game environment
                ((SnakeLearner3) player).learner.setState(((SnakeLearner3) player).gameModel.getState());
                ((SnakeLearner3) player).learner.load_data("test.txt");
                if (((SnakeLearner3) player).gameModel.inGame){
                    ((SnakeLearner3) player).learn();
                    ((SnakeLearner3) player).gameModel.checkApple();
                    ((SnakeLearner3) player).gameModel.checkCollision();
                }
                player.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }
}
