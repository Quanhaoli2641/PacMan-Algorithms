package pacman.entries.pacman;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Created by QuanhaoLi on 10/14/16.
 */

public class ID2 extends Controller<MOVE>{

    private Stack<ActualNodeTree> possibleMoves = new Stack<ActualNodeTree>();
    public Stack<MOVE> bestMoves = new Stack<MOVE>();
    public Queue<Game> games = new LinkedList<Game>();

    private StarterGhosts ghosts = new StarterGhosts();

    public Game bestGameState;

    private int maxDepth;
    private int bestScore = -1;
    private int currDepth = 0;

    public ID2 (int MD) {
        maxDepth = MD;
    }

    private void IDRecursive (Game game, int depth, int maxDepth){

        for (MOVE move : game.getPossibleMoves(game.getPacmanCurrentNodeIndex())) {
            Game copy = game.copy();
            ActualNodeTree a = new ActualNodeTree();
            a.prev = copy;
            copy.advanceGame(move, ghosts.getMove());

            // Handle game over
            // Win or lose

            if (copy.gameOver() || depth == maxDepth) {
                if (copy.getNumberOfActivePills() == 0 && copy.getNumberOfActivePowerPills() == 0) {
                    a.curr = copy;
                    bestScore = copy.getScore();
                    bestGameState = copy;
                    possibleMoves.push(a);
                    break;
                } else {
                    bestMoves.clear();
                    return;
                }
            }

            if (depth < maxDepth) {
                a.curr = copy;
                possibleMoves.push(a);
                // update score
                if (copy.getScore() > bestScore) {
                    bestScore = copy.getScore();
                    bestGameState = copy;
                }
            }

            IDRecursive(copy, ++depth, maxDepth);
        }
        if (depth == maxDepth) {
            games.add(game);
            currDepth = maxDepth;
        }
    }

    private void IDriver(Game game) {
        int depth = 0;
        int localMaxDepth = 10;
        int localBestScore = 0;
        Stack<ActualNodeTree> b = new Stack<ActualNodeTree>();

        Game copy = game.copy();
        games.add(game.copy());
        while (depth != maxDepth && !games.isEmpty()) {
            IDRecursive(games.remove(), depth, localMaxDepth);
            depth = currDepth;
            localMaxDepth += 5;
            if (bestScore > localBestScore) {
                localBestScore = bestScore;
                for (ActualNodeTree ant : possibleMoves) {
                    b.push(ant);
                }
                possibleMoves.clear();
            }

        }
        while (!b.isEmpty()) {
            if (bestGameState == b.peek().curr) {;
                bestMoves.push(b.peek().curr.getPacmanLastMoveMade());
                bestGameState = b.peek().prev;
            }
            b.pop();
        }
    }

    @Override
    public MOVE getMove(Game game, long timeDue) {
        //if (bestMoves.isEmpty())
        IDriver(game);
        //if (bestMoves.isEmpty()) return MOVE.NEUTRAL;
        return bestMoves.pop();
    }

}
