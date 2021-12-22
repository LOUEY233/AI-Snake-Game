package Java2232.final_project;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
//basic architecture of the Q-learning
public class Q_learner {
    private float greedyFactor; //Exploration
    private float discountFactor; //gamma how far the future should you see?
    private float learningRate; //alpha

    public HashMap<String,Float[]> qTable;
    private RewardFunction rewardFunc;//<state, action, reward> used to calculate immediate reward after execution of one action
    private ExecuteFunction executeFunc; //<action, state> execute the action, return the resulting state.

    private boolean stopedLearning = false;

    private String State;

    public Q_learner(float greedy, float discount, float rate, int actions, RewardFunction reward, ExecuteFunction execution) {
        greedyFactor = greedy;
        discountFactor = discount;
        learningRate = rate;
        stopedLearning = false;
        this.rewardFunc = reward;
        this.executeFunc = execution;
    }

    public Q_learner() {

    }

    public void init() {
        Float[] value = {0f,0f,0f,0f};
        int number = 0;
        String i_;
        String ii_;
        String iii_;
        String iiii_;
        for (int i = 0; i <= 30; i++) {
            for (int ii = 0; ii <= 30; ii++) {
                for (int iii = 0; iii <= 30; iii++) {
                    for (int iiii = 0; iiii <= 30; iiii++) {
                        if(i < 10){
                            i_ = String.valueOf(number)+String.valueOf(i);
                        }
                        else {
                            i_ = String.valueOf(i);
                        }
                        if(ii < 10){
                            ii_ = String.valueOf(number)+String.valueOf(ii);
                        }
                        else {
                            ii_ = String.valueOf(ii);
                        }
                        if(iii < 10){
                            iii_ = String.valueOf(number)+String.valueOf(iii);
                        }
                        else {
                            iii_ = String.valueOf(iii);
                        }
                        if(iiii < 10){
                            iiii_ = String.valueOf(number)+String.valueOf(iiii);
                        }
                        else {
                            iiii_ = String.valueOf(iiii);
                        }
                        String key = i_+ii_+iii_+iiii_;
                        value = new Float[]{0f,0f,0f,0f};
                        this.qTable.put(key, value);
                    }
                }
            }
        }
    }

    public void save_output(HashMap<String,Float[]> table, String path) throws Exception{

        List<String> key = new ArrayList(table.keySet());
        List<String> output = new ArrayList<>();
        for(int i=0;i<key.size();i++) {
            String temp = key.get(i)+ " ";
            for(Float e: table.get(key.get(i))){
                temp += e+ " ";
            }
            output.add(temp);
        }

        File file = new File(path);

        if (!file.isFile()) {
            file.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        for (String l:output){
            writer.write(l + "\r\n");
        }
        writer.close();
    }

    public void load_data(String path) throws Exception {
        HashMap<String,Float[]> table = new HashMap<>();
        String key = new String();
        Float[] value = new Float[4];
        try (FileReader reader = new FileReader(path);
             BufferedReader br = new BufferedReader(reader)
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                String [] arr = line.split("\\s+");
                key = arr[0];
                for(int i=1;i<arr.length;i++){
                    value[i-1] = Float.parseFloat(arr[i]);
                }
                Float[] value2 = new Float[4];
                for(int k =0;k<4;k++){
                    value2[k] = value[k];
                }
                table.put(key,value2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.qTable = table;
    }

    public void stopLearning() {
        this.stopedLearning = true;
    }

    public void setqTable() {
        init();
    }

    public HashMap<String,Float[]> getqTable() {
        return qTable;
    }

    public void setState(String newState) {
        this.State = newState;
    }

    public void MoveAndLearn() throws Exception {
        //get the next move
        int nextMove = getNextMove(this.State);

        //execute the action
        String oldState = this.State;
        String newState = executeFunc.executeAction(nextMove);

        //get the reward after the last execution
        float reward = rewardFunc.getReward();
        //then update the table

        float currentQ = this.qTable.get(oldState)[nextMove];


        float futureReward = Float.NEGATIVE_INFINITY;
        for (float q : qTable.get(newState)) {
            if (q > futureReward) futureReward = q;
        }

        if (!stopedLearning)
            qTable.get(oldState)[nextMove] = (1 - learningRate) * currentQ + learningRate * (reward + discountFactor * futureReward);
        this.State = newState;
    }

    public void decreaseGreedy(float rate) {
        //decrease greedy factor
        this.greedyFactor -= rate;
        if (this.greedyFactor < 0) this.greedyFactor = 0;
    }
    public float getGreedyFactor(){ return greedyFactor;}

    public void setGreedyFactor(float greedyFactor) {this.greedyFactor = greedyFactor;
    }

    private int getNextMove(String state) {
        //toss the coin, choose if we want greedy or the best strategy.
        Float[] actionArray = qTable.get(state);
        int length = actionArray.length;
        Random rand = new Random();

        if (!stopedLearning && rand.nextFloat() < greedyFactor) {
            //choose a random action
            return (rand.nextInt(length));
        } else {
            int maxAction = 0;
            float maxReward = Float.NEGATIVE_INFINITY;
            int equalCount = 0; //how many actions are max and equal
            for (int i = 0; i < length; i++) {
                if (actionArray[i] > maxReward) {
                    maxReward = actionArray[i];
                    maxAction = i;
                    equalCount = 1;
                } else if (actionArray[i] == maxReward) {
                    equalCount++;
                    maxAction = rand.nextInt(equalCount) < 1 ? i : maxAction;
                }
            }
            return maxAction;
        }
    }

    @FunctionalInterface
    public static interface ExecuteFunction {
        //execute the action, return the state
        String executeAction(int action) throws Exception;
    }

    @FunctionalInterface
    public static interface RewardFunction {
        float getReward();
    }
}