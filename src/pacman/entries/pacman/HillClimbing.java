package pacman.entries.pacman;

import pacman.controllers.Controller;
import pacman.game.Game;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Node;

/**
 * Created by QuanhaoLi on 10/20/16.
 */

public class HillClimbing extends Controller<MOVE>{

    private StarterGhosts ghosts = new StarterGhosts();

    @Override
    public MOVE getMove(Game game, long timeDue) {

        MOVE nextMove = MOVE.NEUTRAL;
        double indexVal = 0;

        for (MOVE move : game.getPossibleMoves(game.getPacmanCurrentNodeIndex())){
            Game copy = game.copy();
            copy.advanceGame(move, ghosts.getMove());
            double value = Heuristic.heuristicFuncForNext(copy);
            if (copy.gameOver()) {
                if (copy.getNumberOfPills() == 0 && copy.getNumberOfActivePowerPills() == 0) {
                    nextMove = move;
                    indexVal = value;
                    break;
                }
                else {
                    nextMove = move.opposite();
                    break;
                }
            }
            if (value > indexVal) {
                nextMove = move;
                indexVal = value;
            }

        }
        return nextMove;
    }

}

