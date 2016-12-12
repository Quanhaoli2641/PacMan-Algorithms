package pacman.entries.pacman;

import java.util.Stack;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Created by QuanhaoLi on 10/14/16.
 */

public class ItterativeDeepening extends Controller<MOVE>{

    private Stack<MOVE> bestMoves = new Stack<MOVE>();
    private Stack<NodeTree> possibleMoves = new Stack<NodeTree>();

    private Stack<Game> currGames = new Stack<Game>();
    private Stack<Game> nextGames = new Stack<Game>();

    private Game last;

    private int maxDepth;
    private int bestScore = -1;

    private StarterGhosts ghosts = new StarterGhosts();


    public ItterativeDeepening (int MD) {
        maxDepth = MD;
    }

    private void IDRecursive (Game game, int depth, int maxDepth){
        Game nextMove = null;
        for (MOVE move : game.getPossibleMoves(game.getPacmanCurrentNodeIndex())) {
            Game copy = game.copy();
            copy.advanceGame(move, ghosts.getMove());
            nextMove = copy;
            NodeTree node = new NodeTree();
            node.depth = depth;

            if (copy.gameOver() || depth > maxDepth) {
                if (copy.getNumberOfActivePills() == 0 && copy.getNumberOfActivePowerPills() == 0) {

                    node.curr = copy;
                    node.prev = game.copy();
                    possibleMoves.push(node);
                    //found = true;
                    last = copy;
                    break;
                }
                else {
                    bestMoves.clear();
                    return;
                }
            }

            node.curr = copy;
            node.prev = game.copy();
            possibleMoves.push(node);
            if (copy.getScore() > bestScore) {
                bestScore = copy.getScore();
                last = copy;
            }

            IDRecursive(copy,node.depth + 1, maxDepth);
        }

        if (depth > maxDepth) {
            nextGames.push(nextMove);
        }

    }

    private void IDriver(Game game) {
        Game thiscopy = game.copy();
        currGames.push(thiscopy);
        int depth = 0;
        while (!currGames.isEmpty() && maxDepth != 30) {
            IDRecursive(currGames.pop(), depth, maxDepth);
            if (currGames.isEmpty()) {
                while (!nextGames.isEmpty()) {
                    currGames.push(nextGames.pop());
                }
            }
        }
        while(!possibleMoves.isEmpty()) {
            if (last == possibleMoves.peek().curr) {
                bestMoves.push(last.getPacmanLastMoveMade());
                last = possibleMoves.peek().prev;
            }
            possibleMoves.pop();
        }
    }

    @Override
    public MOVE getMove(Game game, long timeDue) {
        if (bestMoves.isEmpty()) IDriver(game);
        if (bestMoves.isEmpty()) return MOVE.NEUTRAL;
        return bestMoves.pop();
    }

}
