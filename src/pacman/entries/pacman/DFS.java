package pacman.entries.pacman;

import java.util.Stack;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Created by QuanhaoLi on 10/14/16.
 */

public class DFS extends Controller<MOVE>{

    private Stack<NodeTree> possibleMoves = new Stack<NodeTree>();
    public Stack<MOVE> bestMoves = new Stack<MOVE>();

    private StarterGhosts ghosts = new StarterGhosts();

    private int bestNodeIndex = 0;

    private int maxDepth;

    public DFS (int MD) {
        maxDepth = MD;
    }

    private void DFSRecursive (Game game, int depth, int bestScore, int prevIndex) {

        for (MOVE move : game.getPossibleMoves(game.getPacmanCurrentNodeIndex())) {
            Game copy = game.copy();
            copy.advanceGame(move, ghosts.getMove());
            NodeTree t = new NodeTree();

            // Handle game over
            // Win or lose

            if (copy.gameOver() || depth == maxDepth) {
                if (copy.getNumberOfActivePills() == 0 && copy.getNumberOfActivePowerPills() == 0) {
                    bestScore = copy.getScore();
                    t.curr = copy;
                    t.prev = game.copy();
                    t.depth = depth +1;
                    possibleMoves.push(t);
                    bestNodeIndex = copy.getPacmanCurrentNodeIndex();

                    break;

                } else {
                    return;
                }
            }

            // update score
            if (copy.getScore() > bestScore) {
                bestScore = copy.getScore();
                t.curr = copy;
                t.prev = game.copy();
                t.depth = depth +1;
                possibleMoves.push(t);
                bestNodeIndex = copy.getPacmanCurrentNodeIndex();
            }

            DFSRecursive(copy, ++depth, bestScore, copy.getPacmanCurrentNodeIndex());
        }
        //if (bestMove == null) return;
        if (possibleMoves.isEmpty()) return;

        while (!possibleMoves.isEmpty()) {
            if (bestNodeIndex == possibleMoves.peek().curr.getPacmanCurrentNodeIndex()) {
                bestMoves.push(possibleMoves.peek().curr.getPacmanLastMoveMade());
                bestNodeIndex = possibleMoves.peek().prev.getPacmanCurrentNodeIndex();
            }
            possibleMoves.pop();
        }
    }

    @Override
    public MOVE getMove(Game game, long timeDue) {
        if (bestMoves.isEmpty()) DFSRecursive(game, 0, -1, 0);//, game.getPacmanCurrentNodeIndex());
        if (bestMoves.isEmpty()) return MOVE.NEUTRAL;
        return bestMoves.pop();
    }

}
