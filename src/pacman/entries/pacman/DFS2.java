package pacman.entries.pacman;

import java.util.Stack;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Created by QuanhaoLi on 10/14/16.
 */

public class DFS2 extends Controller<MOVE>{

    private Stack<NodeTree> possibleMoves = new Stack<NodeTree>();
    public Stack<MOVE> bestMoves = new Stack<MOVE>();

    private StarterGhosts ghosts = new StarterGhosts();

    private int bestNodeIndex = 0;
    public int bestScore = 0;

    public ActualNodeTree bestNode;

    private int maxDepth;
    private ActualNodeTree n = new ActualNodeTree();

    public DFS2 (int MD) {
        maxDepth = MD;
    }

    private void DFSRecursive (Game game, ActualNodeTree prev) {

        for (MOVE move : game.getPossibleMoves(game.getPacmanCurrentNodeIndex())) {
            n.curr = game.copy();
            n.curr.advanceGame(move, ghosts.getMove());

            if (n.curr.gameOver() || n.depth == maxDepth) {
                if (n.curr.getNumberOfActivePills() == 0 && n.curr.getNumberOfActivePowerPills() == 0) {
                    bestScore = n.curr.getScore();
                    n.depth = prev.depth+1;
                    n.pN = prev;
                    bestNode = n;
                    break;

                } else {
                    return;
                }
            }

            // update score
            if (n.curr.getScore() > bestScore) {
                bestScore = n.curr.getScore();
                n.depth = prev.depth+1;
                n.pN = prev;
                bestNode = n;
            }

            DFSRecursive(n.curr, n);
        }
    }

    private Game DFSDriver(Game game) {
        ActualNodeTree head = new ActualNodeTree();
        head.curr = game.copy();
        head.depth = 0;
        head.prev = null;
        head.pN = null;

        DFSRecursive(game, head);
        ActualNodeTree curr = bestNode;
        bestNode = bestNode.pN;
        return curr.curr;
    }

    @Override
    public MOVE getMove(Game game, long timeDue) {
        //Game g = null;
        //if (bestNode == null) {
        //    g = DFSDriver(game);//, game.getPacmanCurrentNodeIndex());
        //}
        //if (bestNode == null) return MOVE.NEUTRAL;
        MOVE move = DFSDriver(game).getPacmanLastMoveMade();
        if (move == null) return MOVE.NEUTRAL;
        return move;
    }

}
