package pacman.entries.pacman;

import java.util.LinkedList;
import java.util.Stack;
import java.util.Queue;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

/**
 * Created by QuanhaoLi on 10/17/16.
 */
public class BFS2 extends Controller<MOVE>{

    private Stack<ActualNodeTree> possibleMoves = new Stack<ActualNodeTree>();
    public Queue<Game> games = new LinkedList<Game>();
    public Stack<MOVE> bestMoves = new Stack<MOVE>();

    private StarterGhosts ghosts = new StarterGhosts();
    private ActualNodeTree a = new ActualNodeTree();

    public Game bestGameState;
    public int bestScore;

    private void BFSRecursive (Game game) {

        for (MOVE move : game.getPossibleMoves(game.getPacmanCurrentNodeIndex())) {

            Game copy = game.copy();
            a.prev = copy;
            copy.advanceGame(move, ghosts.getMove());

            games.add(copy);

            // Handle game over
            // Win or lose

            if (copy.gameOver()) {
                if (copy.getNumberOfActivePills() == 0 && copy.getNumberOfActivePowerPills() == 0) {
                    a.curr = copy;
                    bestScore = copy.getScore();
                    bestGameState = copy;
                    possibleMoves.push(a);
                }
                return;
            }

            // update score

            if (copy.getScore() > bestScore) {
                a.curr = copy;
                possibleMoves.push(a);
                bestGameState = copy;
                bestScore = copy.getScore();
            }

        }

    }

    public void BFSDriver (Game game) {
        int numOfIter = 0;
        int numOfLoop = 0;
        bestGameState = game;
        Game copy = game.copy();
        games.add(copy);
        while (numOfIter < 15) {
            if (numOfLoop == 0){
                numOfIter++;
                numOfLoop = games.size();
            }
            BFSRecursive(games.remove());
            numOfLoop-=1;
        }
        
        while (!possibleMoves.isEmpty()) {
            if (bestGameState == possibleMoves.peek().curr) {
                System.out.print(possibleMoves.peek().curr + " " + bestGameState + "\n");
                bestMoves.push(possibleMoves.peek().curr.getPacmanLastMoveMade());
                bestGameState = possibleMoves.peek().prev;
            }
            possibleMoves.pop();
        }
        return;

    }

    @Override
    public MOVE getMove(Game game, long timeDue) {
        if (bestMoves.isEmpty()) BFSDriver(game);
        if (bestMoves.isEmpty()) return MOVE.NEUTRAL;
        return bestMoves.pop();
    }
}
