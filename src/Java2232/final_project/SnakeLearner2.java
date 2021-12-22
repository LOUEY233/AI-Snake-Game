package Java2232.final_project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
// for no visual training
// the state is not defined so well. It is better to use CNN to grab the whole information.
public class SnakeLearner2 {
	Q_learner learner = new Q_learner();
	Game_1 gameModel;
	private float lastDistance;
	private int lastScore;
	private float reward;
	private float greedyDecay = 0.02f;
	private int generation = 0;
	private int highestScore = 0;
	private int sumScore = 0;
	private boolean stopedLearning = false;

	public SnakeLearner2() throws Exception {
		this.learner = new Q_learner(0.3f, 0.8f, 0.1f, 4, this::getReward, this::executeAction);
		learner.load_data("test.txt");
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
			reward = 120;
			lastScore = currentScore;
			if (stopedLearning) System.out.println("current score:" + currentScore);
		} else if (currentScore < lastScore) {
			reward = -200;
			generation++;
			sumScore += lastScore;
			highestScore = Math.max(lastScore, highestScore);
			float newGreedy = (float) (1f / (float) (highestScore*20));
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
			reward = 3;
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
		SnakeLearner2 player = new SnakeLearner2(); // player
		player.gameModel = new Game_1();  //game environment
		player.learner.init();    //from 0,0,0,0
//		player.learner.load_data("test.txt"); // qtable initialized from existed one
		player.learner.setState(player.gameModel.getState());
		while (player.gameModel.inGame){
			player.learn();
			player.gameModel.checkApple();
			player.gameModel.checkCollision();
		}

	}
}
