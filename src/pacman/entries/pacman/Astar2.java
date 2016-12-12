package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import sun.jvm.hotspot.jdi.DoubleValueImpl;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by QuanhaoLi on 10/19/16.
 */
public class Astar2 extends Controller<MOVE> {

    private Queue<ActualNodeTree> games = new LinkedList<ActualNodeTree>();
    private Queue<Double> scores = new LinkedList<Double>();

    private Stack<MOVE> moves = new Stack<MOVE>();
    private Stack<ActualNodeTree> possibleMoves = new Stack<ActualNodeTree>();

    private StarterGhosts ghosts = new StarterGhosts();

    private double bestScore = 0;
    private Game bestGame = null;

    public static double heuristic (Game game) {

        if (game.gameOver()) {
            if (game.getNumberOfActivePills() == 0 && game.getNumberOfActivePowerPills() == 0) {
                return Double.MAX_VALUE;
            }
            else {
                return Double.MIN_VALUE;
            }
        }

        double score = game.getScore();

        double pillScore = 0;
        for (int pill : game.getActivePillsIndices()) {
            pillScore += 100/game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),pill);
        }
        for (int pill : game.getActivePowerPillsIndices()) {
            pillScore += 500/game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(),pill);
        }

        double ghostScore = 0;
        for (GHOST ghost : GHOST.values()) {
            int ghostCloseness = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost));
            if (game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0) {
                if (ghostCloseness < 20) {
                    ghostScore -= 999/(ghostCloseness*ghostCloseness);
                }
            }
            else if (game.getGhostEdibleTime(ghost) == 0) {
                if (game.wasPowerPillEaten()) {
                    ghostScore += 500/(ghostCloseness*ghostCloseness);
                }
                else {
                    ghostScore += 100/(ghostCloseness*ghostCloseness);
                }
            }
        }


        return score*10 + pillScore + ghostScore + game.getPacmanNumberOfLivesRemaining()*1000;
    }

    public void aStarFill (Game game, int maxDepth) {

        Game copy = game.copy();
        ActualNodeTree head = new ActualNodeTree();
        head.curr = copy;
        head.prev = null;
        double initScore = 0;

        games.add(head);
        scores.add(initScore);
        while (!games.isEmpty()){
            ActualNodeTree currGame = games.remove();
            Game thisgame = currGame.curr;
            double thisScore = scores.remove();// + heuristic(thisgame);

            //double heuristicScore = thisScore + heuristic(game);

            if (thisScore > bestScore) {
                bestScore = thisScore;
                bestGame = thisgame;
                possibleMoves.push(currGame);
                System.out.print(thisScore+ "\n");

            }

            if (thisgame.gameOver()){
                if (thisgame.getNumberOfActivePills() == 0 && thisgame.getNumberOfActivePowerPills() == 0){
                    //bestGame = thisgame;
                    //possibleMoves.push(currGame);
                    break;
                }
                else {
                    return;
                }
            }

            else {

                if (currGame.depth < maxDepth) {

                    LinkedList<ActualNodeTree> n = new LinkedList<ActualNodeTree>();
                    LinkedList<Double> d = new LinkedList<Double>();

                    for (MOVE move : thisgame.getPossibleMoves(thisgame.getPacmanCurrentNodeIndex())) {

                        Game newGame = thisgame.copy();
                        ActualNodeTree newNode = new ActualNodeTree();
                        newNode.prev = thisgame;
                        newNode.depth = currGame.depth +1;
                        newGame.advanceGame(move, ghosts.getMove());
                        newNode.curr = newGame;
                        n.add(newNode);
                        d.add((double)newGame.getScore() + heuristic(newGame));

                    }

                    while (!n.isEmpty()) {
                        int high = -1;
                        int highIndex = -1;
                        for (int i = 0; i < n.size(); i++) {
                            if (n.get(i).curr.getScore() > high) {
                                high = n.get(i).curr.getScore();
                                highIndex = i;
                            }
                        }
                        games.add(n.get(highIndex));
                        scores.add(d.get(highIndex));
                        n.remove(highIndex);
                        d.remove(highIndex);
                    }

                }

            }

        }

        while (!possibleMoves.isEmpty()) {
            if (possibleMoves.peek().curr == bestGame) {
                moves.push(possibleMoves.peek().curr.getPacmanLastMoveMade());
                bestGame = possibleMoves.peek().prev;
            }
            possibleMoves.pop();
        }

    }

    @Override
    public MOVE getMove(Game game, long timeDue) {
        if (moves.isEmpty()) aStarFill(game, 10);
        if (moves.isEmpty()) return MOVE.NEUTRAL;
        return moves.pop();
    }

}


