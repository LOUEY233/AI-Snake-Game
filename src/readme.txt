This is the snake game. I add one AI player to learn this.

Q-learning:
   Q-table : hashmap: key:apple position;snake position(String); value: up down left right(float)
   State: Q-table.key
   Reward:  distance(-3,0,3), score(80), gameover(-100)
   Action: Q-table.value
   Environment: --

Q learning setting: greedy 0.3, discount 0.8, learning rate 0.1, action space 4

Document:
Board: The playing environment for AI or customer player visualization
Game_1: The playing environment for AI training
Q_learner: Q learning architecture
Snake: costomer player environment
SnakeLearner2: AI player environment
SnakerLearner3: AI player environment visualization (todo)

run:
if you want to play: Snake.main
let AI play: SnakeLearner2.main




