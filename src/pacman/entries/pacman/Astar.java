package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Created by QuanhaoLi on 10/19/16.
 */
public class Astar extends Controller<MOVE> {

    private Queue<NodeTree> games = new LinkedList<NodeTree>();
    private Queue<Double> scores = new LinkedList<Double>();

    private Stack<MOVE> moves = new Stack<MOVE>();
    private Stack<NodeTree> possibleMoves = new Stack<NodeTree>();

    private StarterGhosts ghosts = new StarterGhosts();

    private double bestScore = 0;
    private Game bestGame = null;

    public void aStarFill (Game game, int maxDepth) {

        Game copy = game.copy();
        NodeTree head = new NodeTree();
        head.curr = copy;
        head.prev = null;
        double initScore = 0;

        games.add(head);
        scores.add(initScore);
        while (!games.isEmpty()){
            NodeTree currGame = games.remove();
            Game thisgame = currGame.curr;
            double thisScore = scores.remove();

            if (thisScore > bestScore) {
                bestScore = thisScore;
                bestGame = thisgame;
                possibleMoves.push(currGame);

            }

            if (thisgame.gameOver()){
                if (thisgame.getNumberOfActivePills() == 0 && thisgame.getNumberOfActivePowerPills() == 0){
                    break;
                }
                else {
                    return;
                }
            }

            else {

                if (currGame.depth < maxDepth) {

                    LinkedList<NodeTree> n = new LinkedList<NodeTree>();
                    LinkedList<Double> d = new LinkedList<Double>();

                    for (MOVE move : thisgame.getPossibleMoves(thisgame.getPacmanCurrentNodeIndex())) {

                        Game newGame = thisgame.copy();
                        NodeTree newNode = new NodeTree();
                        newNode.prev = thisgame;
                        newNode.depth = currGame.depth +1;
                        newGame.advanceGame(move, ghosts.getMove());
                        newNode.curr = newGame;
                        n.add(newNode);
                        d.add((double)newGame.getScore() + Heuristic.heuristicFuncForNext(newGame));

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


